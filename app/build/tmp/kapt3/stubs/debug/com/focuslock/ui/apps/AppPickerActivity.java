package com.focuslock.ui.apps;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000D\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0007\u0018\u0000 \u001e2\u00020\u0001:\u0001\u001eB\u0005\u00a2\u0006\u0002\u0010\u0002J\u0012\u0010\u000f\u001a\u00020\u00102\b\u0010\u0011\u001a\u0004\u0018\u00010\u0012H\u0014J\u0010\u0010\u0013\u001a\u00020\b2\u0006\u0010\u0014\u001a\u00020\u0015H\u0016J\u0010\u0010\u0016\u001a\u00020\b2\u0006\u0010\u0017\u001a\u00020\u0018H\u0016J\b\u0010\u0019\u001a\u00020\bH\u0016J\b\u0010\u001a\u001a\u00020\u0010H\u0002J\b\u0010\u001b\u001a\u00020\u0010H\u0002J\b\u0010\u001c\u001a\u00020\u0010H\u0002J\b\u0010\u001d\u001a\u00020\u0010H\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u001b\u0010\t\u001a\u00020\n8BX\u0082\u0084\u0002\u00a2\u0006\f\n\u0004\b\r\u0010\u000e\u001a\u0004\b\u000b\u0010\f\u00a8\u0006\u001f"}, d2 = {"Lcom/focuslock/ui/apps/AppPickerActivity;", "Landroidx/appcompat/app/AppCompatActivity;", "()V", "adapter", "Lcom/focuslock/ui/apps/AppPickerAdapter;", "binding", "Lcom/focuslock/databinding/ActivityAppPickerBinding;", "showSystemApps", "", "viewModel", "Lcom/focuslock/viewmodel/AppPickerViewModel;", "getViewModel", "()Lcom/focuslock/viewmodel/AppPickerViewModel;", "viewModel$delegate", "Lkotlin/Lazy;", "onCreate", "", "savedInstanceState", "Landroid/os/Bundle;", "onCreateOptionsMenu", "menu", "Landroid/view/Menu;", "onOptionsItemSelected", "item", "Landroid/view/MenuItem;", "onSupportNavigateUp", "returnSelectedApps", "setupObservers", "setupRecyclerView", "updateDoneButton", "Companion", "app_debug"})
public final class AppPickerActivity extends androidx.appcompat.app.AppCompatActivity {
    private com.focuslock.databinding.ActivityAppPickerBinding binding;
    @org.jetbrains.annotations.NotNull()
    private final kotlin.Lazy viewModel$delegate = null;
    private com.focuslock.ui.apps.AppPickerAdapter adapter;
    private boolean showSystemApps = false;
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String EXTRA_SELECTED_PACKAGES = "extra_selected_packages";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String RESULT_SELECTED_PACKAGES = "result_selected_packages";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String RESULT_SELECTED_NAMES = "result_selected_names";
    @org.jetbrains.annotations.NotNull()
    public static final com.focuslock.ui.apps.AppPickerActivity.Companion Companion = null;
    
    public AppPickerActivity() {
        super();
    }
    
    private final com.focuslock.viewmodel.AppPickerViewModel getViewModel() {
        return null;
    }
    
    @java.lang.Override()
    protected void onCreate(@org.jetbrains.annotations.Nullable()
    android.os.Bundle savedInstanceState) {
    }
    
    @java.lang.Override()
    public boolean onCreateOptionsMenu(@org.jetbrains.annotations.NotNull()
    android.view.Menu menu) {
        return false;
    }
    
    @java.lang.Override()
    public boolean onOptionsItemSelected(@org.jetbrains.annotations.NotNull()
    android.view.MenuItem item) {
        return false;
    }
    
    @java.lang.Override()
    public boolean onSupportNavigateUp() {
        return false;
    }
    
    private final void setupRecyclerView() {
    }
    
    private final void setupObservers() {
    }
    
    private final void updateDoneButton() {
    }
    
    private final void returnSelectedApps() {
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0003\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0007"}, d2 = {"Lcom/focuslock/ui/apps/AppPickerActivity$Companion;", "", "()V", "EXTRA_SELECTED_PACKAGES", "", "RESULT_SELECTED_NAMES", "RESULT_SELECTED_PACKAGES", "app_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
    }
}