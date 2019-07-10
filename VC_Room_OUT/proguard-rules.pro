# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile
-optimizationpasses 5
-dontusemixedcaseclassnames
-dontskipnonpubliclibraryclasses
-dontskipnonpubliclibraryclassmembers
-dontpreverify
-verbose
-printmapping proguardMapping.txt
-optimizations !code/simplification/cast,!field/*,!class/merging/*
-keepattributes *Annotation*,InnerClasses
-keepattributes Signature
-keepattributes SourceFile,LineNumberTable
##----------------------------------------------------------------------------
#
##---------------------------------默认保留区---------------------------------
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
-keep public class * extends android.view.View
-keep public class com.android.vending.licensing.ILicensingService
-keep class android.support.** {*;}
#
#-keepclasseswithmembernames class * {
#    native &lt;methods&gt;;
#}
#-keepclassmembers class * extends android.app.Activity{
#    public void *(android.view.View);
#}
#-keepclassmembers enum * {
#    public static **[] values();
#    public static ** valueOf(java.lang.String);
#}
#-keep public class * extends android.view.View{
#    *** get*();
#    void set*(***);
#    public &lt;init&gt;(android.content.Context);
#    public &lt;init&gt;(android.content.Context, android.util.AttributeSet);
#    public &lt;init&gt;(android.content.Context, android.util.AttributeSet, int);
#}
#-keepclasseswithmembers class * {
#    public &lt;init&gt;(android.content.Context, android.util.AttributeSet);
#    public &lt;init&gt;(android.content.Context, android.util.AttributeSet, int);
#}
#-keep class * implements android.os.Parcelable {
#  public static final android.os.Parcelable$Creator *;
#}
#-keepclassmembers class * implements java.io.Serializable {
#    static final long serialVersionUID;
#    private static final java.io.ObjectStreamField[] serialPersistentFields;
#    private void writeObject(java.io.ObjectOutputStream);
#    private void readObject(java.io.ObjectInputStream);
#    java.lang.Object writeReplace();
#    java.lang.Object readResolve();
#}
-keep class **.R$* {
 *;
}
-keepclassmembers class * {
    void *(**On*Event);
    void *(**Listener);
}

#-keep public class com.demo.li.vc_room_out.library.** { *; }
-keep public class com.demo.li.vc_room_out.library.Global { *; }
-keep public class com.demo.li.vc_room_out.library.entity.* { *; }

-keep public class com.demo.li.vc_room_out.sockethelper.HeartbeatThread { *; }
-keep public class com.demo.li.vc_room_out.sockethelper.SocketVisitClient { *; }
-keep public class com.demo.li.vc_room_out.sockethelper.VisitReceiver{ *; }
-keep public class com.demo.li.vc_room_out.sockethelper.VisitReceiver$* { *; }

#--------(实体Model不能混淆，否则找不到对应的属性获取不到值)-----
#-dontwarn class com.demo.li.vc_room_out.library.entity.*
#不混淆资源类下static的
#-keepclassmembers class **.R$* {
#    public static <fields>;
#}

