################################################################################
# 通用部分
################################################################################

#代码混淆压缩比，在0~7之间，默认为5，一般不做修改
-optimizationpasses 5

#混合时不使用大小写混合，混合后的类名为小写
-dontusemixedcaseclassnames

#指定不去忽略非公共库的类
-dontskipnonpubliclibraryclasses

#这句话能够使我们的项目混淆后产生映射文件
#包含有类名->混淆后类名的映射关系
-verbose

#指定不去忽略非公共库的类
-dontskipnonpubliclibraryclassmembers

#不做预校验，preverify是proguard的四个步骤之一，Android不需要preverify，去掉这一步能够加快混淆速度。
-dontpreverify

#保留Annotation不混淆
-keepattributes *Annotation*,InnerClasses

#避免混淆泛型
-keepattributes Signature

#抛出异常时保留代码行号
-keepattributes SourceFile,LineNumberTable

#保护js调用方法不被混淆
-keepattributes *JavascriptInterface*
-keepclassmembers class * {
    @android.webkit.JavascriptInterface <methods>;
}

# webview
-keep public class android.net.http.SslError
-keep public class android.webkit.WebViewClient
-dontwarn android.webkit.WebView
-dontwarn android.net.http.SslError
-dontwarn android.webkit.WebViewClient


#指定混淆是采用的算法，后面的参数是一个过滤器
#这个过滤器是谷歌推荐的算法，一般不做更改
-optimizations !code/simplification/cast,!field/*,!class/merging/*

#保留我们使用的四大组件，自定义的Application等等这些类不被混淆
#因为这些子类都有可能被外部调用
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Appliction
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
-keep public class * extends android.view.View
-keep public class com.android.vending.licensing.ILicensingService

-keep public class * extends android.support.v4.app.Fragment


#保留support下的所有类及其内部类
-keep class android.support.** {*;}

#保留R下面的资源
-keep class **.R$* {*;}

#保留本地native方法不被混淆
-keepclasseswithmembernames,includedescriptorclasses class * {
    native <methods>;
}

# 对于用Keep标记过的类/方法/成员等做保持
-keep class android.support.annotation.Keep
-keep @android.support.annotation.Keep class * {*;}
-keepclasseswithmembers class * {
    @android.support.annotation.Keep <methods>;
}
-keepclasseswithmembers class * {
    @android.support.annotation.Keep <fields>;
}
-keepclasseswithmembers class * {
    @android.support.annotation.Keep <init>(...);
}

#保留在Activity中的方法参数是view的方法，
#这样以来我们在layout中写的onClick就不会被影响
-keepclassmembers class * extends android.app.Activity {
    public void *(android.view.View);
}

#保留枚举类不被混淆
-keep enum * { *; }

#保留我们自定义控件（继承自View）不被混淆
-keep public class * extends android.view.View {
    *** get*();
    void set*(***);
    public <init>(android.content.Context);
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>(android.content.Context, android.util.AttributeSet, int);
}

#保留Parcelable序列化类不被混淆
-keep class * implements android.os.Parcelable {
    public static final android.os.Parcelable$Creator *;
}

#保留Serializable序列化的类不被混淆
-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}

#对于带有回调函数的onXXEvent的，不能被混淆
-keepclassmembers class * {
    void *(**On*Event);
}

################################################################################
# 第三方库
################################################################################

# 友盟
-dontwarn com.umeng.**
-keep class com.umeng.** {*;}

# xmlpull
-keep class org.xmlpull.v1.** { *;}
-dontwarn org.xmlpull.v1.**

# avi loader indicator
-keep class com.wang.avi.** { *; }

# rxandroid
-dontwarn io.reactivex.**
-keep class io.reactivex.** {*;}

# okhttp
-dontwarn okio.**
-dontwarn com.kymjs.rxvolley.**
-keep class okio.** {*;}
-keep class com.kymjs.rxvolley.** {*;}
-keep class okhttp3.** {*;}

# eventbus
-keepclassmembers class ** {
    @org.greenrobot.eventbus.Subscribe <methods>;
}
-keep enum org.greenrobot.eventbus.ThreadMode { *; }

# eventbus: Only required if you use AsyncExecutor
-keepclassmembers class * extends org.greenrobot.eventbus.util.ThrowableFailureEvent {
    <init>(java.lang.Throwable);
}

# butterknife
-keep class butterknife.** { *; }
-dontwarn butterknife.internal.**
-keep class **$$ViewBinder { *; }
-keepclasseswithmembernames class * {
    @butterknife.* <fields>;
}
-keepclasseswithmembernames class * {
    @butterknife.* <methods>;
}

# gson
-keep class sun.misc.Unsafe { *; }
-keep class com.google.gson.** { *; }
-keep class com.google.gson.examples.android.model.** { *; }

# mojo
-dontwarn org.codehaus.mojo.**
-keep class org.codehaus.mojo.** { *; }

# 支付宝
-dontwarn com.alipay.**
-dontwarn com.ta.utdid2.**
-dontwarn com.ut.device.**
-dontwarn org.json.alipay.**
-dontwarn com.alipay.android.app.IAlixPay.**
-keep class com.alipay.android.app.IAlixPay.**
-keep class com.alipay.** {*;}
-keep class com.ta.utdid2.** {*;}
-keep class com.ut.device.** {*;}
-keep class org.json.alipay.** {*;}

# 威富通支付
-dontwarn com.switfpass.pay.**
-keep class com.switfpass.pay.** {*;}

# 汇付宝支付
-dontwarn com.heepay.plugin.**
-keep class com.heepay.plugin.** {*;}
-dontwarn com.junnet.**
-keep class com.junnet.** {*;}

# 易联银联支付
-dontwarn com.payeco.android.plugin.**
-keep class com.payeco.android.plugin.** {*;}

# 现在支付
-dontwarn com.ipaynow.plugin.**
-keep class com.ipaynow.plugin.** {*;}

# 梓微信支付
-dontwarn com.zwxpay.**
-keep class com.zwxpay.** {*;}

# 银联支付
-dontwarn com.unionpay.**
-keep class com.unionpay.** {*;}
-dontwarn cn.gov.pbc.**
-keep class cn.gov.pbc.** {*;}
-dontwarn cn.gov.pbc.**
-keep class cn.gov.pbc.** {*;}
-dontwarn com.UCMobile.**
-keep class com.UCMobile.** {*;}

# 聚宝支付
-dontwarn android.app.**
-keep class android.app.** {*;}
-dontwarn com.fanwei.**
-keep class com.fanwei.** {*;}
-dontwarn com.opensdk.**
-keep class com.opensdk.** {*;}

# 百度定位
-dontwarn com.baidu.location.**
-keep class com.baidu.location.** {*;}

# apache
-dontwarn org.apache.**
-keep class org.apache.** {*;}
-dontwarn android.net.http.**
-keep class android.net.http.** { *;}
-keep class com.android.volley.** {*;}

################################################################################
# sample.lebox
################################################################################

# leto
-dontwarn com.ledong.lib.**
-keep class com.ledong.lib.** {*;}
-dontwarn com.leto.game.**
-keep class com.leto.game.** {*;}
-keep class com.mgc.letobox.happy.model.** {*;}

# retrofit
# Retrofit does reflection on generic parameters. InnerClasses is required to use Signature and
# EnclosingMethod is required to use InnerClasses.
-keepattributes Signature, InnerClasses, EnclosingMethod

# Retrofit does reflection on method and parameter annotations.
-keepattributes RuntimeVisibleAnnotations, RuntimeVisibleParameterAnnotations

# Retain service method parameters when optimizing.
-keepclassmembers,allowshrinking,allowobfuscation interface * {
    @retrofit2.http.* <methods>;
}

# Ignore annotation used for build tooling.
-dontwarn org.codehaus.mojo.animal_sniffer.IgnoreJRERequirement

# Ignore JSR 305 annotations for embedding nullability information.
-dontwarn javax.annotation.**

# Guarded by a NoClassDefFoundError try/catch and only used when on the classpath.
-dontwarn kotlin.Unit

# Top-level functions that can only be used by Kotlin.
-dontwarn retrofit2.KotlinExtensions
-dontwarn retrofit2.KotlinExtensions$*

# With R8 full mode, it sees no subtypes of Retrofit interfaces since they are created with a Proxy
# and replaces all potential values with null. Explicitly keeping the interfaces prevents this.
#-if interface * { @retrofit2.http.* <methods>; }
-keep,allowobfuscation interface <1>