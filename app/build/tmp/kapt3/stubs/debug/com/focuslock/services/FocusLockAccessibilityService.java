package com.focuslock.services;

/**
 * Accessibility service used as a fallback / complement to the UsageStats polling.
 * On OEMs that restrict UsageStatsManager (Xiaomi MIUI, Huawei EMUI), accessibility events
 * provide reliable window-change callbacks without polling.
 *
 * When the user switches to a blocked app, this service fires TYPE_WINDOW_STATE_CHANGED
 * and can immediately launch the overlay — no polling delay.
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u0010\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\u000eH\u0016J\b\u0010\u000f\u001a\u00020\fH\u0016J\b\u0010\u0010\u001a\u00020\fH\u0016J\b\u0010\u0011\u001a\u00020\fH\u0014R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bX\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0012"}, d2 = {"Lcom/focuslock/services/FocusLockAccessibilityService;", "Landroid/accessibilityservice/AccessibilityService;", "()V", "job", "Lkotlinx/coroutines/CompletableJob;", "prefManager", "Lcom/focuslock/utils/PreferenceManager;", "profileRepository", "Lcom/focuslock/data/repository/ProfileRepository;", "scope", "Lkotlinx/coroutines/CoroutineScope;", "onAccessibilityEvent", "", "event", "Landroid/view/accessibility/AccessibilityEvent;", "onDestroy", "onInterrupt", "onServiceConnected", "app_debug"})
public final class FocusLockAccessibilityService extends android.accessibilityservice.AccessibilityService {
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.CompletableJob job = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.CoroutineScope scope = null;
    private com.focuslock.utils.PreferenceManager prefManager;
    private com.focuslock.data.repository.ProfileRepository profileRepository;
    
    public FocusLockAccessibilityService() {
        super();
    }
    
    @java.lang.Override()
    protected void onServiceConnected() {
    }
    
    @java.lang.Override()
    public void onAccessibilityEvent(@org.jetbrains.annotations.NotNull()
    android.view.accessibility.AccessibilityEvent event) {
    }
    
    @java.lang.Override()
    public void onInterrupt() {
    }
    
    @java.lang.Override()
    public void onDestroy() {
    }
}