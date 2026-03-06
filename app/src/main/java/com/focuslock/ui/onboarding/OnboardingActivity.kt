package com.focuslock.ui.onboarding

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.focuslock.R
import com.focuslock.databinding.ActivityOnboardingBinding
import com.focuslock.services.BlockerService
import com.focuslock.services.BlockerWatchdogWorker
import com.focuslock.ui.main.MainActivity
import com.focuslock.utils.OemUtils
import com.focuslock.utils.PermissionUtils
import com.focuslock.utils.PreferenceManager

/**
 * Multi-step onboarding that guides the user through granting the required permissions:
 *  Step 0: Usage Stats Access
 *  Step 1: Draw Over Apps (System Alert Window)
 *  Step 2: Accessibility Service (optional but recommended)
 *  Step 3: OEM battery whitelist (shown only on problematic OEMs)
 */
class OnboardingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOnboardingBinding
    private lateinit var prefManager: PreferenceManager

    private var currentStep = 0

    // Steps: 0=usage stats, 1=overlay, 2=accessibility, 3=battery (conditional)
    private val steps = mutableListOf(STEP_USAGE_STATS, STEP_OVERLAY, STEP_ACCESSIBILITY)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        prefManager = PreferenceManager(this)

        // If onboarding already done, go straight to main
        if (prefManager.onboardingComplete) {
            goToMain()
            return
        }

        binding = ActivityOnboardingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (OemUtils.requiresBatteryWhitelist) {
            steps.add(STEP_BATTERY)
        }

        showStep(0)
        setupListeners()
    }

    override fun onResume() {
        super.onResume()
        // Re-evaluate current step in case user granted permission and came back
        updateStepUi()
    }

    private fun setupListeners() {
        binding.btnPrimary.setOnClickListener {
            handlePrimaryAction()
        }
        binding.btnSkip.setOnClickListener {
            advanceStep()
        }
        binding.btnNext.setOnClickListener {
            advanceStep()
        }
    }

    private fun handlePrimaryAction() {
        when (steps.getOrNull(currentStep)) {
            STEP_USAGE_STATS -> startActivity(PermissionUtils.getUsageStatsIntent())
            STEP_OVERLAY -> startActivity(PermissionUtils.getOverlayPermissionIntent(this))
            STEP_ACCESSIBILITY -> startActivity(PermissionUtils.getAccessibilitySettingsIntent())
            STEP_BATTERY -> {
                val intent = OemUtils.getBatteryWhitelistIntent(this)
                if (intent != null) startActivity(intent)
                else advanceStep()
            }
        }
    }

    private fun showStep(step: Int) {
        currentStep = step
        updateStepUi()
    }

    private fun updateStepUi() {
        val stepType = steps.getOrNull(currentStep) ?: run {
            completeOnboarding()
            return
        }

        val isGranted = when (stepType) {
            STEP_USAGE_STATS -> PermissionUtils.hasUsageStatsPermission(this)
            STEP_OVERLAY -> PermissionUtils.hasOverlayPermission(this)
            STEP_ACCESSIBILITY -> PermissionUtils.hasAccessibilityPermission(this)
            STEP_BATTERY -> false // Can't detect, always show
            else -> false
        }

        binding.apply {
            // Progress indicator
            tvStepCounter.text = getString(R.string.step_of, currentStep + 1, steps.size)
            progressBar.max = steps.size
            progressBar.progress = currentStep + 1

            when (stepType) {
                STEP_USAGE_STATS -> {
                    ivStepIcon.setImageResource(R.drawable.ic_shield)
                    tvStepTitle.setText(R.string.onboarding_usage_stats_title)
                    tvStepDescription.setText(R.string.onboarding_usage_stats_desc)
                    btnPrimary.setText(R.string.onboarding_open_settings)
                    btnSkip.visibility = View.GONE
                    btnNext.visibility = if (isGranted) View.VISIBLE else View.GONE
                    statusIcon.setImageResource(
                        if (isGranted) R.drawable.ic_check_circle else R.drawable.ic_warning
                    )
                    statusIcon.visibility = View.VISIBLE
                    btnPrimary.visibility = if (isGranted) View.GONE else View.VISIBLE
                }
                STEP_OVERLAY -> {
                    ivStepIcon.setImageResource(R.drawable.ic_shield)
                    tvStepTitle.setText(R.string.onboarding_overlay_title)
                    tvStepDescription.setText(R.string.onboarding_overlay_desc)
                    btnPrimary.setText(R.string.onboarding_open_settings)
                    btnSkip.visibility = View.GONE
                    btnNext.visibility = if (isGranted) View.VISIBLE else View.GONE
                    statusIcon.setImageResource(
                        if (isGranted) R.drawable.ic_check_circle else R.drawable.ic_warning
                    )
                    statusIcon.visibility = View.VISIBLE
                    btnPrimary.visibility = if (isGranted) View.GONE else View.VISIBLE
                }
                STEP_ACCESSIBILITY -> {
                    ivStepIcon.setImageResource(R.drawable.ic_shield)
                    tvStepTitle.setText(R.string.onboarding_accessibility_title)
                    tvStepDescription.setText(R.string.onboarding_accessibility_desc)
                    statusIcon.setImageResource(
                        if (isGranted) R.drawable.ic_check_circle else R.drawable.ic_info
                    )
                    statusIcon.visibility = View.VISIBLE
                    if (isGranted) {
                        btnPrimary.visibility = View.GONE
                        btnSkip.visibility = View.GONE
                        btnNext.visibility = View.VISIBLE
                        btnNext.setText(R.string.onboarding_continue)
                    } else {
                        btnPrimary.setText(R.string.onboarding_open_settings)
                        btnPrimary.visibility = View.VISIBLE
                        btnSkip.visibility = View.VISIBLE
                        btnSkip.setText(R.string.onboarding_skip)
                        btnNext.visibility = View.GONE
                    }
                }
                STEP_BATTERY -> {
                    ivStepIcon.setImageResource(R.drawable.ic_battery)
                    tvStepTitle.setText(R.string.onboarding_battery_title)
                    tvStepDescription.text = getString(
                        R.string.onboarding_battery_desc,
                        android.os.Build.MANUFACTURER
                    )
                    btnPrimary.setText(R.string.onboarding_open_battery_settings)
                    btnSkip.visibility = View.VISIBLE
                    btnSkip.setText(R.string.onboarding_skip)
                    btnNext.visibility = View.GONE
                    statusIcon.visibility = View.GONE
                    btnPrimary.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun advanceStep() {
        val nextStep = currentStep + 1
        if (nextStep >= steps.size) {
            completeOnboarding()
        } else {
            showStep(nextStep)
        }
    }

    private fun completeOnboarding() {
        prefManager.onboardingComplete = true
        BlockerService.start(this)
        BlockerWatchdogWorker.schedule(this)
        goToMain()
    }

    private fun goToMain() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    companion object {
        private const val STEP_USAGE_STATS = 0
        private const val STEP_OVERLAY = 1
        private const val STEP_ACCESSIBILITY = 2
        private const val STEP_BATTERY = 3
    }
}
