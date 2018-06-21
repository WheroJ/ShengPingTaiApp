# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /Volumes/OSXY_DOC/AndroidSDK/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}


-dontwarn
-ignorewarnings
-optimizationpasses 5
-dontusemixedcaseclassnames
-dontskipnonpubliclibraryclasses
-dontpreverify
-verbose
-useuniqueclassmembernames


-allowaccessmodification

-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*

-keepattributes InnerClasses
-dontoptimize
-keepattributes EnclosingMethod
-keepattributes Signature
-keepattributes *Annotation*
-keep class * extends java.lang.annotation.Annotation { *; }


#-keep public class * extends android.app.Fragment
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
#-keep public class * extends android.support.v4.app.Fragment
-keep public class * extends android.widget.RelativeLayout
-keep public class * extends android.view.View
-keep public class * extends android.widget.LinearLayout


-keep public class * implements java.io.Serializable {*;}
-dontwarn sun.misc.**
-keep class sun.misc.**{ *; }
-dontwarn com.google.gson.**
-keep class com.google.gson.**{ *; }
-keep class com.hongman.wlwl.entity.**{ *; }


-keepclasseswithmembernames class * {
    native <methods>;
}

-keep class butterknife.** { *; }
-dontwarn butterknife.internal.**
-keep class **$$ViewBinder { *; }
-keepclasseswithmembernames class * {
    @butterknife.* <fields>;
}
-keepclasseswithmembernames class * {
    @butterknife.* <methods>;
}

-dontwarn com.facebook.**
-keep class com.facebook.** { *; }

-dontwarn cn.jpush.**
-keep class cn.jpush.** { *; }

-dontwarn com.scottyab.**
-keep class com.scottyab.** { *; }

-keep class org.apache.** { *; }

-dontwarn com.squareup.**
-keep class com.squareup.** { *; }

-keep class maven.com.squareup.** { *; }

-keep class com.afinal.** { *; }

-keep class com.flyco.** { *; }

-dontwarn com.amap.api.**
-keep class com.amap.api.**{*;}
-dontwarn com.autonavi.**
-keep class com.autonavi.**{*;}

-dontwarn org.codehaus.**
-keep class org.codehaus.**{*;}

-dontwarn com.alipay.**
-keep class com.alipay.android.app.IAlixPay{*;}
-keep class com.alipay.android.app.IAlixPay$Stub{*;}
-keep class com.alipay.android.app.IRemoteServiceCallback{*;}
-keep class com.alipay.android.app.IRemoteServiceCallback$Stub{*;}
-keep class com.alipay.sdk.app.PayTask{ public *;}
-keep class com.alipay.sdk.app.AuthTask{ public *;}


-dontwarn com.tencent.mm.**
-keep class com.tencent.mm.**{*;}
-keep class com.tencent.mm.sdk.openapi.**{*;}
-dontwarn com.sina.**
-keep class com.sina.**{*;}
-keep class cn.sharesdk.**{*;}
-keep class com.sina.**{*;}
-keep class **.R$* {*;}
-keep class **.R{*;}
-keep class com.mob.**{*;}
-dontwarn com.mob.**
-dontwarn cn.sharesdk.**
-dontwarn **.R$*


-keep class com.alipay.android.app.IAlixPay{*;}
-keep class com.alipay.android.app.IAlixPay$Stub{*;}
-keep class com.alipay.android.app.IRemoteServiceCallback{*;}
-keep class com.alipay.android.app.IRemoteServiceCallback$Stub{*;}
-keep class com.alipay.sdk.app.PayTask{ public *;}
-keep class com.alipay.sdk.app.AuthTask{ public *;}
-keep class com.alipay.sdk.app.H5PayCallback {
    <fields>;
    <methods>;
}
-keep class com.alipay.android.phone.mrpc.core.** { *; }
-keep class com.alipay.apmobilesecuritysdk.** { *; }
-keep class com.alipay.mobile.framework.service.annotation.** { *; }
-keep class com.alipay.mobilesecuritysdk.face.** { *; }
-keep class com.alipay.tscenter.biz.rpc.** { *; }
-keep class org.json.alipay.** { *; }
-keep class com.alipay.tscenter.** { *; }
-keep class com.ta.utdid2.** { *;}
-keep class com.ut.device.** { *;}

-assumenosideeffects class com.zrspring.libv2.util.LogUtil {
public static void deMessage(...);
public static void warningMessage(...);
public static void infoMessage(...);
public static void infoMessageOld(...);
public static void json(...);
public static void errorMessage(...);
}

-assumenosideeffects class android.util.Log {
 public static int d(...);
 public static int v(...);
 public static int i(...);
 public static int e(...);
}

#retrofit混淆相关
-dontwarn okio.**
-dontwarn javax.annotation.**