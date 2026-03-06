package com.focuslock.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.focuslock.R
import com.focuslock.databinding.ActivityMainBinding
import com.focuslock.services.BlockerService
import com.focuslock.ui.profile.ProfileEditorActivity
import com.focuslock.ui.settings.SettingsActivity
import com.focuslock.utils.PermissionUtils
import com.focuslock.viewmodel.MainViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels()
    private lateinit var profilesAdapter: ProfilesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        setupRecyclerView()
        setupFab()
        observeProfiles()

        // Ensure service is running
        if (PermissionUtils.allCorePermissionsGranted(this)) {
            BlockerService.start(this)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_settings -> {
                startActivity(Intent(this, SettingsActivity::class.java))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun setupRecyclerView() {
        profilesAdapter = ProfilesAdapter(
            onToggle = { profile, isEnabled ->
                viewModel.toggleProfile(profile, isEnabled)
            },
            onEdit = { profile ->
                openProfileEditor(profile.id)
            },
            onDelete = { profile ->
                AlertDialog.Builder(this)
                    .setTitle(R.string.delete_profile_title)
                    .setMessage(getString(R.string.delete_profile_message, profile.name))
                    .setPositiveButton(R.string.delete) { _, _ ->
                        viewModel.deleteProfile(profile)
                    }
                    .setNegativeButton(R.string.cancel, null)
                    .show()
            }
        )
        binding.rvProfiles.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = profilesAdapter
        }
    }

    private fun setupFab() {
        binding.fab.setOnClickListener {
            openProfileEditor(-1L)
        }
    }

    private fun observeProfiles() {
        viewModel.profiles.observe(this, Observer { profiles ->
            profilesAdapter.submitList(profiles)
            binding.tvEmptyState.visibility =
                if (profiles.isEmpty()) android.view.View.VISIBLE else android.view.View.GONE
        })
    }

    private fun openProfileEditor(profileId: Long) {
        val intent = Intent(this, ProfileEditorActivity::class.java).apply {
            putExtra(ProfileEditorActivity.EXTRA_PROFILE_ID, profileId)
        }
        startActivity(intent)
    }
}
