package com.focuslock.ui.mute

import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.focuslock.R
import com.focuslock.data.db.entities.MutedApp
import com.focuslock.data.db.entities.MuteScheduleType
import com.focuslock.databinding.ActivityMuteRuleEditorBinding
import com.focuslock.ui.apps.AppPickerActivity
import com.focuslock.utils.TimeUtils
import com.focuslock.viewmodel.MuteRuleEditorViewModel
import com.google.android.material.datepicker.MaterialDatePicker
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MuteRuleEditorActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMuteRuleEditorBinding
    private val viewModel: MuteRuleEditorViewModel by viewModels()
    private val dateFormat = SimpleDateFormat("MMM d, yyyy", Locale.getDefault())

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
                MutedApp(muteRuleId = 0, packageName = pkg, appName = name)
            }
            viewModel.setMutedApps(apps)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMuteRuleEditorBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val ruleId = intent.getLongExtra(EXTRA_RULE_ID, -1L)
        val isNew = ruleId == -1L

        setSupportActionBar(binding.toolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            title = if (isNew) getString(R.string.new_mute_rule) else getString(R.string.edit_mute_rule)
        }

        viewModel.loadRule(ruleId)
        setupChips()
        setupListeners()
        observeViewModel()
        updateDateDisplay()
        updateTimeDisplay()
    }

    override fun onSupportNavigateUp(): Boolean { finish(); return true }

    private fun setupChips() {
        binding.chipGroupSchedule.setOnCheckedStateChangeListener { _, checkedIds ->
            val type = when {
                R.id.chip_date_range in checkedIds -> MuteScheduleType.DATE_RANGE
                R.id.chip_weekdays   in checkedIds -> MuteScheduleType.WEEKDAYS
                R.id.chip_weekends   in checkedIds -> MuteScheduleType.WEEKENDS
                else                               -> MuteScheduleType.EVERY_DAY
            }
            viewModel.scheduleType = type
            updateScheduleSections(type)
        }
    }

    private fun updateScheduleSections(type: MuteScheduleType) {
        val isDateRange = type == MuteScheduleType.DATE_RANGE
        binding.sectionDateRange.visibility = if (isDateRange) View.VISIBLE else View.GONE
        binding.sectionHourRange.visibility = if (isDateRange) View.GONE else View.VISIBLE
    }

    private fun setupListeners() {
        binding.btnSelectApps.setOnClickListener {
            val current = viewModel.mutedApps.value ?: emptyList()
            val intent = Intent(this, AppPickerActivity::class.java).apply {
                putStringArrayListExtra(
                    AppPickerActivity.EXTRA_SELECTED_PACKAGES,
                    ArrayList(current.map { it.packageName })
                )
            }
            appPickerLauncher.launch(intent)
        }

        binding.btnStartDate.setOnClickListener { showDatePicker(isStart = true) }
        binding.btnEndDate.setOnClickListener { showDatePicker(isStart = false) }

        binding.switchHourRange.setOnCheckedChangeListener { _, isChecked ->
            viewModel.useHourRange = isChecked
            binding.rowTimePickers.visibility = if (isChecked) View.VISIBLE else View.GONE
        }

        binding.btnStartTime.setOnClickListener {
            TimePickerDialog(this, { _, h, m ->
                viewModel.startHour = h; viewModel.startMinute = m
                binding.btnStartTime.text = TimeUtils.formatTime(h, m)
            }, viewModel.startHour, viewModel.startMinute, true).show()
        }

        binding.btnEndTime.setOnClickListener {
            TimePickerDialog(this, { _, h, m ->
                viewModel.endHour = h; viewModel.endMinute = m
                binding.btnEndTime.text = TimeUtils.formatTime(h, m)
            }, viewModel.endHour, viewModel.endMinute, true).show()
        }

        binding.btnSave.setOnClickListener {
            val name = binding.etRuleName.text?.toString()?.trim()
            if (name.isNullOrEmpty()) {
                binding.etRuleName.error = getString(R.string.error_profile_name_required)
                return@setOnClickListener
            }
            if ((viewModel.mutedApps.value ?: emptyList()).isEmpty()) {
                Toast.makeText(this, R.string.error_no_apps_selected, Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            viewModel.saveRule(name)
        }
    }

    private fun observeViewModel() {
        viewModel.mutedApps.observe(this) { apps ->
            binding.tvMutedAppsSummary.text = if (apps.isEmpty()) {
                getString(R.string.no_apps_selected)
            } else {
                resources.getQuantityString(R.plurals.apps_blocked_count, apps.size, apps.size)
            }
        }

        viewModel.saveComplete.observe(this) { done ->
            if (done) {
                Toast.makeText(this, R.string.mute_rule_saved, Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }

    private fun showDatePicker(isStart: Boolean) {
        val initial = if (isStart) viewModel.startDateMs else viewModel.endDateMs
        val picker = MaterialDatePicker.Builder.datePicker()
            .setTitleText(if (isStart) R.string.start_date else R.string.end_date)
            .setSelection(initial)
            .build()
        picker.addOnPositiveButtonClickListener { ms ->
            if (isStart) { viewModel.startDateMs = ms }
            else         { viewModel.endDateMs = ms }
            updateDateDisplay()
        }
        picker.show(supportFragmentManager, "date_picker_${if (isStart) "start" else "end"}")
    }

    private fun updateDateDisplay() {
        binding.btnStartDate.text = dateFormat.format(Date(viewModel.startDateMs))
        binding.btnEndDate.text   = dateFormat.format(Date(viewModel.endDateMs))
    }

    private fun updateTimeDisplay() {
        binding.btnStartTime.text = TimeUtils.formatTime(viewModel.startHour, viewModel.startMinute)
        binding.btnEndTime.text   = TimeUtils.formatTime(viewModel.endHour, viewModel.endMinute)
    }

    companion object {
        const val EXTRA_RULE_ID = "extra_rule_id"
    }
}
