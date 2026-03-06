package com.focuslock.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.focuslock.FocusLockApplication
import kotlinx.coroutines.launch

class OverrideHistoryViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = (application as FocusLockApplication).overrideLogRepository

    val logs = repository.allLogs

    fun clearHistory() {
        viewModelScope.launch {
            repository.clearAll()
        }
    }
}
