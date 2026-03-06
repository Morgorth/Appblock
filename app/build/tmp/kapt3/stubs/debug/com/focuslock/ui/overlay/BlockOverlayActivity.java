package com.focuslock.ui.overlay;

/**
 * Full-screen overlay activity that appears on top of blocked apps.
 *
 * Shows:
 * - Blocked app name and icon
 * - Which profile is causing the block
 * - When the block ends
 * - "Go Back" button (sends user home)
 * - "Emergency Override" button with friction (20-char minimum justification)
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0004\u0018\u0000 \u00152\u00020\u0001:\u0001\u0015B\u0005\u00a2\u0006\u0002\u0010\u0002J\b\u0010\u000b\u001a\u00020\fH\u0002J\b\u0010\r\u001a\u00020\fH\u0002J\b\u0010\u000e\u001a\u00020\fH\u0002J\b\u0010\u000f\u001a\u00020\fH\u0016J\u0012\u0010\u0010\u001a\u00020\f2\b\u0010\u0011\u001a\u0004\u0018\u00010\u0012H\u0014J\b\u0010\u0013\u001a\u00020\fH\u0002J\b\u0010\u0014\u001a\u00020\fH\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\nX\u0082.\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0016"}, d2 = {"Lcom/focuslock/ui/overlay/BlockOverlayActivity;", "Landroidx/appcompat/app/AppCompatActivity;", "()V", "activeProfileName", "", "binding", "Lcom/focuslock/databinding/ActivityBlockOverlayBinding;", "blockedAppName", "blockedPackageName", "prefManager", "Lcom/focuslock/utils/PreferenceManager;", "confirmOverride", "", "goHome", "loadBlockInfo", "onBackPressed", "onCreate", "savedInstanceState", "Landroid/os/Bundle;", "setupButtons", "setupOverrideField", "Companion", "app_debug"})
public final class BlockOverlayActivity extends androidx.appcompat.app.AppCompatActivity {
    private com.focuslock.databinding.ActivityBlockOverlayBinding binding;
    private com.focuslock.utils.PreferenceManager prefManager;
    @org.jetbrains.annotations.NotNull()
    private java.lang.String blockedPackageName = "";
    @org.jetbrains.annotations.NotNull()
    private java.lang.String blockedAppName = "";
    @org.jetbrains.annotations.NotNull()
    private java.lang.String activeProfileName = "";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String EXTRA_PACKAGE_NAME = "extra_package_name";
    private static final int MIN_JUSTIFICATION_LENGTH = 20;
    @org.jetbrains.annotations.NotNull()
    public static final com.focuslock.ui.overlay.BlockOverlayActivity.Companion Companion = null;
    
    public BlockOverlayActivity() {
        super();
    }
    
    @java.lang.Override()
    protected void onCreate(@org.jetbrains.annotations.Nullable()
    android.os.Bundle savedInstanceState) {
    }
    
    @java.lang.Override()
    public void onBackPressed() {
    }
    
    private final void loadBlockInfo() {
    }
    
    private final void setupButtons() {
    }
    
    private final void setupOverrideField() {
    }
    
    private final void confirmOverride() {
    }
    
    private final void goHome() {
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\b\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0007"}, d2 = {"Lcom/focuslock/ui/overlay/BlockOverlayActivity$Companion;", "", "()V", "EXTRA_PACKAGE_NAME", "", "MIN_JUSTIFICATION_LENGTH", "", "app_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
    }
}