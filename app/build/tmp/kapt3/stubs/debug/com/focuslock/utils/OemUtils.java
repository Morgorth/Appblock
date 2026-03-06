package com.focuslock.utils;

/**
 * Utilities for detecting OEMs that aggressively kill background services
 * and providing deep-links to their battery whitelisting settings.
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00000\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\b\u0004\b\u00c6\u0002\u0018\u00002\u00020\u0001:\u0001\u0015B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u0010\u0010\r\u001a\u0004\u0018\u00010\u000e2\u0006\u0010\u000f\u001a\u00020\u0010J\u000e\u0010\u0011\u001a\b\u0012\u0004\u0012\u00020\u000e0\u0012H\u0002J\u0018\u0010\u0013\u001a\u00020\n2\u0006\u0010\u000f\u001a\u00020\u00102\u0006\u0010\u0014\u001a\u00020\u000eH\u0002R\u001b\u0010\u0003\u001a\u00020\u00048FX\u0086\u0084\u0002\u00a2\u0006\f\n\u0004\b\u0007\u0010\b\u001a\u0004\b\u0005\u0010\u0006R\u0011\u0010\t\u001a\u00020\n8F\u00a2\u0006\u0006\u001a\u0004\b\u000b\u0010\f\u00a8\u0006\u0016"}, d2 = {"Lcom/focuslock/utils/OemUtils;", "", "()V", "currentOem", "Lcom/focuslock/utils/OemUtils$Oem;", "getCurrentOem", "()Lcom/focuslock/utils/OemUtils$Oem;", "currentOem$delegate", "Lkotlin/Lazy;", "requiresBatteryWhitelist", "", "getRequiresBatteryWhitelist", "()Z", "getBatteryWhitelistIntent", "Landroid/content/Intent;", "context", "Landroid/content/Context;", "getOemIntents", "", "isIntentAvailable", "intent", "Oem", "app_debug"})
public final class OemUtils {
    @org.jetbrains.annotations.NotNull()
    private static final kotlin.Lazy currentOem$delegate = null;
    @org.jetbrains.annotations.NotNull()
    public static final com.focuslock.utils.OemUtils INSTANCE = null;
    
    private OemUtils() {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.focuslock.utils.OemUtils.Oem getCurrentOem() {
        return null;
    }
    
    public final boolean getRequiresBatteryWhitelist() {
        return false;
    }
    
    /**
     * Returns an intent for the OEM-specific battery settings screen, or null if unavailable.
     * Falls back to generic battery optimization request.
     */
    @org.jetbrains.annotations.Nullable()
    public final android.content.Intent getBatteryWhitelistIntent(@org.jetbrains.annotations.NotNull()
    android.content.Context context) {
        return null;
    }
    
    private final java.util.List<android.content.Intent> getOemIntents() {
        return null;
    }
    
    private final boolean isIntentAvailable(android.content.Context context, android.content.Intent intent) {
        return false;
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0010\u0010\n\u0002\b\t\b\u0086\u0081\u0002\u0018\u00002\b\u0012\u0004\u0012\u00020\u00000\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002j\u0002\b\u0003j\u0002\b\u0004j\u0002\b\u0005j\u0002\b\u0006j\u0002\b\u0007j\u0002\b\bj\u0002\b\t\u00a8\u0006\n"}, d2 = {"Lcom/focuslock/utils/OemUtils$Oem;", "", "(Ljava/lang/String;I)V", "XIAOMI", "HUAWEI", "SAMSUNG", "OPPO", "VIVO", "ONEPLUS", "OTHER", "app_debug"})
    public static enum Oem {
        /*public static final*/ XIAOMI /* = new XIAOMI() */,
        /*public static final*/ HUAWEI /* = new HUAWEI() */,
        /*public static final*/ SAMSUNG /* = new SAMSUNG() */,
        /*public static final*/ OPPO /* = new OPPO() */,
        /*public static final*/ VIVO /* = new VIVO() */,
        /*public static final*/ ONEPLUS /* = new ONEPLUS() */,
        /*public static final*/ OTHER /* = new OTHER() */;
        
        Oem() {
        }
        
        @org.jetbrains.annotations.NotNull()
        public static kotlin.enums.EnumEntries<com.focuslock.utils.OemUtils.Oem> getEntries() {
            return null;
        }
    }
}