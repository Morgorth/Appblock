package com.focuslock.data.repository;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000n\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\t\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\"\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\r\u0018\u00002\u00020\u0001B\u001d\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u00a2\u0006\u0002\u0010\bJ \u0010\u000f\u001a\u00020\u00102\u0006\u0010\u0011\u001a\u00020\u00122\b\b\u0002\u0010\u0013\u001a\u00020\u0014H\u0086@\u00a2\u0006\u0002\u0010\u0015J\u0016\u0010\u0016\u001a\u00020\u00172\u0006\u0010\u0018\u001a\u00020\fH\u0086@\u00a2\u0006\u0002\u0010\u0019J\u001c\u0010\u001a\u001a\b\u0012\u0004\u0012\u00020\u00120\u000b2\u0006\u0010\u001b\u001a\u00020\u0012H\u0086@\u00a2\u0006\u0002\u0010\u001cJ$\u0010\u001d\u001a\u0010\u0012\u0004\u0012\u00020\u0014\u0012\u0004\u0012\u00020\u0014\u0018\u00010\u001e2\u0006\u0010\u001b\u001a\u00020\u0012H\u0086@\u00a2\u0006\u0002\u0010\u001cJ\u001c\u0010\u001f\u001a\b\u0012\u0004\u0012\u00020 0\u000b2\u0006\u0010!\u001a\u00020\u0010H\u0086@\u00a2\u0006\u0002\u0010\"J\u0014\u0010#\u001a\b\u0012\u0004\u0012\u00020\u00120$H\u0086@\u00a2\u0006\u0002\u0010%J\u0018\u0010&\u001a\u0004\u0018\u00010\f2\u0006\u0010\'\u001a\u00020\u0010H\u0086@\u00a2\u0006\u0002\u0010\"J\u001c\u0010(\u001a\b\u0012\u0004\u0012\u00020)0\u000b2\u0006\u0010!\u001a\u00020\u0010H\u0086@\u00a2\u0006\u0002\u0010\"J \u0010*\u001a\u00020+2\u0006\u0010,\u001a\u00020\u00142\u0006\u0010-\u001a\u00020\u00142\u0006\u0010.\u001a\u00020)H\u0002J$\u0010/\u001a\u00020\u00172\u0006\u0010!\u001a\u00020\u00102\f\u00100\u001a\b\u0012\u0004\u0012\u00020 0\u000bH\u0086@\u00a2\u0006\u0002\u00101J$\u00102\u001a\u00020\u00172\u0006\u0010!\u001a\u00020\u00102\f\u00103\u001a\b\u0012\u0004\u0012\u00020)0\u000bH\u0086@\u00a2\u0006\u0002\u00101J\u001e\u00104\u001a\u00020\u00172\u0006\u0010\'\u001a\u00020\u00102\u0006\u00105\u001a\u00020+H\u0086@\u00a2\u0006\u0002\u00106J\u0016\u00107\u001a\u00020\u00172\u0006\u0010\u0018\u001a\u00020\fH\u0086@\u00a2\u0006\u0002\u0010\u0019R\u001d\u0010\t\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\f0\u000b0\n\u00a2\u0006\b\n\u0000\u001a\u0004\b\r\u0010\u000eR\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u00068"}, d2 = {"Lcom/focuslock/data/repository/ProfileRepository;", "", "profileDao", "Lcom/focuslock/data/db/dao/ProfileDao;", "blockedAppDao", "Lcom/focuslock/data/db/dao/BlockedAppDao;", "scheduleDayDao", "Lcom/focuslock/data/db/dao/ScheduleDayDao;", "(Lcom/focuslock/data/db/dao/ProfileDao;Lcom/focuslock/data/db/dao/BlockedAppDao;Lcom/focuslock/data/db/dao/ScheduleDayDao;)V", "allProfiles", "Landroidx/lifecycle/LiveData;", "", "Lcom/focuslock/data/db/entities/Profile;", "getAllProfiles", "()Landroidx/lifecycle/LiveData;", "createProfile", "", "name", "", "overrideDurationMinutes", "", "(Ljava/lang/String;ILkotlin/coroutines/Continuation;)Ljava/lang/Object;", "deleteProfile", "", "profile", "(Lcom/focuslock/data/db/entities/Profile;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getActiveProfilesBlockingApp", "packageName", "(Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getBlockEndTime", "Lkotlin/Pair;", "getBlockedApps", "Lcom/focuslock/data/db/entities/BlockedApp;", "profileId", "(JLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getCurrentlyBlockedPackages", "", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getProfileById", "id", "getSchedule", "Lcom/focuslock/data/db/entities/ScheduleDay;", "isTimeInWindow", "", "hour", "minute", "schedule", "saveBlockedApps", "apps", "(JLjava/util/List;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "saveSchedule", "days", "setProfileEnabled", "isEnabled", "(JZLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "updateProfile", "app_debug"})
public final class ProfileRepository {
    @org.jetbrains.annotations.NotNull()
    private final com.focuslock.data.db.dao.ProfileDao profileDao = null;
    @org.jetbrains.annotations.NotNull()
    private final com.focuslock.data.db.dao.BlockedAppDao blockedAppDao = null;
    @org.jetbrains.annotations.NotNull()
    private final com.focuslock.data.db.dao.ScheduleDayDao scheduleDayDao = null;
    @org.jetbrains.annotations.NotNull()
    private final androidx.lifecycle.LiveData<java.util.List<com.focuslock.data.db.entities.Profile>> allProfiles = null;
    
    public ProfileRepository(@org.jetbrains.annotations.NotNull()
    com.focuslock.data.db.dao.ProfileDao profileDao, @org.jetbrains.annotations.NotNull()
    com.focuslock.data.db.dao.BlockedAppDao blockedAppDao, @org.jetbrains.annotations.NotNull()
    com.focuslock.data.db.dao.ScheduleDayDao scheduleDayDao) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.lifecycle.LiveData<java.util.List<com.focuslock.data.db.entities.Profile>> getAllProfiles() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object getProfileById(long id, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.focuslock.data.db.entities.Profile> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object createProfile(@org.jetbrains.annotations.NotNull()
    java.lang.String name, int overrideDurationMinutes, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.Long> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object updateProfile(@org.jetbrains.annotations.NotNull()
    com.focuslock.data.db.entities.Profile profile, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object deleteProfile(@org.jetbrains.annotations.NotNull()
    com.focuslock.data.db.entities.Profile profile, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object setProfileEnabled(long id, boolean isEnabled, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object getBlockedApps(long profileId, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<com.focuslock.data.db.entities.BlockedApp>> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object saveBlockedApps(long profileId, @org.jetbrains.annotations.NotNull()
    java.util.List<com.focuslock.data.db.entities.BlockedApp> apps, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object getSchedule(long profileId, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<com.focuslock.data.db.entities.ScheduleDay>> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object saveSchedule(long profileId, @org.jetbrains.annotations.NotNull()
    java.util.List<com.focuslock.data.db.entities.ScheduleDay> days, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    /**
     * Returns the set of package names that are currently blocked at this moment
     * (across all enabled profiles whose schedule is currently active).
     */
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object getCurrentlyBlockedPackages(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.Set<java.lang.String>> $completion) {
        return null;
    }
    
    private final boolean isTimeInWindow(int hour, int minute, com.focuslock.data.db.entities.ScheduleDay schedule) {
        return false;
    }
    
    /**
     * Returns profile name(s) causing the block for a given package at the current time.
     */
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object getActiveProfilesBlockingApp(@org.jetbrains.annotations.NotNull()
    java.lang.String packageName, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<java.lang.String>> $completion) {
        return null;
    }
    
    /**
     * Returns the end time (hour, minute) of the blocking window for a given package.
     * Returns null if no active block.
     */
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object getBlockEndTime(@org.jetbrains.annotations.NotNull()
    java.lang.String packageName, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Pair<java.lang.Integer, java.lang.Integer>> $completion) {
        return null;
    }
}