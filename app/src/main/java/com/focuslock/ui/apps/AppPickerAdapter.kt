package com.focuslock.ui.apps

import android.content.pm.PackageManager
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.focuslock.databinding.ItemAppBinding
import com.focuslock.viewmodel.AppItem

class AppPickerAdapter(
    private val onToggle: (String) -> Unit
) : ListAdapter<AppItem, AppPickerAdapter.ViewHolder>(DIFF_CALLBACK) {

    inner class ViewHolder(private val binding: ItemAppBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: AppItem) {
            binding.tvAppName.text = item.appName
            binding.tvPackageName.text = item.packageName
            binding.cbSelected.isChecked = item.isSelected

            // Load app icon
            try {
                val pm = binding.root.context.packageManager
                binding.ivAppIcon.setImageDrawable(pm.getApplicationIcon(item.packageName))
            } catch (e: PackageManager.NameNotFoundException) {
                binding.ivAppIcon.setImageResource(android.R.drawable.sym_def_app_icon)
            }

            binding.cbSelected.setOnCheckedChangeListener(null)
            binding.cbSelected.isChecked = item.isSelected
            binding.cbSelected.setOnCheckedChangeListener { _, _ ->
                onToggle(item.packageName)
            }

            binding.root.setOnClickListener { onToggle(item.packageName) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemAppBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<AppItem>() {
            override fun areItemsTheSame(oldItem: AppItem, newItem: AppItem) =
                oldItem.packageName == newItem.packageName

            override fun areContentsTheSame(oldItem: AppItem, newItem: AppItem) =
                oldItem == newItem
        }
    }
}
