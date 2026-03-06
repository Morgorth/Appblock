package com.focuslock.services;

/**
 * Persistent foreground service that polls the foreground app every 1500ms using
 * UsageStatsManager. When a blocked app is detected during an active schedule window,
 * it launches the BlockOverlayActivity above it.
 *
 * Also manages the override countdown timer: tracks when an override expires and
 * sends periodic notification updates.
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000^\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0007\n\u0002\u0010\t\n\u0002\b\u0004\u0018\u0000 \'2\u00020\u0001:\u0001\'B\u0005\u00a2\u0006\u0002\u0010\u0002J\u000e\u0010\u0010\u001a\u00020\u0011H\u0082@\u00a2\u0006\u0002\u0010\u0012J\n\u0010\u0013\u001a\u0004\u0018\u00010\u0014H\u0002J\u0012\u0010\u0015\u001a\u0004\u0018\u00010\u00162\u0006\u0010\u0017\u001a\u00020\u0018H\u0016J\b\u0010\u0019\u001a\u00020\u0011H\u0016J\b\u0010\u001a\u001a\u00020\u0011H\u0016J\"\u0010\u001b\u001a\u00020\u001c2\b\u0010\u0017\u001a\u0004\u0018\u00010\u00182\u0006\u0010\u001d\u001a\u00020\u001c2\u0006\u0010\u001e\u001a\u00020\u001cH\u0016J\u0010\u0010\u001f\u001a\u00020\u00112\u0006\u0010 \u001a\u00020\u0014H\u0002J \u0010!\u001a\u00020\u00112\u0006\u0010 \u001a\u00020\u00142\u0006\u0010\"\u001a\u00020\u00142\u0006\u0010#\u001a\u00020$H\u0002J\b\u0010%\u001a\u00020\u0011H\u0002J\b\u0010&\u001a\u00020\u0011H\u0002R\u0010\u0010\u0003\u001a\u0004\u0018\u00010\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0005\u001a\u0004\u0018\u00010\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\tX\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u000bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\rX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000e\u001a\u00020\u000fX\u0082.\u00a2\u0006\u0002\n\u0000\u00a8\u0006("}, d2 = {"Lcom/focuslock/services/BlockerService;", "Landroidx/lifecycle/LifecycleService;", "()V", "overrideCountdownJob", "Lkotlinx/coroutines/Job;", "pollingJob", "prefManager", "Lcom/focuslock/utils/PreferenceManager;", "profileRepository", "Lcom/focuslock/data/repository/ProfileRepository;", "serviceJob", "Lkotlinx/coroutines/CompletableJob;", "serviceScope", "Lkotlinx/coroutines/CoroutineScope;", "usageStatsManager", "Landroid/app/usage/UsageStatsManager;", "checkForegroundApp", "", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getForegroundApp", "", "onBind", "Landroid/os/IBinder;", "intent", "Landroid/content/Intent;", "onCreate", "onDestroy", "onStartCommand", "", "flags", "startId", "showBlockOverlay", "packageName", "startOverrideCountdown", "appName", "durationMs", "", "startPolling", "stopOverrideCountdown", "Companion", "app_debug"})
public final class BlockerService extends androidx.lifecycle.LifecycleService {
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.CompletableJob serviceJob = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.CoroutineScope serviceScope = null;
    private com.focuslock.utils.PreferenceManager prefManager;
    private android.app.usage.UsageStatsManager usageStatsManager;
    private com.focuslock.data.repository.ProfileRepository profileRepository;
    @org.jetbrains.annotations.Nullable()
    private kotlinx.coroutines.Job pollingJob;
    @org.jetbrains.annotations.Nullable()
    private kotlinx.coroutines.Job overrideCountdownJob;
    private static final long POLL_INTERVAL_MS = 1500L;
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String ACTION_START_OVERRIDE = "com.focuslock.ACTION_START_OVERRIDE";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String ACTION_STOP_OVERRIDE = "com.focuslock.ACTION_STOP_OVERRIDE";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String EXTRA_PACKAGE_NAME = "extra_package_name";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String EXTRA_APP_NAME = "extra_app_name";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String EXTRA_DURATION_MS = "extra_duration_ms";
    @org.jetbrains.annotations.NotNull()
    public static final com.focuslock.services.BlockerService.Companion Companion = null;
    
    public BlockerService() {
        super();
    }
    
    @java.lang.Override()
    public void onCreate() {
    }
    
    @java.lang.Override()
    public int onStartCommand(@org.jetbrains.annotations.Nullable()
    android.content.Intent intent, int flags, int startId) {
        return 0;
    }
    
    private final void startPolling() {
    }
    
    private final java.lang.Object checkForegroundApp(kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    private final java.lang.String getForegroundApp() {
        return null;
    }
    
    private final void showBlockOverlay(java.lang.String packageName) {
    }
    
    private final void startOverrideCountdown(java.lang.String packageName, java.lang.String appName, long durationMs) {
    }
    
    private final void stopOverrideCountdown() {
    }
    
    @java.lang.Override()
    public void onDestroy() {
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public android.os.IBinder onBind(@org.jetbrains.annotations.NotNull()
    android.content.Intent intent) {
        return null;
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000.\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0005\n\u0002\u0010\t\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\b\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u000e\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\u000eJ&\u0010\u000f\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\u000e2\u0006\u0010\u0010\u001a\u00020\u00042\u0006\u0010\u0011\u001a\u00020\u00042\u0006\u0010\u0012\u001a\u00020\u0013R\u000e\u0010\u0003\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\nX\u0082T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0014"}, d2 = {"Lcom/focuslock/services/BlockerService$Companion;", "", "()V", "ACTION_START_OVERRIDE", "", "ACTION_STOP_OVERRIDE", "EXTRA_APP_NAME", "EXTRA_DURATION_MS", "EXTRA_PACKAGE_NAME", "POLL_INTERVAL_MS", "", "start", "", "context", "Landroid/content/Context;", "startOverride", "packageName", "appName", "durationMinutes", "", "app_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
        
        public final void start(@org.jetbrains.annotations.NotNull()
        android.content.Context context) {
        }
        
        public final void startOverride(@org.jetbrains.annotations.NotNull()
        android.content.Context context, @org.jetbrains.annotations.NotNull()
        java.lang.String packageName, @org.jetbrains.annotations.NotNull()
        java.lang.String appName, int durationMinutes) {
        }
    }
}