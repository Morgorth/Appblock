package com.focuslock.ui.onboarding;

/**
 * Multi-step onboarding that guides the user through granting the required permissions:
 * Step 0: Usage Stats Access
 * Step 1: Draw Over Apps (System Alert Window)
 * Step 2: Accessibility Service (optional but recommended)
 * Step 3: OEM battery whitelist (shown only on problematic OEMs)
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00004\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010!\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0007\u0018\u0000 \u00182\u00020\u0001:\u0001\u0018B\u0005\u00a2\u0006\u0002\u0010\u0002J\b\u0010\u000b\u001a\u00020\fH\u0002J\b\u0010\r\u001a\u00020\fH\u0002J\b\u0010\u000e\u001a\u00020\fH\u0002J\b\u0010\u000f\u001a\u00020\fH\u0002J\u0012\u0010\u0010\u001a\u00020\f2\b\u0010\u0011\u001a\u0004\u0018\u00010\u0012H\u0014J\b\u0010\u0013\u001a\u00020\fH\u0014J\b\u0010\u0014\u001a\u00020\fH\u0002J\u0010\u0010\u0015\u001a\u00020\f2\u0006\u0010\u0016\u001a\u00020\u0006H\u0002J\b\u0010\u0017\u001a\u00020\fH\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bX\u0082.\u00a2\u0006\u0002\n\u0000R\u0014\u0010\t\u001a\b\u0012\u0004\u0012\u00020\u00060\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0019"}, d2 = {"Lcom/focuslock/ui/onboarding/OnboardingActivity;", "Landroidx/appcompat/app/AppCompatActivity;", "()V", "binding", "Lcom/focuslock/databinding/ActivityOnboardingBinding;", "currentStep", "", "prefManager", "Lcom/focuslock/utils/PreferenceManager;", "steps", "", "advanceStep", "", "completeOnboarding", "goToMain", "handlePrimaryAction", "onCreate", "savedInstanceState", "Landroid/os/Bundle;", "onResume", "setupListeners", "showStep", "step", "updateStepUi", "Companion", "app_debug"})
public final class OnboardingActivity extends androidx.appcompat.app.AppCompatActivity {
    private com.focuslock.databinding.ActivityOnboardingBinding binding;
    private com.focuslock.utils.PreferenceManager prefManager;
    private int currentStep = 0;
    @org.jetbrains.annotations.NotNull()
    private final java.util.List<java.lang.Integer> steps = null;
    private static final int STEP_USAGE_STATS = 0;
    private static final int STEP_OVERLAY = 1;
    private static final int STEP_ACCESSIBILITY = 2;
    private static final int STEP_BATTERY = 3;
    @org.jetbrains.annotations.NotNull()
    public static final com.focuslock.ui.onboarding.OnboardingActivity.Companion Companion = null;
    
    public OnboardingActivity() {
        super();
    }
    
    @java.lang.Override()
    protected void onCreate(@org.jetbrains.annotations.Nullable()
    android.os.Bundle savedInstanceState) {
    }
    
    @java.lang.Override()
    protected void onResume() {
    }
    
    private final void setupListeners() {
    }
    
    private final void handlePrimaryAction() {
    }
    
    private final void showStep(int step) {
    }
    
    private final void updateStepUi() {
    }
    
    private final void advanceStep() {
    }
    
    private final void completeOnboarding() {
    }
    
    private final void goToMain() {
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0004\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\b"}, d2 = {"Lcom/focuslock/ui/onboarding/OnboardingActivity$Companion;", "", "()V", "STEP_ACCESSIBILITY", "", "STEP_BATTERY", "STEP_OVERLAY", "STEP_USAGE_STATS", "app_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
    }
}