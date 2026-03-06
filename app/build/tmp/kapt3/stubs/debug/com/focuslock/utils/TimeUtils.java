package com.focuslock.utils;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00002\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0010\t\n\u0002\b\u0005\n\u0002\u0010 \n\u0002\b\u0005\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u000e\u0010\u0006\u001a\u00020\u00072\u0006\u0010\b\u001a\u00020\tJ\u000e\u0010\n\u001a\u00020\u00072\u0006\u0010\b\u001a\u00020\tJ\u000e\u0010\u000b\u001a\u00020\u00072\u0006\u0010\f\u001a\u00020\rJ\u0016\u0010\u000e\u001a\u00020\u00072\u0006\u0010\u000f\u001a\u00020\t2\u0006\u0010\u0010\u001a\u00020\tJ4\u0010\u0011\u001a\u00020\u00072\f\u0010\u0012\u001a\b\u0012\u0004\u0012\u00020\t0\u00132\u0006\u0010\u0014\u001a\u00020\t2\u0006\u0010\u0015\u001a\u00020\t2\u0006\u0010\u0016\u001a\u00020\t2\u0006\u0010\u0017\u001a\u00020\tR\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0018"}, d2 = {"Lcom/focuslock/utils/TimeUtils;", "", "()V", "dateTimeFormat", "Ljava/text/SimpleDateFormat;", "timeFormat", "dayName", "", "dayOfWeek", "", "dayNameShort", "formatDateTime", "epochMs", "", "formatTime", "hour", "minute", "scheduleSummary", "enabledDays", "", "startHour", "startMinute", "endHour", "endMinute", "app_debug"})
public final class TimeUtils {
    @org.jetbrains.annotations.NotNull()
    private static final java.text.SimpleDateFormat timeFormat = null;
    @org.jetbrains.annotations.NotNull()
    private static final java.text.SimpleDateFormat dateTimeFormat = null;
    @org.jetbrains.annotations.NotNull()
    public static final com.focuslock.utils.TimeUtils INSTANCE = null;
    
    private TimeUtils() {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String formatTime(int hour, int minute) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String formatDateTime(long epochMs) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String dayName(int dayOfWeek) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String dayNameShort(int dayOfWeek) {
        return null;
    }
    
    /**
     * Returns a human-readable schedule summary, e.g. "Mon–Fri, 09:00–17:00"
     */
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String scheduleSummary(@org.jetbrains.annotations.NotNull()
    java.util.List<java.lang.Integer> enabledDays, int startHour, int startMinute, int endHour, int endMinute) {
        return null;
    }
}