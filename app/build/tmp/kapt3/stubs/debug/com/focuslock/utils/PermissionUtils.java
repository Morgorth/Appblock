package com.focuslock.utils;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000 \n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0007\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u000e\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0006J\u0006\u0010\u0007\u001a\u00020\bJ\u000e\u0010\t\u001a\u00020\b2\u0006\u0010\u0005\u001a\u00020\u0006J\u000e\u0010\n\u001a\u00020\b2\u0006\u0010\u0005\u001a\u00020\u0006J\u0006\u0010\u000b\u001a\u00020\bJ\u000e\u0010\f\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0006J\u000e\u0010\r\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0006J\u000e\u0010\u000e\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0006\u00a8\u0006\u000f"}, d2 = {"Lcom/focuslock/utils/PermissionUtils;", "", "()V", "allCorePermissionsGranted", "", "context", "Landroid/content/Context;", "getAccessibilitySettingsIntent", "Landroid/content/Intent;", "getBatteryOptimizationIntent", "getOverlayPermissionIntent", "getUsageStatsIntent", "hasAccessibilityPermission", "hasOverlayPermission", "hasUsageStatsPermission", "app_debug"})
public final class PermissionUtils {
    @org.jetbrains.annotations.NotNull()
    public static final com.focuslock.utils.PermissionUtils INSTANCE = null;
    
    private PermissionUtils() {
        super();
    }
    
    public final boolean hasUsageStatsPermission(@org.jetbrains.annotations.NotNull()
    android.content.Context context) {
        return false;
    }
    
    public final boolean hasOverlayPermission(@org.jetbrains.annotations.NotNull()
    android.content.Context context) {
        return false;
    }
    
    public final boolean hasAccessibilityPermission(@org.jetbrains.annotations.NotNull()
    android.content.Context context) {
        return false;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final android.content.Intent getUsageStatsIntent() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final android.content.Intent getOverlayPermissionIntent(@org.jetbrains.annotations.NotNull()
    android.content.Context context) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final android.content.Intent getAccessibilitySettingsIntent() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final android.content.Intent getBatteryOptimizationIntent(@org.jetbrains.annotations.NotNull()
    android.content.Context context) {
        return null;
    }
    
    public final boolean allCorePermissionsGranted(@org.jetbrains.annotations.NotNull()
    android.content.Context context) {
        return false;
    }
}