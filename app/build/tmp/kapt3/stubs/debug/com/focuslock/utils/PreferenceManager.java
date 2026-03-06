package com.focuslock.utils;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000@\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\t\n\u0002\b\u0005\n\u0002\u0010\u000e\n\u0002\b\u0005\n\u0002\u0010\b\n\u0002\b\u0005\n\u0002\u0010\u000b\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0004\u0018\u0000 $2\u00020\u0001:\u0001$B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u0006\u0010 \u001a\u00020!J\u000e\u0010\"\u001a\u00020\u00182\u0006\u0010#\u001a\u00020\fR$\u0010\u0007\u001a\u00020\u00062\u0006\u0010\u0005\u001a\u00020\u00068F@FX\u0086\u000e\u00a2\u0006\f\u001a\u0004\b\b\u0010\t\"\u0004\b\n\u0010\u000bR$\u0010\r\u001a\u00020\f2\u0006\u0010\u0005\u001a\u00020\f8F@FX\u0086\u000e\u00a2\u0006\f\u001a\u0004\b\u000e\u0010\u000f\"\u0004\b\u0010\u0010\u0011R$\u0010\u0013\u001a\u00020\u00122\u0006\u0010\u0005\u001a\u00020\u00128F@FX\u0086\u000e\u00a2\u0006\f\u001a\u0004\b\u0014\u0010\u0015\"\u0004\b\u0016\u0010\u0017R$\u0010\u0019\u001a\u00020\u00182\u0006\u0010\u0005\u001a\u00020\u00188F@FX\u0086\u000e\u00a2\u0006\f\u001a\u0004\b\u001a\u0010\u001b\"\u0004\b\u001c\u0010\u001dR\u000e\u0010\u001e\u001a\u00020\u001fX\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006%"}, d2 = {"Lcom/focuslock/utils/PreferenceManager;", "", "context", "Landroid/content/Context;", "(Landroid/content/Context;)V", "value", "", "activeOverrideExpiryMs", "getActiveOverrideExpiryMs", "()J", "setActiveOverrideExpiryMs", "(J)V", "", "activeOverridePackage", "getActiveOverridePackage", "()Ljava/lang/String;", "setActiveOverridePackage", "(Ljava/lang/String;)V", "", "defaultOverrideDurationMinutes", "getDefaultOverrideDurationMinutes", "()I", "setDefaultOverrideDurationMinutes", "(I)V", "", "onboardingComplete", "getOnboardingComplete", "()Z", "setOnboardingComplete", "(Z)V", "prefs", "Landroid/content/SharedPreferences;", "clearOverride", "", "isOverrideActive", "packageName", "Companion", "app_debug"})
public final class PreferenceManager {
    @org.jetbrains.annotations.NotNull()
    private final android.content.SharedPreferences prefs = null;
    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String PREF_NAME = "focuslock_prefs";
    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String KEY_ONBOARDING_COMPLETE = "onboarding_complete";
    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String KEY_OVERRIDE_DURATION = "override_duration_minutes";
    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String KEY_ACTIVE_OVERRIDE_PKG = "active_override_package";
    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String KEY_ACTIVE_OVERRIDE_EXPIRY = "active_override_expiry";
    @org.jetbrains.annotations.NotNull()
    public static final com.focuslock.utils.PreferenceManager.Companion Companion = null;
    
    public PreferenceManager(@org.jetbrains.annotations.NotNull()
    android.content.Context context) {
        super();
    }
    
    public final boolean getOnboardingComplete() {
        return false;
    }
    
    public final void setOnboardingComplete(boolean value) {
    }
    
    public final int getDefaultOverrideDurationMinutes() {
        return 0;
    }
    
    public final void setDefaultOverrideDurationMinutes(int value) {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getActiveOverridePackage() {
        return null;
    }
    
    public final void setActiveOverridePackage(@org.jetbrains.annotations.NotNull()
    java.lang.String value) {
    }
    
    public final long getActiveOverrideExpiryMs() {
        return 0L;
    }
    
    public final void setActiveOverrideExpiryMs(long value) {
    }
    
    public final void clearOverride() {
    }
    
    public final boolean isOverrideActive(@org.jetbrains.annotations.NotNull()
    java.lang.String packageName) {
        return false;
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0005\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\t"}, d2 = {"Lcom/focuslock/utils/PreferenceManager$Companion;", "", "()V", "KEY_ACTIVE_OVERRIDE_EXPIRY", "", "KEY_ACTIVE_OVERRIDE_PKG", "KEY_ONBOARDING_COMPLETE", "KEY_OVERRIDE_DURATION", "PREF_NAME", "app_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
    }
}