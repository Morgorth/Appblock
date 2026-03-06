package com.focuslock.utils;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00000\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0005\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u000e\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\fJ\u000e\u0010\r\u001a\u00020\u000e2\u0006\u0010\u000b\u001a\u00020\fJ\u000e\u0010\u000f\u001a\u00020\u000e2\u0006\u0010\u000b\u001a\u00020\fJ\u001e\u0010\u0010\u001a\u00020\u000e2\u0006\u0010\u000b\u001a\u00020\f2\u0006\u0010\u0011\u001a\u00020\u00042\u0006\u0010\u0012\u001a\u00020\u0007R\u000e\u0010\u0003\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0007X\u0086T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0013"}, d2 = {"Lcom/focuslock/utils/NotificationUtils;", "", "()V", "CHANNEL_BLOCKER", "", "CHANNEL_OVERRIDE", "NOTIF_ID_BLOCKER", "", "NOTIF_ID_OVERRIDE", "buildBlockerNotification", "Landroid/app/Notification;", "context", "Landroid/content/Context;", "cancelOverrideNotification", "", "createChannels", "showOverrideCountdownNotification", "appName", "remainingSeconds", "app_debug"})
public final class NotificationUtils {
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String CHANNEL_BLOCKER = "focuslock_blocker";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String CHANNEL_OVERRIDE = "focuslock_override";
    public static final int NOTIF_ID_BLOCKER = 1001;
    public static final int NOTIF_ID_OVERRIDE = 1002;
    @org.jetbrains.annotations.NotNull()
    public static final com.focuslock.utils.NotificationUtils INSTANCE = null;
    
    private NotificationUtils() {
        super();
    }
    
    public final void createChannels(@org.jetbrains.annotations.NotNull()
    android.content.Context context) {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final android.app.Notification buildBlockerNotification(@org.jetbrains.annotations.NotNull()
    android.content.Context context) {
        return null;
    }
    
    public final void showOverrideCountdownNotification(@org.jetbrains.annotations.NotNull()
    android.content.Context context, @org.jetbrains.annotations.NotNull()
    java.lang.String appName, int remainingSeconds) {
    }
    
    public final void cancelOverrideNotification(@org.jetbrains.annotations.NotNull()
    android.content.Context context) {
    }
}