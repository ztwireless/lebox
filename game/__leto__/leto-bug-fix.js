define("__leto__/leto-bug-fix.js", function(require, module, exports, process) {
	'use strict'

	function trimSchemaAndSlash(v) {
		var idx = v.indexOf('://')
		if(idx != -1) {
			v = v.substring(idx + 3)
		}
		while(v.startsWith('/')) {
			v = v.substring(1)
		}
		return v
	}

	function convertReaddirResult(files, dirPath) {
		// remove schema and leading slash
		dirPath = trimSchemaAndSlash(dirPath)

		if(window.__isAndroid) {
			/*
			 for Android, old sdk may return two wrong formats
			 first one: files is string, not array, and it contains recursived results
			 second one: files is array, that is right, but sub path contains parent path
			 */

			// if files is string, we need parse it, it is not a valid json string
			var arr = files
			if(typeof files == 'string') {
				try {
					// trim bracket
					files = files.substring(1, files.length - 1)

					// split and add every segment, trim whitespace
					arr = []
					var segments = files.split(',')
					segments.map(f => {
						arr.push(f.trim())
					})
				} catch(e) {
					// if failed, abort, return empty array
					return []
				}
			}

			// remove parent path, and remove recursived path
			var transformedFiles = []
			arr.map(f => {
				f = trimSchemaAndSlash(f)
				if(f.startsWith(dirPath)) {
					f = f.substring(dirPath.length)
				}
				if(f.indexOf('/') == -1) {
					transformedFiles.push(f)
				}
			})
			return transformedFiles
		} else {
			/*
			 for iOS, old sdk returns file paths with parent path included,
			 so we need remove parent path from sub path
			 */

			// remove parent path from result
			var transformedFiles = []
			files.map(f => {
				while(f.startsWith('/')) {
					f = f.substring(1)
				}
				if(f.startsWith(dirPath)) {
					f = f.substring(dirPath.length)
				}
				transformedFiles.push(f)
			})
			return transformedFiles
		}
	}

	(function() {
		// get platform
		if(window.__isAndroid == undefined) {
			var sys = mgc.getSystemInfoSync()
			window.__isAndroid = sys.platform == 'android'
		}

		//////////////////////////////////////////////////////////////////
		// patch readdir for both platform

		// get file system manager and save old readdir methods
		var fs = mgc.getFileSystemManager()
		fs.__readdir = fs.readdir.bind(fs)
		fs.__readdirSync = fs.readdirSync.bind(fs)

		// replace old method
		fs.readdir = function(params) {
			this.__readdir(Object.assign(params, {
				beforeSuccess: res => {
					res.files = convertReaddirResult(res.files, params.dirPath)
				}
			}))
		}.bind(fs)
		fs.readdirSync = function(dirPath) {
			var files = this.__readdirSync(dirPath)
			return convertReaddirResult(files, dirPath)
		}.bind(fs)

		////////////////////////////////////////////////////////////////////
		// patch unzip for android platform, if LetoJSZip is defined
		if(window.__isAndroid && window.LetoJSZip) {
			// save old method
			fs.__unzip = fs.unzip.bind(fs)

			// replace old method
			fs.unzip = function(params) {
				this.readFile({
					filePath: params.zipFilePath,
					success: res => {
						LetoJSZip.loadAsync(res.data).then(zip => {
							var files = Object.keys(zip.files)
							var c = files.length
							files.forEach(filename => {
								zip.files[filename].async('arraybuffer').then(b => {
									this.writeFile({
										filePath: `${params.targetPath}/${filename}`,
										data: b,
										success: res => {
											c--
											if(c <= 0) {
												if(params.success) {
													params.success()
												}
												if(params.complete) {
													params.complete()
												}
											}
										}
									})
								})
							})
						})
					},
					fail: res => {
						if(params.fail) {
							params.fail(res)
						}
						if(params.complete) {
							params.complete()
						}
					}
				})
			}.bind(fs)
		}

		////////////////////////////////////////////////////////////////////
		// patch BannerAd hide/destroy to return promise if framework is lower
		// than 2.6.17
		if(mgc.ext_compareVersion('2.6.17', mgc.SDKVersion) >= 0) {
			var ad = mgc.createBannerAd({
				adUnitId: 'test',
				style: {}
			})
			ad.__proto__.__hide = ad.__proto__.hide
			ad.__proto__.__destroy = ad.__proto__.destroy
			ad.__proto__.hide = function() {
				ad.__hide()
				return new Promise(function(resolve, reject) {
					resolve()
				})
			}
			ad.__proto__.destroy = function() {
				ad.__destroy()
				return new Promise(function(resolve, reject) {
					resolve()
				})
			}
			ad.__destroy()
		}

		////////////////////////////////////////////////////////////////////
		// patch postMessage, merge __leto_game_info__ data to compatibilize
		// old leto sdk bug
		if(mgc.ext_compareVersion('2.6.17', mgc.SDKVersion) >= 0) {
			mgc.__postMessage = mgc.postMessage
			Object.defineProperty(mgc, 'postMessage', {
				configurable: true,
				enumerable: true,
				value: function(params) {
					// if it is task info, merge with cached info to get a big data
					if(params && params.data && params.msg == '__leto_game_info__') {
						window.__leto_game_info_data = params.data = Object.assign(window.__leto_game_info_data || {}, params.data)
					}

					// forward to old method
					mgc.__postMessage(params)
				}
			})
		}

		////////////////////////////////////////////////////////////////////
		// patch request, for form POST, convert json data to key value string
		mgc.__request = mgc.request
		Object.defineProperty(mgc, 'request', {
			configurable: true,
			enumerable: true,
			value: function(params) {
				// if method is post and content type is form encoded, and data
				// is json, convert json data to key value string
				if(params.method == 'POST' && params.header && (params.header['content-type'] || params.header['Content-Type'])) {
					var ct = params.header['content-type'] || params.header['Content-Type']
					if(ct && ct.indexOf('x-www-form-urlencoded') != -1 && params.data) {
						if(typeof(params.data) == 'string' && params.data.startsWith('{') && params.data.endsWith('}')) {
							try {
								params.data = JSON.parse(params.data)
							} catch(e) {
							}
						}
						if(typeof(params.data) == 'object') {
							var buf = ''
							for(var k in params.data) {
								if(buf.length > 0) {
									buf += '&'
								}
								buf += `${k}=${params.data[k]}`
							}
							params.data = buf
						}
					}
				}

				// forward to old method
				mgc.__request(params)
			}
		})
	})()
})
