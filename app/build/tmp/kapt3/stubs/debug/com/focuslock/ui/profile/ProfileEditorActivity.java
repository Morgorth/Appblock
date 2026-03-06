package com.focuslock.ui.profile;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000b\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\b\n\u0002\u0018\u0002\n\u0002\b\u0004\u0018\u0000 +2\u00020\u0001:\u0001+B\u0005\u00a2\u0006\u0002\u0010\u0002J\u0016\u0010\u0011\u001a\u00020\u00122\f\u0010\u0013\u001a\b\u0012\u0004\u0012\u00020\u00150\u0014H\u0002J\u0018\u0010\u0016\u001a\u00020\u00122\u0006\u0010\u0017\u001a\u00020\u00182\u0006\u0010\u0019\u001a\u00020\u001aH\u0002J\u0012\u0010\u001b\u001a\u00020\u00122\b\u0010\u001c\u001a\u0004\u0018\u00010\u001dH\u0014J\b\u0010\u001e\u001a\u00020\u001fH\u0016J\u0016\u0010 \u001a\u00020\u00122\f\u0010!\u001a\b\u0012\u0004\u0012\u00020\u001a0\u0014H\u0002J\b\u0010\"\u001a\u00020\u0012H\u0002J\b\u0010#\u001a\u00020\u0012H\u0002J2\u0010$\u001a\u00020\u00122\u0006\u0010%\u001a\u00020\u00152\u0006\u0010&\u001a\u00020\u00152\u0018\u0010\'\u001a\u0014\u0012\u0004\u0012\u00020\u0015\u0012\u0004\u0012\u00020\u0015\u0012\u0004\u0012\u00020\u00120(H\u0002J\u0010\u0010)\u001a\u00020\u00122\u0006\u0010*\u001a\u00020\u0015H\u0002R\u001c\u0010\u0003\u001a\u0010\u0012\f\u0012\n \u0006*\u0004\u0018\u00010\u00050\u00050\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bX\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\nX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u001b\u0010\u000b\u001a\u00020\f8BX\u0082\u0084\u0002\u00a2\u0006\f\n\u0004\b\u000f\u0010\u0010\u001a\u0004\b\r\u0010\u000e\u00a8\u0006,"}, d2 = {"Lcom/focuslock/ui/profile/ProfileEditorActivity;", "Landroidx/appcompat/app/AppCompatActivity;", "()V", "appPickerLauncher", "Landroidx/activity/result/ActivityResultLauncher;", "Landroid/content/Intent;", "kotlin.jvm.PlatformType", "binding", "Lcom/focuslock/databinding/ActivityProfileEditorBinding;", "profileId", "", "viewModel", "Lcom/focuslock/viewmodel/ProfileEditorViewModel;", "getViewModel", "()Lcom/focuslock/viewmodel/ProfileEditorViewModel;", "viewModel$delegate", "Lkotlin/Lazy;", "applyPreset", "", "daysToEnable", "", "", "bindScheduleDay", "dayBinding", "Lcom/focuslock/databinding/ItemScheduleDayBinding;", "day", "Lcom/focuslock/data/db/entities/ScheduleDay;", "onCreate", "savedInstanceState", "Landroid/os/Bundle;", "onSupportNavigateUp", "", "renderSchedule", "days", "setupListeners", "setupObservers", "showTimePicker", "hour", "minute", "onSet", "Lkotlin/Function2;", "updateOverrideDurationLabel", "minutes", "Companion", "app_debug"})
public final class ProfileEditorActivity extends androidx.appcompat.app.AppCompatActivity {
    private com.focuslock.databinding.ActivityProfileEditorBinding binding;
    @org.jetbrains.annotations.NotNull()
    private final kotlin.Lazy viewModel$delegate = null;
    private long profileId = -1L;
    @org.jetbrains.annotations.NotNull()
    private final androidx.activity.result.ActivityResultLauncher<android.content.Intent> appPickerLauncher = null;
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String EXTRA_PROFILE_ID = "extra_profile_id";
    @org.jetbrains.annotations.NotNull()
    public static final com.focuslock.ui.profile.ProfileEditorActivity.Companion Companion = null;
    
    public ProfileEditorActivity() {
        super();
    }
    
    private final com.focuslock.viewmodel.ProfileEditorViewModel getViewModel() {
        return null;
    }
    
    @java.lang.Override()
    protected void onCreate(@org.jetbrains.annotations.Nullable()
    android.os.Bundle savedInstanceState) {
    }
    
    @java.lang.Override()
    public boolean onSupportNavigateUp() {
        return false;
    }
    
    private final void setupObservers() {
    }
    
    private final void setupListeners() {
    }
    
    private final void updateOverrideDurationLabel(int minutes) {
    }
    
    private final void renderSchedule(java.util.List<com.focuslock.data.db.entities.ScheduleDay> days) {
    }
    
    private final void bindScheduleDay(com.focuslock.databinding.ItemScheduleDayBinding dayBinding, com.focuslock.data.db.entities.ScheduleDay day) {
    }
    
    private final void showTimePicker(int hour, int minute, kotlin.jvm.functions.Function2<? super java.lang.Integer, ? super java.lang.Integer, kotlin.Unit> onSet) {
    }
    
    private final void applyPreset(java.util.List<java.lang.Integer> daysToEnable) {
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0005"}, d2 = {"Lcom/focuslock/ui/profile/ProfileEditorActivity$Companion;", "", "()V", "EXTRA_PROFILE_ID", "", "app_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
    }
}