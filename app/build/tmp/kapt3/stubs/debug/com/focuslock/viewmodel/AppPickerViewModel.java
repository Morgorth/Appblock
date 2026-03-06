package com.focuslock.viewmodel;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000Z\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0002\b\u0002\n\u0002\u0010\"\n\u0002\b\u0006\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u000e\u0010\u0015\u001a\u00020\u00162\u0006\u0010\u0017\u001a\u00020\u0018J\u0014\u0010\u0019\u001a\b\u0012\u0004\u0012\u00020\u001a0\u00072\u0006\u0010\u001b\u001a\u00020\u001cJ\u0014\u0010\u001d\u001a\u00020\u00162\f\u0010\u001e\u001a\b\u0012\u0004\u0012\u00020\u00180\u001fJ\u000e\u0010 \u001a\u00020\u00162\u0006\u0010!\u001a\u00020\u0018J\u001c\u0010\"\u001a\u00020\u00162\u0006\u0010#\u001a\u00020\n2\f\u0010$\u001a\b\u0012\u0004\u0012\u00020\u00180\u001fR\u001a\u0010\u0005\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\b0\u00070\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001c\u0010\t\u001a\u0010\u0012\f\u0012\n \u000b*\u0004\u0018\u00010\n0\n0\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\f\u001a\b\u0012\u0004\u0012\u00020\b0\u0007X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u001d\u0010\r\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\b0\u00070\u000e\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000f\u0010\u0010R\u0017\u0010\u0011\u001a\b\u0012\u0004\u0012\u00020\n0\u000e\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0011\u0010\u0010R\u000e\u0010\u0012\u001a\u00020\u0013X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0014\u001a\u00020\nX\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006%"}, d2 = {"Lcom/focuslock/viewmodel/AppPickerViewModel;", "Landroidx/lifecycle/AndroidViewModel;", "application", "Landroid/app/Application;", "(Landroid/app/Application;)V", "_apps", "Landroidx/lifecycle/MutableLiveData;", "", "Lcom/focuslock/viewmodel/AppItem;", "_isLoading", "", "kotlin.jvm.PlatformType", "allApps", "apps", "Landroidx/lifecycle/LiveData;", "getApps", "()Landroidx/lifecycle/LiveData;", "isLoading", "pm", "Landroid/content/pm/PackageManager;", "showSystemApps", "filter", "", "query", "", "getSelectedApps", "Lcom/focuslock/data/db/entities/BlockedApp;", "profileId", "", "loadApps", "alreadyBlockedPackages", "", "toggleSelection", "packageName", "toggleSystemApps", "show", "alreadyBlocked", "app_debug"})
public final class AppPickerViewModel extends androidx.lifecycle.AndroidViewModel {
    @org.jetbrains.annotations.NotNull()
    private final android.content.pm.PackageManager pm = null;
    @org.jetbrains.annotations.NotNull()
    private final androidx.lifecycle.MutableLiveData<java.util.List<com.focuslock.viewmodel.AppItem>> _apps = null;
    @org.jetbrains.annotations.NotNull()
    private final androidx.lifecycle.LiveData<java.util.List<com.focuslock.viewmodel.AppItem>> apps = null;
    @org.jetbrains.annotations.NotNull()
    private final androidx.lifecycle.MutableLiveData<java.lang.Boolean> _isLoading = null;
    @org.jetbrains.annotations.NotNull()
    private final androidx.lifecycle.LiveData<java.lang.Boolean> isLoading = null;
    @org.jetbrains.annotations.NotNull()
    private java.util.List<com.focuslock.viewmodel.AppItem> allApps;
    private boolean showSystemApps = false;
    
    public AppPickerViewModel(@org.jetbrains.annotations.NotNull()
    android.app.Application application) {
        super(null);
    }
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.lifecycle.LiveData<java.util.List<com.focuslock.viewmodel.AppItem>> getApps() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.lifecycle.LiveData<java.lang.Boolean> isLoading() {
        return null;
    }
    
    public final void loadApps(@org.jetbrains.annotations.NotNull()
    java.util.Set<java.lang.String> alreadyBlockedPackages) {
    }
    
    public final void filter(@org.jetbrains.annotations.NotNull()
    java.lang.String query) {
    }
    
    public final void toggleSelection(@org.jetbrains.annotations.NotNull()
    java.lang.String packageName) {
    }
    
    public final void toggleSystemApps(boolean show, @org.jetbrains.annotations.NotNull()
    java.util.Set<java.lang.String> alreadyBlocked) {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.focuslock.data.db.entities.BlockedApp> getSelectedApps(long profileId) {
        return null;
    }
}