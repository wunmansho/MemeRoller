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
# Proguard config for project using GMS

-keep class com.google.** { *; }
-keep class io.reactivex.** { *; }

-keep class com.google.protobuf.** { *; }
-keep class android.content.res.** { *; }
-keep class android.os.** { *; }
-keep class com.google.android.gms.tagmanager.** { *; }
-keep class com.google.protobuf.** { *; }


-keep class libcore.io.** { *; }
-keep class org.robolectric.** { *; }
-keep class com.google.ads.** { *; }
-keep class com.google.android.gms.ads.** { *; }
-keep class com.google.android.gms.** { *; }

-keep class com.google.firebase.** { *; }
-keep class com.google.protobuf.** { *; }
-keep class kotlin.** { *; }



-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public class * extends com.bumptech.glide.module.AppGlideModule
-keep public enum com.bumptech.glide.load.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}


-keepclasseswithmembers class **.R$* {
    public static final int define_*;
}

-dontwarn com.google.**
-dontwarn io.reactivex.**
-dontwarn kotlin.Metadata.**
-dontwarn com.google.protobuf.**
-dontwarn android.content.res.**
-dontwarn android.os.**
-dontwarn com.google.android.**
-dontwarn com.google.protobuf.**


-dontwarn libcore.io.**
-dontwarn org.robolectric.**
-dontwarn com.google.ads.**
-dontwarn com.google.android.gms.ads.**

-dontwarn com.google.android.gms.dynamic.**
-dontwarn com.google.android.gms.internal.**

-dontwarn com.google.android.gms.measurement.**
-dontwarn com.google.android.gms.plus.PlusOneButton$OnPlusOneClickListener
-dontwarn com.google.android.gms.tagmanager.zzce
-dontwarn com.google.android.gms.**
-dontwarn com.google.firebase.**

-dontwarn com.google.protobuf.**

-keep class com.google.android.gms.internal.** { *; }
-keep public class com.google.android.gms.* { public *; }
-dontwarn com.google.android.gms.**
# For Google Play Services
-keep public class com.google.android.gms.ads.**{
   public *;
}

# For old ads classes
-keep public class com.google.ads.**{
   public *;
}

# For mediation
-keepattributes *Annotation*

# Other required classes for Google Play Services
# Read more at http://developer.android.com/google/play-services/setup.html
-keep class * extends java.util.ListResourceBundle {
   protected Object[][] getContents();
}

-keep public class com.google.android.gms.common.internal.safeparcel.SafeParcelable {
   public static final *** NULL;
}

-dontwarn androidx.**
-keep class androidx.** { *; }
-keep interface androidx.** { *; }




