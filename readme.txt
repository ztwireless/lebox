1. 游戏代码在game目录, 直接修改然后在studio运行即可生效, 工程会自动打包game下的代码到assets

2. 如果想要批量替换, 还是要把游戏代码发给我处理一下, 因为导出的代码我们需要做一点小处理, 我处理完后直接替换game目录下的代码即可.
如果只替换单个js文件, 大部分情况可以手动处理, 其实就是把原始的代码包装在define方法里, 例如:

define("game.js", function(require, module, exports, process) {
    ... // 原始的代码
})

define的第一个参数是js文件路径

3. 如果要查看log, 使用 adb logcat -s Page:* AppService:* chromium:*

4. 如果要在游戏中加log, 使用console.log, 用其它的log方法可能打印不出来