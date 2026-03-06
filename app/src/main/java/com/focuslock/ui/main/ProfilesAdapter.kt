package com.focuslock.ui.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.focuslock.data.db.entities.Profile
import com.focuslock.databinding.ItemProfileBinding

class ProfilesAdapter(
    private val onToggle: (Profile, Boolean) -> Unit,
    private val onEdit: (Profile) -> Unit,
    private val onDelete: (Profile) -> Unit
) : ListAdapter<Profile, ProfilesAdapter.ViewHolder>(DIFF_CALLBACK) {

    inner class ViewHolder(private val binding: ItemProfileBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(profile: Profile) {
            binding.tvProfileName.text = profile.name
            binding.switchEnabled.isChecked = profile.isEnabled

            // Avoid triggering listener during rebind
            binding.switchEnabled.setOnCheckedChangeListener(null)
            binding.switchEnabled.isChecked = profile.isEnabled
            binding.switchEnabled.setOnCheckedChangeListener { _, isChecked ->
                onToggle(profile, isChecked)
            }

            binding.root.setOnClickListener { onEdit(profile) }
            binding.btnDelete.setOnClickListener { onDelete(profile) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemProfileBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Profile>() {
            override fun areItemsTheSame(oldItem: Profile, newItem: Profile) =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: Profile, newItem: Profile) =
                oldItem == newItem
        }
    }
}
