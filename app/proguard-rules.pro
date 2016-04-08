    # Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in C:\Program Files\ADT Bundle\sdk/tools/proguard/proguard-android.txt
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

-keep class io.realm.annotations.RealmModule
-keep @io.realm.annotations.RealmModule class *
-keep class io.realm.internal.Keep
-keep @io.realm.internal.Keep class *
-dontwarn javax.**
-dontwarn io.realm.**
-dontwarn com.alibaba.**
-dontwarn com.parse.**
-dontwarn rx.internal.**
-dontwarn com.google.**
-dontwarn com.viewpagerindicator.**
-keepattributes SourceFile,LineNumberTable
-keep public class * extends java.lang.Exception
-dontwarn org.modelmapper.**
-keep class android.support.v7.widget.LinearLayoutManager { *; }
-keepattributes *Annotation*,Signature
-keep class org.openstack.** { *; }
-keep class cn.pedant.SweetAlert.** { *; }
-keep class org.openstack.android.summit.common.data_access.deserialization** { *; }
-keep class com.google.** { *; }