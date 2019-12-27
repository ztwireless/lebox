define("__leto__/leto-audio-ios.js", function(require, module, exports, process) {
    'use strict'

    // buffer cache, key is partial url, value is buffer
    var bufferCache = {}

    // audio state, copy from cocos
    var State = {
        /**
         * @property {Number} ERROR
         */
        ERROR : -1,
        /**
         * @property {Number} INITIALZING
         */
        INITIALZING: 0,
        /**
         * @property {Number} PLAYING
         */
        PLAYING: 1,
        /**
         * @property {Number} PAUSED
         */
        PAUSED: 2,
        /**
         * @property {Number} STOPPED
         */
        STOPPED: 3,
    }

    // auto increment id counter
    var __leto_audio_id = 0

    class LetoAudio {
        constructor() {
            // src
            this._src = ''

            // buffer
            this._buffer = null

            // create volume
            this._volume = LetoAudio._context['createGain']()
            this._volume['gain'].value = 1
            this._volume['connect'](LetoAudio._context['destination'])

            // loop
            this.loop = false

            // in loading
            this._loading = false

            // pause flag
            this._paused = true

            // Record the currently playing Source
            this._currentSource = null

            // other
            this.autoplay = false
            this._currentTime = 0
            this._duration = 0
            this.obeyMuteSwitch = true
            this.startTime = -1

            // callbacks
            this._endedCallbacks = []
            this._playCallbacks = []
            this._pauseCallbacks = []
            this._stopCallbacks = []
            this._canplayCallbacks = []
            this._errorCallbacks = []
            this._seekedCallbacks = []
            this._seekingCallbacks = []
            this._waitingCallbacks = []
            this._timeUpdateCallbacks = []
            this._finishCallback = null

            // ensure audio context is ok
            this._ensureContext()
        }

        _ensureContext() {
            if(LetoAudio._context.state == 'suspended' && LetoAudio._context['resume']) {
                LetoAudio._context['resume']()
            } else if(LetoAudio._context.state == 'closed') {
                createContext()
            }
        }

        get buffered() {
            return this._duration
        }

        get duration() {
            return this._duration
        }

        get paused() {
            return this._paused
        }

        get currentTime() {
            if(this.startTime >= 0) {
                if(this.paused) {
                    return this._currentTime
                } else {
                    return (this._currentTime + LetoAudio._context.currentTime - this.startTime) % this.duration
                }
            } else {
                return 0
            }
        }

        setCurrentTime(v) {
            // TODO
        }

        getCurrentTime() {
            return this.currentTime
        }

        getDuration() {
            return this.duration
        }

        getState() {
            if(this.paused) {
                return State.PAUSED
            } else {
                return State.PLAYING
            }
        }

        pause() {
            if(!this._paused && this.startTime >= 0 && this._currentSource) {
                // pause
                this._stopSource(this._currentSource)
                this._currentSource = null
                this._currentTime += LetoAudio._context.currentTime - this.startTime
                this._currentTime %= this.duration

                // save flag
                this._paused = true

                // callback
                this._pauseCallbacks.forEach(fn => {
                    fn()
                })
            }
        }

        resume() {
            if(this.paused && !this._currentSource) {
                this.play(this._currentTime)
            }
        }

        get volume() {
            return this._volume['gain'].value
        }

        set volume(num) {
            if(isFinite(num)) {
                this._volume['gain'].value = num
            }
        }

        getVolume() {
            return this.volume
        }

        setVolume(v) {
            this.volume = v
        }

        setLoop(v) {
            this.loop = v
        }

        set loops(v) {
            this.loop = v == 0
        }

        get src() {
            return this._src
        }

        set src(url) {
            if(window.cc && typeof(url) == 'object') {
                if(url.url && url.url.length > 0) {
                    this._setSrc(url.url)
                } else {
                    this._setSrc(url.nativeUrl)
                }
            } else {
                this._setSrc(url)
            }
        }

        get url() {
            return this._src
        }

        set url(v) {
            this.src = v
        }

        _setSrc(url) {
            // resolve url
            if(window.__SubPackPipe) {
                url = window.__SubPackPipe.transformURL(url)
            }
            if(window.__MD5Pipe) {
                // if the url is already md5 piped, don't do it again
                if(!/\.[a-f0-9]{5}\./.test(url)) {
                    url = window.__MD5Pipe.transformURL(url)
                }
            }

            // if same, no need to set
            if(this._src == url) {
                // if no cache, we need ensure loading flag is true
                if(!bufferCache[url]) {
                    this._loading = true
                }
                return
            }

            // set
            this._src = url

            // load if not loaded
            var b = bufferCache[url]
            if(b) {
                // set buffer
                this._buffer = b
                this._duration = b.duration

                // can play callback
                this._canplayCallbacks.forEach(fn => {
                    fn()
                })

                // play
                this.play()
            } else {
                this._loading = true
                this._load(url, (err, buffer) => {
                    // check result
                    if(err) {
                        // check loading flag is still true
                        if(this._loading) {
                            this._errorCallbacks.forEach(fn => {
                                fn(err)
                            })
                        }
                    } else {
                        this._buffer = buffer
                        this._duration = buffer.duration
                        bufferCache[url] = buffer

                        // check loading flag is still true
                        if(this._loading) {
                            // can play callback
                            this._canplayCallbacks.forEach(fn => {
                                fn()
                            })

                            // play
                            this.play()
                        }
                    }

                    // clear flag
                    this._loading = false
                })
            }
        }

        seek(position) {
            // seeking
            this._seekingCallbacks.forEach(fn => {
                fn()
            })

            // re-play
            this.play(position)

            // seeked
            this._seekedCallbacks.forEach(fn => {
                fn()
            })
        }

        play(offset) {
            // if no buffer, return
            if(!this._buffer) {
                return
            }

            // If repeat play, you need to stop before an audio
            if (this._currentSource) {
                this._currentSource.stop(0)
            }

            // ensure audio context is ok
            this._ensureContext()

            // create a buffer source
            var audio = LetoAudio._context["createBufferSource"]()
            audio.buffer = this._buffer
            audio["connect"](this._volume)
            audio.loop = this.loop

            // set start time
            this.startTime = LetoAudio._context.currentTime
            offset = offset || this.currentTime
            this._currentTime = offset

            // start source
            this._startSource(audio, offset)

            // set current source
            this._currentSource = audio

            // set ended callback
            this._currentSource.onended = () => {
                // clean for laya
                if(Laya && Laya.SoundManager && Laya.SoundManager.removeChannel) {
                    Laya.SoundManager.removeChannel(this)
                }

                // callback
                if(this._finishCallback) {
                    this._finishCallback()
                }
            }

            // set flag
            this._paused = false

            // callback
            this._playCallbacks.forEach(fn => {
                fn()
            })
        }

        _startSource(audio, offset) {
            if(audio) {
                try {
                    if (!this.loop) {
                        if (audio.start) {
                            audio.start(0, offset, this._duration - offset)
                        } else if (audio["notoGrainOn"]) {
                            audio["noteGrainOn"](0, offset, this._duration - offset)
                        } else {
                            audio["noteOn"](0, offset, this._duration - offset)
                        }
                    } else {
                        if (audio.start) {
                            audio.start(0)
                        } else if (audio["notoGrainOn"]) {
                            audio["noteGrainOn"](0)
                        } else {
                            audio["noteOn"](0)
                        }
                    }
                } catch(e) {
                }
            }
        }

        _stopSource(audio) {
            if(audio && audio.stop) {
                audio.stop(0)
                audio.onended = null
            }
        }

        stop() {
            // stop, so we reset some fields
            this._currentTime = 0
            var audio = this._currentSource
            this._currentSource = null
            this.startTime = -1
            this._loading = false
            this._paused = true
            this._stopSource(audio)

            // callback
            this._stopCallbacks.forEach(fn => {
                fn()
            })
        }

        destroy() {
            // stop
            this._stopSource(this._currentSource)

            // clear cache
            delete bufferCache[this._src]
            this._buffer = null

            // reset
            this._loading = false
            this._paused = true
            this.startTime = -1
            this._currentTime = 0
            this._duration = 0

            // clear callbacks
            this._endedCallbacks.splice(0, this._endedCallbacks.length)
            this._playCallbacks.splice(0, this._playCallbacks.length)
            this._pauseCallbacks.splice(0, this._pauseCallbacks.length)
            this._stopCallbacks.splice(0, this._stopCallbacks.length)
            this._canplayCallbacks.splice(0, this._canplayCallbacks.length)
            this._errorCallbacks.splice(0, this._errorCallbacks.length)
            this._seekedCallbacks.splice(0, this._seekedCallbacks.length)
            this._seekingCallbacks.splice(0, this._seekingCallbacks.length)
            this._waitingCallbacks.splice(0, this._waitingCallbacks.length)
            this._timeUpdateCallbacks.splice(0, this._timeUpdateCallbacks.length)
        }

        _load(url, cb) {
            // sometime it is internal format, so we need check it in local
            var fs = window.mgc.getFileSystemManager()
            var localExist = true
            try {
                fs.accessSync(url)
            } catch(e) {
                localExist = false
            }

            // if no luck, ensure wdfile schema is checked
            if(!localExist && !url.startsWith('wdfile://')) {
                try {
                    let url2 = fs.resolveUrlSync(`wdfile://${url}`)
                    fs.accessSync(url2)
                    url = url2
                    localExist = true
                } catch(e) {
                }
            }

            // if still no luck, check downloader cache
            if(!localExist) {
                if(window.wxDownloader && window.wxDownloader.cacheDir && window.wxDownloader.getCacheName) {
                    var cachedPath = window.wxDownloader.getCacheName(url)
                    url = window.wxDownloader.cacheDir + '/' + cachedPath
                    url = fs.resolveUrlSync(url)
                }
            } else {
                url = fs.resolveUrlSync(url)
            }

            // create request, here we use original xhr
            var request = new window.NativeGlobal.XMLHttpRequest()
            request.open('GET', url, true)
            request.responseType = 'arraybuffer'

            // Our asynchronous callback
            request.onload = function () {
                if (request._timeoutId >= 0) {
                    clearTimeout(request._timeoutId)
                }
                LetoAudio._context['decodeAudioData'](request.response, function (buffer) {
                    //success
                    cb(null, buffer)
                }, function () {
                    //error
                    cb('decode error - ' + url)
                })
            }

            // on error
            request.onerror = function (e) {
                cb('request error - ' + url)
            }

            // on timeout
            if (request.ontimeout === undefined) {
                request._timeoutId = setTimeout(function () {
                    request.ontimeout()
                }, request.timeout)
            }
            request.ontimeout = function () {
                cb('request timeout - ' + url)
            }

            // send request
            request.send()
        }

        onEnded(cb) {
            this._endedCallbacks.push(cb)
        }

        offEnded(cb) {
            var idx = this._endedCallbacks.indexOf(cb)
            if(idx != -1) {
                this._endedCallbacks.splice(idx, 1)
            }
        }

        onPlay(cb) {
            this._playCallbacks.push(cb)
        }

        offPlay(cb) {
            var idx = this._playCallbacks.indexOf(cb)
            if(idx != -1) {
                this._playCallbacks.splice(idx, 1)
            }
        }

        onPause(cb) {
            this._pauseCallbacks.push(cb)
        }

        offPause(cb) {
            var idx = this._pauseCallbacks.indexOf(cb)
            if(idx != -1) {
                this._pauseCallbacks.splice(idx, 1)
            }
        }

        onStop(cb) {
            this._stopCallbacks.push(cb)
        }

        offStop(cb) {
            var idx = this._stopCallbacks.indexOf(cb)
            if(idx != -1) {
                this._stopCallbacks.splice(idx, 1)
            }
        }

        onCanplay(cb) {
            this._canplayCallbacks.push(cb)
        }

        offCanplay(cb) {
            var idx = this._canplayCallbacks.indexOf(cb)
            if(idx != -1) {
                this._canplayCallbacks.splice(idx, 1)
            }
        }

        onTimeUpdate(cb) {
            this._timeUpdateCallbacks.push(cb)
        }

        offTimeUpdate(cb) {
            var idx = this._timeUpdateCallbacks.indexOf(cb)
            if(idx != -1) {
                this._timeUpdateCallbacks.splice(idx, 1)
            }
        }

        onError(cb) {
            this._errorCallbacks.push(cb)
        }

        offError(cb) {
            var idx = this._errorCallbacks.indexOf(cb)
            if(idx != -1) {
                this._errorCallbacks.splice(idx, 1)
            }
        }

        onSeeked(cb) {
            this._seekedCallbacks.push(cb)
        }

        offSeeked(cb) {
            var idx = this._seekedCallbacks.indexOf(cb)
            if(idx != -1) {
                this._seekedCallbacks.splice(idx, 1)
            }
        }

        onSeeking(cb) {
            this._seekingCallbacks.push(cb)
        }

        offSeeking(cb) {
            var idx = this._seekingCallbacks.indexOf(cb)
            if(idx != -1) {
                this._seekingCallbacks.splice(idx, 1)
            }
        }

        onWaiting(cb) {
            this._waitingCallbacks.push(cb)
        }

        offWaiting(cb) {
            var idx = this._waitingCallbacks.indexOf(cb)
            if(idx != -1) {
                this._waitingCallbacks.splice(idx, 1)
            }
        }
    }

    function createContext() {
        // set native context
        var context = new (window.AudioContext || window.webkitAudioContext || window.mozAudioContext)()
        LetoAudio._context = context

        // check context integrity
        if (!context["createBufferSource"] ||
            !context["createGain"] ||
            !context["destination"] ||
            !context["decodeAudioData"]) {
            throw 'context is incomplete'
        }

        // keep a global reference to inner audio context
        window._letoAudio = LetoAudio
    }

    (function() {
        // we only enable this for ios
        var sys = mgc.getSystemInfoSync()
        if(sys.platform != 'android') {
            // check if browser supports Web Audio
            // check native context
            var supportWebAudio = !!(window.AudioContext || window.webkitAudioContext || window.mozAudioContext)

            // audio support info
            var support = { ONLY_ONE: false, WEB_AUDIO: supportWebAudio, DELAY_CREATE_CTX: false, ONE_SOURCE: false }

            // if iOS, load event is different
            support.USE_LOADER_EVENT = 'loadedmetadata'

            // save audio support info
            window.__letoAudioSupport = support

            // if delay create ctx, or not delay
            if (support.DELAY_CREATE_CTX) {
                setTimeout(function() {
                    createContext()
                }, 0)
            } else {
                createContext()
            }

            // when app is hide, suspend audio context and resume it when it backs
            // we add it here because we won't process it in service webview, just
            // in page webview
            mgc.onHide(function() {
                if(window._letoAudio && window._letoAudio._context && window._letoAudio._context.suspend) {
                    window._letoAudio._context['suspend']()
                }
            })
            mgc.onShow(function() {
                if(window._letoAudio && window._letoAudio._context && window._letoAudio._context.resume) {
                    window._letoAudio._context['resume']()
                }
            })

            // save to global
            window.LetoAudio = LetoAudio

            // replace createInnerAudioContext
            Object.defineProperty(mgc, 'createInnerAudioContext', {
                configurable: true,
                enumerable: true,
                value: function() {
                    return new window.LetoAudio()
                }
            })

            // replace cocos audio engine play method
            if(window.cc && window.cc.audioEngine) {
                window.cc.audioEngine.play = function(clip, loop, volume) {
                    var audio = new LetoAudio()
                    if(typeof(clip) == 'string') {
                        audio.src = clip
                    } else {
                        if(clip.url && clip.url.length > 0) {
                            audio.src = clip.url
                        } else {
                            audio.src = clip.nativeUrl
                        }
                    }
                    audio.loop = loop
                    if(!volume) {
                        volume = 1
                    } else if(typeof(volume) == 'string') {
                        volume = Number.parseFloat(volume)
                    }
                    audio.volume = volume
                    audio.play()

                    // use auto increment id, and manually put it to cc audio cache
                    __leto_audio_id++
                    var _id = `__leto_audio_${__leto_audio_id}__`
                    if(window.cc.audioEngine._id2audio) {
                        window.cc.audioEngine._id2audio[_id] = audio
                    }
                    return _id
                }
            }
        }
    })()

    module.exports = LetoAudio
})
