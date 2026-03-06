# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.kts.

# Keep entry points
-keep class com.appblock.MainActivity { *; }
-keep class com.appblock.service.BlockerService { *; }
-keep class com.appblock.receiver.BootReceiver { *; }

# Keep Kotlin metadata for reflection
-keepattributes RuntimeVisibleAnnotations
-keep class kotlin.Metadata { *; }

# AndroidX / Material
-dontwarn androidx.**
-keep class androidx.** { *; }
