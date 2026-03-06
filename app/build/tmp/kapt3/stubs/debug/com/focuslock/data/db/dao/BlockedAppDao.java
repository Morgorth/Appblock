package com.focuslock.data.db.dao;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000.\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\t\n\u0002\b\u0002\n\u0002\u0010 \n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0006\bg\u0018\u00002\u00020\u0001J\u0016\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\u00a7@\u00a2\u0006\u0002\u0010\u0006J\u0016\u0010\u0007\u001a\u00020\u00032\u0006\u0010\b\u001a\u00020\tH\u00a7@\u00a2\u0006\u0002\u0010\nJ\u001c\u0010\u000b\u001a\b\u0012\u0004\u0012\u00020\u00050\f2\u0006\u0010\b\u001a\u00020\tH\u00a7@\u00a2\u0006\u0002\u0010\nJ\"\u0010\r\u001a\b\u0012\u0004\u0012\u00020\u000e0\f2\f\u0010\u000f\u001a\b\u0012\u0004\u0012\u00020\t0\fH\u00a7@\u00a2\u0006\u0002\u0010\u0010J\u0016\u0010\u0011\u001a\u00020\t2\u0006\u0010\u0004\u001a\u00020\u0005H\u00a7@\u00a2\u0006\u0002\u0010\u0006J\u001c\u0010\u0012\u001a\u00020\u00032\f\u0010\u0013\u001a\b\u0012\u0004\u0012\u00020\u00050\fH\u00a7@\u00a2\u0006\u0002\u0010\u0010\u00a8\u0006\u0014"}, d2 = {"Lcom/focuslock/data/db/dao/BlockedAppDao;", "", "delete", "", "blockedApp", "Lcom/focuslock/data/db/entities/BlockedApp;", "(Lcom/focuslock/data/db/entities/BlockedApp;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "deleteAllForProfile", "profileId", "", "(JLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getBlockedAppsForProfile", "", "getBlockedPackagesForProfiles", "", "profileIds", "(Ljava/util/List;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "insert", "insertAll", "apps", "app_debug"})
@androidx.room.Dao()
public abstract interface BlockedAppDao {
    
    @androidx.room.Query(value = "SELECT * FROM blocked_apps WHERE profileId = :profileId")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getBlockedAppsForProfile(long profileId, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<com.focuslock.data.db.entities.BlockedApp>> $completion);
    
    @androidx.room.Query(value = "SELECT DISTINCT packageName FROM blocked_apps WHERE profileId IN (:profileIds)")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getBlockedPackagesForProfiles(@org.jetbrains.annotations.NotNull()
    java.util.List<java.lang.Long> profileIds, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<java.lang.String>> $completion);
    
    @androidx.room.Insert(onConflict = 1)
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object insert(@org.jetbrains.annotations.NotNull()
    com.focuslock.data.db.entities.BlockedApp blockedApp, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.Long> $completion);
    
    @androidx.room.Insert(onConflict = 1)
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object insertAll(@org.jetbrains.annotations.NotNull()
    java.util.List<com.focuslock.data.db.entities.BlockedApp> apps, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Delete()
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object delete(@org.jetbrains.annotations.NotNull()
    com.focuslock.data.db.entities.BlockedApp blockedApp, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Query(value = "DELETE FROM blocked_apps WHERE profileId = :profileId")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object deleteAllForProfile(long profileId, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
}