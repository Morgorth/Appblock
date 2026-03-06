package com.focuslock.ui.history

import android.content.pm.PackageManager
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.focuslock.data.db.entities.OverrideLog
import com.focuslock.databinding.ItemOverrideLogBinding
import com.focuslock.utils.TimeUtils

class OverrideHistoryAdapter :
    ListAdapter<OverrideLog, OverrideHistoryAdapter.ViewHolder>(DIFF_CALLBACK) {

    inner class ViewHolder(private val binding: ItemOverrideLogBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(log: OverrideLog) {
            binding.tvDateTime.text = TimeUtils.formatDateTime(log.timestamp)
            binding.tvAppName.text = log.appName
            binding.tvProfileName.text = log.profileName
            binding.tvJustification.text = log.justification
            binding.tvDuration.text = binding.root.context.resources
                .getQuantityString(com.focuslock.R.plurals.minutes_count, log.durationMinutes, log.durationMinutes)

            // Try to load app icon
            try {
                val pm = binding.root.context.packageManager
                binding.ivAppIcon.setImageDrawable(pm.getApplicationIcon(log.packageName))
            } catch (e: PackageManager.NameNotFoundException) {
                binding.ivAppIcon.setImageResource(android.R.drawable.sym_def_app_icon)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemOverrideLogBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<OverrideLog>() {
            override fun areItemsTheSame(oldItem: OverrideLog, newItem: OverrideLog) =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: OverrideLog, newItem: OverrideLog) =
                oldItem == newItem
        }
    }
}
