package com.focuslock.ui.mute

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.focuslock.data.db.entities.MuteRule
import com.focuslock.data.db.entities.MuteScheduleType
import com.focuslock.databinding.ItemMuteRuleBinding
import com.focuslock.utils.TimeUtils
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MuteRulesAdapter(
    private val onToggle: (MuteRule, Boolean) -> Unit,
    private val onEdit: (MuteRule) -> Unit,
    private val onDelete: (MuteRule) -> Unit
) : ListAdapter<MuteRule, MuteRulesAdapter.ViewHolder>(DIFF) {

    private val dateFormat = SimpleDateFormat("MMM d", Locale.getDefault())

    inner class ViewHolder(private val binding: ItemMuteRuleBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(rule: MuteRule) {
            binding.tvRuleName.text = rule.name
            binding.tvScheduleSummary.text = scheduleSummary(rule)

            binding.switchEnabled.setOnCheckedChangeListener(null)
            binding.switchEnabled.isChecked = rule.isEnabled
            binding.switchEnabled.setOnCheckedChangeListener { _, checked ->
                onToggle(rule, checked)
            }

            binding.root.setOnClickListener { onEdit(rule) }
            binding.btnDelete.setOnClickListener { onDelete(rule) }
        }

        private fun scheduleSummary(rule: MuteRule): String = when (rule.scheduleType) {
            MuteScheduleType.DATE_RANGE -> {
                val start = dateFormat.format(Date(rule.startDateMs))
                val end   = dateFormat.format(Date(rule.endDateMs))
                "$start → $end"
            }
            MuteScheduleType.WEEKDAYS -> "Weekdays" + hourSuffix(rule)
            MuteScheduleType.WEEKENDS -> "Weekends" + hourSuffix(rule)
            MuteScheduleType.EVERY_DAY -> "Every day" + hourSuffix(rule)
        }

        private fun hourSuffix(rule: MuteRule): String =
            if (rule.useHourRange)
                ", ${TimeUtils.formatTime(rule.startHour, rule.startMinute)}–${TimeUtils.formatTime(rule.endHour, rule.endMinute)}"
            else ""
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(ItemMuteRuleBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        holder.bind(getItem(position))

    companion object {
        private val DIFF = object : DiffUtil.ItemCallback<MuteRule>() {
            override fun areItemsTheSame(a: MuteRule, b: MuteRule) = a.id == b.id
            override fun areContentsTheSame(a: MuteRule, b: MuteRule) = a == b
        }
    }
}
