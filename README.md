## lebox

梦工厂开心盒子

## 说明

### Arrow Puzzle APK 沙盒演示

仓库内置了测试游戏 [`apks/arrowc002.apk`](apks/arrowc002.apk)，包名为
`com.weewa.arrowc002`。构建时，`app/build.gradle` 中的
`prepareTestGameAssets` 任务会把该 APK 复制到生成的 assets 目录，因此最终的
开心盒子 APK 已包含游戏安装包，不需要联网下载。

#### 构建和安装开心盒子

```bash
./gradlew :app:assembleDebug
adb install -r -t app/build/outputs/apk/debug/app-debug.apk
```

Debug 构建默认启用本地 Mock 数据，用于替代已经停止服务的
`miniapi_dev.mgc-games.com`。安装完成后，启动桌面上的“开心盒子”。

#### 在 Demo 中安装、运行和卸载游戏

1. 进入开心盒子的“首页”，页面顶部显示“安装”“运行”“卸载”三个按钮。
2. 首次使用时点击“安装”。盒子会将内置的 `arrowc002.apk` 安装到
   NewBlackbox 私有沙盒中；该操作不会调用 Android 系统安装器，也不会把游戏
   安装成手机桌面上的独立应用。
3. “运行”按钮变为可用后点击“运行”，Arrow Puzzle 将在开心盒子的沙盒进程中
   启动。
4. 游戏页面左上角提供悬浮的“返回盒子”按钮。点击后会先切回开心盒子，再异步
   停止游戏沙盒进程，避免 Unity `GameActivity` 在销毁阶段卡死并留下黑屏。
5. 回到首页后点击“卸载”，即可从盒子私有沙盒中删除 Arrow Puzzle。该操作只
   影响沙盒副本，不会卸载手机系统中可能存在的同包名应用。

对应实现：

- `ApkSandboxManager`：复制、安装、启动、停止和卸载沙盒游戏。
- `SandboxGameReturnOverlay`：为游戏添加悬浮返回入口，并安全退出沙盒进程。
- `TabGameFragment`：连接首页三个演示按钮与沙盒操作。

该流程已在 Android 模拟器和 Android 16 / HyperOS 小米 13 真机验证。

### 参数替换

`app/src/main/AndroidManifest.xml`, 以下元数据可以替换:

* `MGC_APPID`: 梦工厂渠道id, 从梦工厂申请
* `MGC_TEST_ENV`: 使用测试环境, 开发时建议为true
* `UMENG_APPKEY`: 友盟app key
* `UMENG_CHANNEL`: 友盟渠道
* `MGC_WECHAT_APPID`: 微信后台app id
* `MGC_WECHAT_APPSECRET`: 微信app secret
* `MGC_ENABLE_WECHAT_LOGIN`: true表示打开微信登录, false表示不是用微信登录, 如果是false, 上面的微信参数也就不用填了

### 签名替换

* 为了微信登录功能正常, 请设置自己的签名证书并在微信后台设置签名指纹

### 支持猎豹游戏

* 如果需要支持猎豹游戏, 可以在`AndroidManifest.xml`中添加一下元数据用于配置猎豹环境

```
<meta-data
    android:name="CMGAME_SDK_ID"
    android:value="猎豹的sdkid" />
<meta-data
    android:name="CMGAME_HOST"
    android:value="猎豹url" />
<meta-data
    android:name="CMGAME_TT_BANNER_ID"
    android:value="头条banner广告id" />
<meta-data
    android:name="CMGAME_TT_VIDEO_ID"
    android:value="头条视频广告id" />
```

### 微信登录

* 如果修改了包名, 请确认也修改了`build.gradle`中的`applicationId`, 如下:

```
    defaultConfig {
        applicationId "com.mgc.letobox.happy" // 这个请修改成你的包名
        
        ...
    }
```

* 另外`WXEntryActivity`需要放置在`包名.wxapi`下, 请移动`WXEntryActivity`以适合你的包名. 比如你的新包名是`a.b`, 则把`WXEntryActivity.java`挪到`a.b.wxapi`包下.

## 有问题可以进QQ群：926072116 讨论
梦工厂LeBox技术讨论群

