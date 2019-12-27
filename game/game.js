define("game.js", function(require, module, exports, process) {
 require('libs/weapp-adapter/index');
var Parser = require('libs/xmldom/dom-parser');
window.DOMParser = Parser.DOMParser;
require('libs/wx-downloader.js');
require('src/settings.0c446');
var settings = window._CCSettings;
var SubPackPipe = require('./libs/subpackage-pipe');
require('main.695ab');
require(settings.debug ? 'cocos2d-js.js' : 'cocos2d-js-min.6981b.js');
require('./libs/engine/index.js');
require('./libs/weapp-adapter/XMLHttpRequest.js')

wxDownloader.REMOTE_SERVER_ROOT = "";
wxDownloader.SUBCONTEXT_ROOT = "";
var pipeBeforeDownloader = cc.loader.md5Pipe || cc.loader.assetLoader;
cc.loader.insertPipeAfter(pipeBeforeDownloader, wxDownloader);

if (settings.subpackages) {
    var subPackPipe = new SubPackPipe(settings.subpackages);
    cc.loader.insertPipeAfter(pipeBeforeDownloader, subPackPipe);
}

if (cc.sys.browserType === cc.sys.BROWSER_TYPE_WECHAT_GAME_SUB) {
    require('./libs/sub-context-adapter');
}
else {
    // Release Image objects after uploaded gl texture
    cc.macro.CLEANUP_IMAGE_CACHE = true;
}

window.boot(); 
})