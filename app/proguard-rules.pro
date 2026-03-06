# ─── Room ────────────────────────────────────────────────────────────────────
-keep class com.focuslock.data.db.entities.** { *; }
-keep interface com.focuslock.data.db.dao.** { *; }

# Room generates implementations for DAO interfaces at compile time; keep them.
-keep class * extends androidx.room.RoomDatabase { *; }
-keepclassmembers class * extends androidx.room.RoomDatabase {
    public static ** getInstance(...);
}

# ─── Kotlin ──────────────────────────────────────────────────────────────────
-keepattributes *Annotation*
-keepclassmembers class **$WhenMappings {
    <fields>;
}
-keep class kotlin.Metadata { *; }
-keepclassmembers class kotlin.Metadata {
    public <methods>;
}

# ─── Kotlin Coroutines ───────────────────────────────────────────────────────
-keepnames class kotlinx.coroutines.internal.MainDispatcherFactory {}
-keepnames class kotlinx.coroutines.CoroutineExceptionHandler {}
-keepclassmembernames class kotlinx.** {
    volatile <fields>;
}

# ─── WorkManager ─────────────────────────────────────────────────────────────
-keep class * extends androidx.work.Worker
-keep class * extends androidx.work.ListenableWorker {
    public <init>(android.content.Context, androidx.work.WorkerParameters);
}
-keep class * extends androidx.work.CoroutineWorker {
    public <init>(android.content.Context, androidx.work.WorkerParameters);
}

# ─── ViewBinding ─────────────────────────────────────────────────────────────
-keep class com.focuslock.databinding.** { *; }

# ─── Accessibility Service ───────────────────────────────────────────────────
-keep class com.focuslock.services.FocusLockAccessibilityService { *; }

# ─── Android components (manifest-registered) ────────────────────────────────
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.app.Application

# ─── General Android ─────────────────────────────────────────────────────────
-keepattributes SourceFile,LineNumberTable
-renamesourcefileattribute SourceFile
