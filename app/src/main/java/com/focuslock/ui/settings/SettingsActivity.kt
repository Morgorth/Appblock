package com.focuslock.ui.settings

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.focuslock.R
import com.focuslock.databinding.ActivitySettingsBinding
import com.focuslock.ui.history.OverrideHistoryActivity
import com.focuslock.ui.onboarding.OnboardingActivity
import com.focuslock.utils.PermissionUtils
import com.focuslock.utils.PreferenceManager

class SettingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingsBinding
    private lateinit var prefManager: PreferenceManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            title = getString(R.string.settings)
        }

        prefManager = PreferenceManager(this)
        setupUi()
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    override fun onResume() {
        super.onResume()
        updatePermissionStatuses()
    }

    private fun setupUi() {
        // Override duration slider
        val currentDuration = prefManager.defaultOverrideDurationMinutes
        binding.sliderOverrideDuration.value = currentDuration.toFloat()
        updateOverrideDurationLabel(currentDuration)

        binding.sliderOverrideDuration.addOnChangeListener { _, value, _ ->
            val mins = value.toInt()
            prefManager.defaultOverrideDurationMinutes = mins
            updateOverrideDurationLabel(mins)
        }

        // Override history
        binding.rowOverrideHistory.setOnClickListener {
            startActivity(Intent(this, OverrideHistoryActivity::class.java))
        }

        // Re-trigger permissions
        binding.rowPermissions.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle(R.string.recheck_permissions_title)
                .setMessage(R.string.recheck_permissions_message)
                .setPositiveButton(R.string.open_settings) { _, _ ->
                    prefManager.onboardingComplete = false
                    val intent = Intent(this, OnboardingActivity::class.java)
                    startActivity(intent)
                    finish()
                }
                .setNegativeButton(R.string.cancel, null)
                .show()
        }

        updatePermissionStatuses()
    }

    private fun updateOverrideDurationLabel(minutes: Int) {
        binding.tvOverrideDurationValue.text =
            resources.getQuantityString(R.plurals.minutes_count, minutes, minutes)
    }

    private fun updatePermissionStatuses() {
        val usageOk = PermissionUtils.hasUsageStatsPermission(this)
        val overlayOk = PermissionUtils.hasOverlayPermission(this)
        val accessibilityOk = PermissionUtils.hasAccessibilityPermission(this)

        binding.statusUsageStats.setImageResource(
            if (usageOk) R.drawable.ic_check_circle else R.drawable.ic_warning
        )
        binding.statusOverlay.setImageResource(
            if (overlayOk) R.drawable.ic_check_circle else R.drawable.ic_warning
        )
        binding.statusAccessibility.setImageResource(
            if (accessibilityOk) R.drawable.ic_check_circle else R.drawable.ic_info
        )
    }
}
