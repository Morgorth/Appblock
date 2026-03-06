package com.focuslock.ui.history;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00006\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0002\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\b\u0010\r\u001a\u00020\u000eH\u0002J\u0012\u0010\u000f\u001a\u00020\u000e2\b\u0010\u0010\u001a\u0004\u0018\u00010\u0011H\u0014J\b\u0010\u0012\u001a\u00020\u0013H\u0016J\b\u0010\u0014\u001a\u00020\u000eH\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082.\u00a2\u0006\u0002\n\u0000R\u001b\u0010\u0007\u001a\u00020\b8BX\u0082\u0084\u0002\u00a2\u0006\f\n\u0004\b\u000b\u0010\f\u001a\u0004\b\t\u0010\n\u00a8\u0006\u0015"}, d2 = {"Lcom/focuslock/ui/history/OverrideHistoryActivity;", "Landroidx/appcompat/app/AppCompatActivity;", "()V", "binding", "Lcom/focuslock/databinding/ActivityOverrideHistoryBinding;", "historyAdapter", "Lcom/focuslock/ui/history/OverrideHistoryAdapter;", "viewModel", "Lcom/focuslock/viewmodel/OverrideHistoryViewModel;", "getViewModel", "()Lcom/focuslock/viewmodel/OverrideHistoryViewModel;", "viewModel$delegate", "Lkotlin/Lazy;", "observeLogs", "", "onCreate", "savedInstanceState", "Landroid/os/Bundle;", "onSupportNavigateUp", "", "setupRecyclerView", "app_debug"})
public final class OverrideHistoryActivity extends androidx.appcompat.app.AppCompatActivity {
    private com.focuslock.databinding.ActivityOverrideHistoryBinding binding;
    @org.jetbrains.annotations.NotNull()
    private final kotlin.Lazy viewModel$delegate = null;
    private com.focuslock.ui.history.OverrideHistoryAdapter historyAdapter;
    
    public OverrideHistoryActivity() {
        super();
    }
    
    private final com.focuslock.viewmodel.OverrideHistoryViewModel getViewModel() {
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
    
    private final void setupRecyclerView() {
    }
    
    private final void observeLogs() {
    }
}