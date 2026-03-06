package com.focuslock.viewmodel;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000b\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\t\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\b\n\u0002\b\u0005\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u000e\u0010!\u001a\u00020\"2\u0006\u0010#\u001a\u00020\u0015J\u0016\u0010$\u001a\u00020\"2\u0006\u0010%\u001a\u00020&2\u0006\u0010\'\u001a\u00020(J\u0014\u0010)\u001a\u00020\"2\f\u0010*\u001a\b\u0012\u0004\u0012\u00020\b0\u0007J\u000e\u0010+\u001a\u00020\"2\u0006\u0010,\u001a\u00020\u000fR\u001a\u0010\u0005\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\b0\u00070\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0016\u0010\t\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\n0\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001c\u0010\u000b\u001a\u0010\u0012\f\u0012\n \r*\u0004\u0018\u00010\f0\f0\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001a\u0010\u000e\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u000f0\u00070\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001d\u0010\u0010\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\b0\u00070\u0011\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0012\u0010\u0013R\u001e\u0010\u0016\u001a\u00020\u00152\u0006\u0010\u0014\u001a\u00020\u0015@BX\u0086\u000e\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0017\u0010\u0018R\u0019\u0010\u0019\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\n0\u0011\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001a\u0010\u0013R\u000e\u0010\u001b\u001a\u00020\u001cX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0017\u0010\u001d\u001a\b\u0012\u0004\u0012\u00020\f0\u0011\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001e\u0010\u0013R\u001d\u0010\u001f\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u000f0\u00070\u0011\u00a2\u0006\b\n\u0000\u001a\u0004\b \u0010\u0013\u00a8\u0006-"}, d2 = {"Lcom/focuslock/viewmodel/ProfileEditorViewModel;", "Landroidx/lifecycle/AndroidViewModel;", "application", "Landroid/app/Application;", "(Landroid/app/Application;)V", "_blockedApps", "Landroidx/lifecycle/MutableLiveData;", "", "Lcom/focuslock/data/db/entities/BlockedApp;", "_profile", "Lcom/focuslock/data/db/entities/Profile;", "_saveComplete", "", "kotlin.jvm.PlatformType", "_scheduleDays", "Lcom/focuslock/data/db/entities/ScheduleDay;", "blockedApps", "Landroidx/lifecycle/LiveData;", "getBlockedApps", "()Landroidx/lifecycle/LiveData;", "<set-?>", "", "currentProfileId", "getCurrentProfileId", "()J", "profile", "getProfile", "repository", "Lcom/focuslock/data/repository/ProfileRepository;", "saveComplete", "getSaveComplete", "scheduleDays", "getScheduleDays", "loadProfile", "", "profileId", "saveProfile", "name", "", "overrideDuration", "", "setBlockedApps", "apps", "updateScheduleDay", "updated", "app_debug"})
public final class ProfileEditorViewModel extends androidx.lifecycle.AndroidViewModel {
    @org.jetbrains.annotations.NotNull()
    private final com.focuslock.data.repository.ProfileRepository repository = null;
    @org.jetbrains.annotations.NotNull()
    private final androidx.lifecycle.MutableLiveData<com.focuslock.data.db.entities.Profile> _profile = null;
    @org.jetbrains.annotations.NotNull()
    private final androidx.lifecycle.LiveData<com.focuslock.data.db.entities.Profile> profile = null;
    @org.jetbrains.annotations.NotNull()
    private final androidx.lifecycle.MutableLiveData<java.util.List<com.focuslock.data.db.entities.BlockedApp>> _blockedApps = null;
    @org.jetbrains.annotations.NotNull()
    private final androidx.lifecycle.LiveData<java.util.List<com.focuslock.data.db.entities.BlockedApp>> blockedApps = null;
    @org.jetbrains.annotations.NotNull()
    private final androidx.lifecycle.MutableLiveData<java.util.List<com.focuslock.data.db.entities.ScheduleDay>> _scheduleDays = null;
    @org.jetbrains.annotations.NotNull()
    private final androidx.lifecycle.LiveData<java.util.List<com.focuslock.data.db.entities.ScheduleDay>> scheduleDays = null;
    @org.jetbrains.annotations.NotNull()
    private final androidx.lifecycle.MutableLiveData<java.lang.Boolean> _saveComplete = null;
    @org.jetbrains.annotations.NotNull()
    private final androidx.lifecycle.LiveData<java.lang.Boolean> saveComplete = null;
    private long currentProfileId = -1L;
    
    public ProfileEditorViewModel(@org.jetbrains.annotations.NotNull()
    android.app.Application application) {
        super(null);
    }
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.lifecycle.LiveData<com.focuslock.data.db.entities.Profile> getProfile() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.lifecycle.LiveData<java.util.List<com.focuslock.data.db.entities.BlockedApp>> getBlockedApps() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.lifecycle.LiveData<java.util.List<com.focuslock.data.db.entities.ScheduleDay>> getScheduleDays() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.lifecycle.LiveData<java.lang.Boolean> getSaveComplete() {
        return null;
    }
    
    public final long getCurrentProfileId() {
        return 0L;
    }
    
    public final void loadProfile(long profileId) {
    }
    
    public final void updateScheduleDay(@org.jetbrains.annotations.NotNull()
    com.focuslock.data.db.entities.ScheduleDay updated) {
    }
    
    public final void setBlockedApps(@org.jetbrains.annotations.NotNull()
    java.util.List<com.focuslock.data.db.entities.BlockedApp> apps) {
    }
    
    public final void saveProfile(@org.jetbrains.annotations.NotNull()
    java.lang.String name, int overrideDuration) {
    }
}