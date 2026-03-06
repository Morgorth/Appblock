package com.focuslock.ui.profile

import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.focuslock.R
import com.focuslock.data.db.entities.BlockedApp
import com.focuslock.data.db.entities.ScheduleDay
import com.focuslock.databinding.ActivityProfileEditorBinding
import com.focuslock.databinding.ItemScheduleDayBinding
import com.focuslock.ui.apps.AppPickerActivity
import com.focuslock.utils.TimeUtils
import com.focuslock.viewmodel.ProfileEditorViewModel

class ProfileEditorActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfileEditorBinding
    private val viewModel: ProfileEditorViewModel by viewModels()

    private var profileId: Long = -1L

    private val appPickerLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val packages = result.data
                ?.getStringArrayListExtra(AppPickerActivity.RESULT_SELECTED_PACKAGES)
                ?: emptyList<String>()
            val names = result.data
                ?.getStringArrayListExtra(AppPickerActivity.RESULT_SELECTED_NAMES)
                ?: emptyList<String>()
            val apps = packages.zip(names).map { (pkg, name) ->
                BlockedApp(
                    profileId = viewModel.currentProfileId,
                    packageName = pkg,
                    appName = name
                )
            }
            viewModel.setBlockedApps(apps)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileEditorBinding.inflate(layoutInflater)
        setContentView(binding.root)

        profileId = intent.getLongExtra(EXTRA_PROFILE_ID, -1L)
        val isNew = profileId == -1L

        setSupportActionBar(binding.toolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            title = if (isNew) getString(R.string.new_profile) else getString(R.string.edit_profile)
        }

        viewModel.loadProfile(profileId)
        setupObservers()
        setupListeners()
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    private fun setupObservers() {
        viewModel.profile.observe(this, Observer { profile ->
            if (profile != null) {
                binding.etProfileName.setText(profile.name)
                binding.sliderOverrideDuration.value = profile.overrideDurationMinutes.toFloat()
                updateOverrideDurationLabel(profile.overrideDurationMinutes)
            }
        })

        viewModel.blockedApps.observe(this, Observer { apps ->
            binding.tvBlockedAppsSummary.text = if (apps.isEmpty()) {
                getString(R.string.no_apps_selected)
            } else {
                resources.getQuantityString(R.plurals.apps_blocked_count, apps.size, apps.size)
            }
        })

        viewModel.scheduleDays.observe(this, Observer { days ->
            renderSchedule(days)
        })

        viewModel.saveComplete.observe(this, Observer { done ->
            if (done) {
                Toast.makeText(this, R.string.profile_saved, Toast.LENGTH_SHORT).show()
                finish()
            }
        })
    }

    private fun setupListeners() {
        binding.btnSelectApps.setOnClickListener {
            val currentApps = viewModel.blockedApps.value ?: emptyList()
            val intent = Intent(this, AppPickerActivity::class.java).apply {
                putStringArrayListExtra(
                    AppPickerActivity.EXTRA_SELECTED_PACKAGES,
                    ArrayList(currentApps.map { it.packageName })
                )
            }
            appPickerLauncher.launch(intent)
        }

        binding.sliderOverrideDuration.addOnChangeListener { _, value, _ ->
            updateOverrideDurationLabel(value.toInt())
        }

        binding.btnSave.setOnClickListener {
            val name = binding.etProfileName.text?.toString()?.trim()
            if (name.isNullOrEmpty()) {
                binding.etProfileName.error = getString(R.string.error_profile_name_required)
                return@setOnClickListener
            }
            val duration = binding.sliderOverrideDuration.value.toInt()
            viewModel.saveProfile(name, duration)
        }

        // Quick schedule presets
        binding.btnPresetWeekdays.setOnClickListener { applyPreset(listOf(1, 2, 3, 4, 5)) }
        binding.btnPresetWeekends.setOnClickListener { applyPreset(listOf(6, 7)) }
        binding.btnPresetEveryDay.setOnClickListener { applyPreset((1..7).toList()) }
    }

    private fun updateOverrideDurationLabel(minutes: Int) {
        binding.tvOverrideDurationValue.text =
            resources.getQuantityString(R.plurals.minutes_count, minutes, minutes)
    }

    private fun renderSchedule(days: List<ScheduleDay>) {
        binding.scheduleContainer.removeAllViews()
        days.sortedBy { it.dayOfWeek }.forEach { day ->
            val dayBinding = ItemScheduleDayBinding.inflate(
                layoutInflater, binding.scheduleContainer, true
            )
            bindScheduleDay(dayBinding, day)
        }
    }

    private fun bindScheduleDay(dayBinding: ItemScheduleDayBinding, day: ScheduleDay) {
        dayBinding.tvDayName.text = TimeUtils.dayName(day.dayOfWeek)
        dayBinding.switchDayEnabled.isChecked = day.isEnabled
        dayBinding.timePickers.visibility = if (day.isEnabled) View.VISIBLE else View.GONE

        dayBinding.tvStartTime.text = TimeUtils.formatTime(day.startHour, day.startMinute)
        dayBinding.tvEndTime.text = TimeUtils.formatTime(day.endHour, day.endMinute)

        dayBinding.switchDayEnabled.setOnCheckedChangeListener { _, isChecked ->
            dayBinding.timePickers.visibility = if (isChecked) View.VISIBLE else View.GONE
            viewModel.updateScheduleDay(day.copy(isEnabled = isChecked))
        }

        dayBinding.tvStartTime.setOnClickListener {
            showTimePicker(day.startHour, day.startMinute) { h, m ->
                dayBinding.tvStartTime.text = TimeUtils.formatTime(h, m)
                viewModel.updateScheduleDay(day.copy(startHour = h, startMinute = m))
            }
        }

        dayBinding.tvEndTime.setOnClickListener {
            showTimePicker(day.endHour, day.endMinute) { h, m ->
                dayBinding.tvEndTime.text = TimeUtils.formatTime(h, m)
                viewModel.updateScheduleDay(day.copy(endHour = h, endMinute = m))
            }
        }
    }

    private fun showTimePicker(hour: Int, minute: Int, onSet: (Int, Int) -> Unit) {
        TimePickerDialog(this, { _, h, m -> onSet(h, m) }, hour, minute, true).show()
    }

    private fun applyPreset(daysToEnable: List<Int>) {
        val currentDays = viewModel.scheduleDays.value?.toMutableList() ?: return
        val updated = currentDays.map { day ->
            day.copy(isEnabled = day.dayOfWeek in daysToEnable)
        }
        updated.forEach { viewModel.updateScheduleDay(it) }
    }

    companion object {
        const val EXTRA_PROFILE_ID = "extra_profile_id"
    }
}
