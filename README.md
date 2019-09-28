## lebox

梦工厂开心盒子

## 说明

### 参数替换

`app/src/main/AndroidManifest.xml`, 以下元数据可以替换:

* `MGC_APPID`: 梦工厂渠道id, 从梦工厂申请
* `MGC_TEST_ENV`: 使用测试环境, 开发时建议为true
* `UMENG_APPKEY`: 友盟app key
* `UMENG_CHANNEL`: 友盟渠道
* `MGC_WECHAT_APPID`: 微信后台app id
* `MGC_WECHAT_APPSECRET`: 微信app secret

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

## 有问题可以扫码进群讨论

![Group BarCode](group_barcode.jpg)