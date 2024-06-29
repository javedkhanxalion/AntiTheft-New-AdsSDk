# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
-keepclassmembers class fqcn.of.javascript.interface.for.webview {
   public *;
}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

#-keep class me.pqpo.smartcropperlib.**{*;}
-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
-renamesourcefileattribute SourceFile

# prevent Crashlytics obfuscation
-keep class com.crashlytics.** { *; }
-dontwarn com.crashlytics.**


-keep class android.webkit.JavascriptInterface {*;}
-keepclassmembers class * {
@android.webkit.JavascriptInterface *;
}

-keep class com.shockwave.**
-keepclassmembers class com.shockwave.** { *; }

-keepattributes *Annotation*
-keepattributes Exceptions,InnerClasses,Signature,Deprecated,SourceFile,LineNumberTable,*Annotation*,EnclosingMethod

-allowaccessmodification
-optimizations !code/simplification/arithmetic
-overloadaggressively
#-adaptresourcefilenames **.properties,**.gif,**.jpg,**.png,**.xml,**.xsd,**.wsdl
-adaptresourcefilecontents **.properties,META-INF/MANIFEST.MF

-keep class * extends androidx.room.RoomDatabase
-keep @androidx.room.Entity class *
-dontwarn androidx.room.paging.**
-dontwarn android.arch.util.paging.CountedDataSource
-dontwarn android.arch.persistence.room.paging.LimitOffsetDataSource
-keepclassmembers class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator CREATOR;
}
-keep @kotlinx.android.parcel.Parcelize public class *
# Gson specific classes
-dontwarn sun.misc.**
-keep class com.google.gson.stream.** { *; }

# Application classes that will be serialized/deserialized over Gson
-keep class com.google.gson.examples.android.model.** { <fields>; }

# Prevent proguard from stripping interface information from TypeAdapter, TypeAdapterFactory,
# JsonSerializer, JsonDeserializer instances (so they can be used in @JsonAdapter)
-keep class * implements com.google.gson.TypeAdapter
-keep class * implements com.google.gson.TypeAdapterFactory
-keep class * implements com.google.gson.JsonSerializer
-keep class * implements com.google.gson.JsonDeserializer

# Prevent R8 from leaving Data object members always null
-keepclassmembers,allowobfuscation class * {
  @com.google.gson.annotations.SerializedName <fields>;
}

# Retain generic signatures of TypeToken and its subclasses with R8 version 3.0 and higher.
-keep,allowobfuscation,allowshrinking class com.google.gson.reflect.TypeToken
-keep,allowobfuscation,allowshrinking class * extends com.google.gson.reflect.TypeToken
-keep class com.google.gson.reflect.TypeToken
-keep class * extends com.google.gson.reflect.TypeToken
-keep public class * implements java.lang.reflect.Type

-dontwarn androidx.databinding.**
-keep class androidx.databinding.** { *; }
-keep class * implements androidx.viewbinding.ViewBinding {
    public static *** bind(android.view.View);
    public static *** inflate(android.view.LayoutInflater);
}
-keep class com.google.common.* {*;}
-keep class com.google.common.** {*;}
-keep class com.google.common.base.CharMatcher { *; }
-keep class java.util.concurrent.** { *; }
-keep class com.crashlytics.** { *; }
-dontwarn com.crashlytics.**
# ACRA loads Plugins using reflection
-keep class * implements org.acra.plugins.Plugin {*;}
# ACRA uses enum fields in json
-keep class org.acra.* {*;}
-keep class org.acra.** {*;}

# autodsl accesses constructors using reflection
-keepclassmembers class * implements org.acra.config.Configuration { <init>(...); }

# ACRA creates a proxy for this interface
-keep interface org.acra.ErrorReporter
-dontwarn android.support.**

-dontwarn com.faendir.kotlin.autodsl.DslInspect
-dontwarn com.faendir.kotlin.autodsl.DslMandatory
-dontwarn com.google.auto.service.AutoService
-keep class com.crashlytics.** { *; }
-keep class java.util.concurrent.** { *; }
-keep class com.google.common.base.CharMatcher { *; }
-dontwarn com.crashlytics.**
-keep class com.mbridge.** {*; }
-keep interface com.mbridge.** {*; }
-dontwarn com.mbridge.**
-keepclassmembers class **.R$* { public static final int mbridge*; }

-keep public class com.mbridge.* extends androidx.** { *; }
-keep public class androidx.viewpager.widget.PagerAdapter{*;}
-keep public class androidx.viewpager.widget.ViewPager.OnPageChangeListener{*;}
-keep interface androidx.annotation.IntDef{*;}
-keep interface androidx.annotation.Nullable{*;}
-keep interface androidx.annotation.CheckResult{*;}
-keep interface androidx.annotation.NonNull{*;}
-keep public class androidx.fragment.app.Fragment{*;}
-keep public class androidx.core.content.FileProvider{*;}
-keep public class androidx.core.app.NotificationCompat{*;}
-keep public class androidx.appcompat.widget.AppCompatImageView {*;}
-keep public class androidx.recyclerview.*{*;}
-keep class com.mbridge.msdk.foundation.tools.FastKV{*;}
-keep class com.mbridge.msdk.foundation.tools.FastKV$Builder{*;}

-keep interface androidx.annotation.IntDef{ *; }
-keep interface androidx.annotation.Nullable{ *; }
-keep interface androidx.annotation.CheckResult{ *; }
-keep interface androidx.annotation.NonNull{ *; }
-keep public class androidx.fragment.app.Fragment{ *; }
-keep public class androidx.core.content.FileProvider{ *; }
-keep public class androidx.core.app.NotificationCompat{ *; }
-keep public class androidx.appcompat.widget.AppCompatImageView { *; }
-keep public class androidx.recyclerview.*{ *; }
-dontwarn android.content.pm.PackageManager$ResolveInfoFlags
-keepnames class kotlinx.coroutines.** {*;}
-keepnames class kotlinx.coroutines.* {*;}
-keepnames class kotlinx.coroutines.internal.MainDispatcherFactory {}
-keepnames class kotlinx.coroutines.CoroutineExceptionHandler {}
-keepclassmembernames class kotlinx.** {
    volatile <fields>;
}

#-keep interface com.google.firebase.*{ *; }
#-keep interface com.google.firebase.**{ *; }
#-keep public class * implements com.bumptech.glide.module.GlideModule
#-keep class * extends com.bumptech.glide.module.AppGlideModule {
# <init>(...);
#}
#-keep public enum com.bumptech.glide.load.ImageHeaderParser$** {
#  **[] $VALUES;
#  public *;
#}
#-keep class com.bumptech.glide.load.data.ParcelFileDescriptorRewinder$InternalRewinder {
#  *** rewind();
#}
#-keep public class com.google.android.gms.** { public protected *; }