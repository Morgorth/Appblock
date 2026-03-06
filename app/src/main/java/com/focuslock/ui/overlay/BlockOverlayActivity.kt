package com.focuslock.ui.overlay

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.focuslock.FocusLockApplication
import com.focuslock.R
import com.focuslock.databinding.ActivityBlockOverlayBinding
import com.focuslock.services.BlockerService
import com.focuslock.utils.PreferenceManager
import com.focuslock.utils.TimeUtils
import kotlinx.coroutines.launch

/**
 * Full-screen overlay activity that appears on top of blocked apps.
 *
 * Shows:
 *  - Blocked app name and icon
 *  - Which profile is causing the block
 *  - When the block ends
 *  - "Go Back" button (sends user home)
 *  - "Emergency Override" button with friction (20-char minimum justification)
 */
class BlockOverlayActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBlockOverlayBinding
    private lateinit var prefManager: PreferenceManager

    private var blockedPackageName = ""
    private var blockedAppName = ""
    private var activeProfileName = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Show over lock screen
        window.addFlags(
            WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED or
                    WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD or
                    WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
        )

        binding = ActivityBlockOverlayBinding.inflate(layoutInflater)
        setContentView(binding.root)

        prefManager = PreferenceManager(this)
        blockedPackageName = intent.getStringExtra(EXTRA_PACKAGE_NAME) ?: ""

        if (blockedPackageName.isEmpty()) {
            finish()
            return
        }

        loadBlockInfo()
        setupButtons()
        setupOverrideField()
    }

    override fun onBackPressed() {
        // Override back button — send user home instead of going back to blocked app
        goHome()
    }

    private fun loadBlockInfo() {
        val pm = packageManager
        try {
            val appInfo = pm.getApplicationInfo(blockedPackageName, 0)
            blockedAppName = pm.getApplicationLabel(appInfo).toString()
            binding.ivBlockedAppIcon.setImageDrawable(pm.getApplicationIcon(appInfo))
        } catch (e: PackageManager.NameNotFoundException) {
            blockedAppName = blockedPackageName
            binding.ivBlockedAppIcon.setImageResource(android.R.drawable.sym_def_app_icon)
        }
        binding.tvBlockedAppName.text = blockedAppName

        lifecycleScope.launch {
            val app = application as FocusLockApplication
            val profileNames = app.profileRepository.getActiveProfilesBlockingApp(blockedPackageName)
            activeProfileName = profileNames.firstOrNull() ?: getString(R.string.unknown_profile)
            binding.tvProfileName.text = getString(R.string.blocked_by_profile, activeProfileName)

            val endTime = app.profileRepository.getBlockEndTime(blockedPackageName)
            if (endTime != null) {
                binding.tvBlockEndTime.text = getString(
                    R.string.block_ends_at,
                    TimeUtils.formatTime(endTime.first, endTime.second)
                )
                binding.tvBlockEndTime.visibility = View.VISIBLE
            } else {
                binding.tvBlockEndTime.visibility = View.GONE
            }
        }
    }

    private fun setupButtons() {
        binding.btnGoBack.setOnClickListener { goHome() }

        binding.btnEmergencyOverride.setOnClickListener {
            val isShowing = binding.overrideContainer.visibility == View.VISIBLE
            binding.overrideContainer.visibility = if (isShowing) View.GONE else View.VISIBLE
            if (!isShowing) {
                binding.etJustification.requestFocus()
            }
        }
    }

    private fun setupOverrideField() {
        binding.etJustification.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                val length = s?.length ?: 0
                binding.btnConfirmOverride.isEnabled = length >= MIN_JUSTIFICATION_LENGTH
                binding.tvJustificationHint.text = if (length < MIN_JUSTIFICATION_LENGTH) {
                    getString(R.string.justification_chars_remaining, MIN_JUSTIFICATION_LENGTH - length)
                } else {
                    ""
                }
            }
        })

        binding.btnConfirmOverride.isEnabled = false
        binding.btnConfirmOverride.setOnClickListener {
            confirmOverride()
        }
    }

    private fun confirmOverride() {
        val justification = binding.etJustification.text?.toString() ?: return
        val durationMinutes = prefManager.defaultOverrideDurationMinutes

        // Log the override
        lifecycleScope.launch {
            val app = application as FocusLockApplication
            app.overrideLogRepository.logOverride(
                packageName = blockedPackageName,
                appName = blockedAppName,
                profileName = activeProfileName,
                justification = justification,
                durationMinutes = durationMinutes
            )
        }

        // Tell the service to start override countdown
        BlockerService.startOverride(this, blockedPackageName, blockedAppName, durationMinutes)

        // Launch the originally-blocked app
        val launchIntent = packageManager.getLaunchIntentForPackage(blockedPackageName)
        if (launchIntent != null) {
            startActivity(launchIntent)
        }
        finish()
    }

    private fun goHome() {
        val homeIntent = Intent(Intent.ACTION_MAIN).apply {
            addCategory(Intent.CATEGORY_HOME)
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        startActivity(homeIntent)
        finish()
    }

    companion object {
        const val EXTRA_PACKAGE_NAME = "extra_package_name"
        private const val MIN_JUSTIFICATION_LENGTH = 20
    }
}
