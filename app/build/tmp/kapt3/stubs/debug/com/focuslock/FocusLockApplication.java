package com.focuslock;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000*\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u0002\n\u0000\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\b\u0010\u0013\u001a\u00020\u0014H\u0016R\u001b\u0010\u0003\u001a\u00020\u00048BX\u0082\u0084\u0002\u00a2\u0006\f\n\u0004\b\u0007\u0010\b\u001a\u0004\b\u0005\u0010\u0006R\u001b\u0010\t\u001a\u00020\n8FX\u0086\u0084\u0002\u00a2\u0006\f\n\u0004\b\r\u0010\b\u001a\u0004\b\u000b\u0010\fR\u001b\u0010\u000e\u001a\u00020\u000f8FX\u0086\u0084\u0002\u00a2\u0006\f\n\u0004\b\u0012\u0010\b\u001a\u0004\b\u0010\u0010\u0011\u00a8\u0006\u0015"}, d2 = {"Lcom/focuslock/FocusLockApplication;", "Landroid/app/Application;", "()V", "database", "Lcom/focuslock/data/db/AppDatabase;", "getDatabase", "()Lcom/focuslock/data/db/AppDatabase;", "database$delegate", "Lkotlin/Lazy;", "overrideLogRepository", "Lcom/focuslock/data/repository/OverrideLogRepository;", "getOverrideLogRepository", "()Lcom/focuslock/data/repository/OverrideLogRepository;", "overrideLogRepository$delegate", "profileRepository", "Lcom/focuslock/data/repository/ProfileRepository;", "getProfileRepository", "()Lcom/focuslock/data/repository/ProfileRepository;", "profileRepository$delegate", "onCreate", "", "app_debug"})
public final class FocusLockApplication extends android.app.Application {
    @org.jetbrains.annotations.NotNull()
    private final kotlin.Lazy database$delegate = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlin.Lazy profileRepository$delegate = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlin.Lazy overrideLogRepository$delegate = null;
    
    public FocusLockApplication() {
        super();
    }
    
    private final com.focuslock.data.db.AppDatabase getDatabase() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.focuslock.data.repository.ProfileRepository getProfileRepository() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.focuslock.data.repository.OverrideLogRepository getOverrideLogRepository() {
        return null;
    }
    
    @java.lang.Override()
    public void onCreate() {
    }
}