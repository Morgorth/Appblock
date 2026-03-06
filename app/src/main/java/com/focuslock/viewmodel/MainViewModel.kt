package com.focuslock.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.focuslock.FocusLockApplication
import com.focuslock.data.db.entities.Profile
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = (application as FocusLockApplication).profileRepository

    val profiles = repository.allProfiles

    fun toggleProfile(profile: Profile, isEnabled: Boolean) {
        viewModelScope.launch {
            repository.setProfileEnabled(profile.id, isEnabled)
        }
    }

    fun deleteProfile(profile: Profile) {
        viewModelScope.launch {
            repository.deleteProfile(profile)
        }
    }
}
