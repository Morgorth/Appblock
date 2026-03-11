package com.focuslock.ui.main

import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewOutlineProvider
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
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
        observeActivitySummary()

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
        viewModel.profiles.observe(this) { profiles ->
            profilesAdapter.submitList(profiles)
            binding.tvEmptyState.visibility =
                if (profiles.isEmpty()) View.VISIBLE else View.GONE
        }
    }

    private fun observeActivitySummary() {
        viewModel.activitySummary.observe(this) { summary ->
            val blocked = summary.blockedPackages

            // Stats row
            binding.tvStatBlockedCount.text = blocked.size.toString()

            if (summary.freeAtTime != null) {
                binding.tvStatFreeAt.text = summary.freeAtTime
                binding.tvStatFreeLabel.text = getString(R.string.stat_label_free_at)
            } else {
                binding.tvStatFreeAt.text = getString(R.string.stat_value_open)
                binding.tvStatFreeLabel.text = getString(R.string.stat_label_status)
            }

            binding.tvStatOverrides.text = summary.totalOverrides.toString()

            // Icon row
            if (blocked.isEmpty()) {
                binding.iconRowContainer.visibility = View.GONE
                binding.dividerIcons.visibility = View.GONE
            } else {
                binding.iconRowContainer.visibility = View.VISIBLE
                binding.dividerIcons.visibility = View.VISIBLE
                populateBlockedIcons(blocked)
            }
        }
    }

    private fun populateBlockedIcons(packages: List<String>) {
        val row = binding.iconRow
        row.removeAllViews()

        val sizePx = dpToPx(40)
        val overlapPx = dpToPx(10)
        val maxIcons = 9

        packages.take(maxIcons).forEachIndexed { index, pkg ->
            val icon = loadAppIcon(pkg)
            val iv = ImageView(this).apply {
                layoutParams = LinearLayout.LayoutParams(sizePx, sizePx).also { lp ->
                    if (index > 0) lp.marginStart = -overlapPx
                }
                setImageDrawable(icon)
                setBackgroundResource(R.drawable.bg_icon_circle)
                clipToOutline = true
                outlineProvider = ViewOutlineProvider.BACKGROUND
                elevation = (index + 1) * 1f
                scaleType = ImageView.ScaleType.FIT_CENTER
                setPadding(dpToPx(3), dpToPx(3), dpToPx(3), dpToPx(3))
            }
            row.addView(iv)
        }

        val overflow = packages.size - maxIcons
        if (overflow > 0) {
            val tv = TextView(this).apply {
                layoutParams = LinearLayout.LayoutParams(sizePx, sizePx).also { lp ->
                    lp.marginStart = -overlapPx
                }
                text = "+$overflow"
                textSize = 12f
                setTextColor(getColor(R.color.text_secondary))
                setBackgroundResource(R.drawable.bg_icon_circle)
                gravity = android.view.Gravity.CENTER
                elevation = (maxIcons + 1) * 1f
            }
            row.addView(tv)
        }
    }

    private fun loadAppIcon(packageName: String): Drawable =
        try {
            packageManager.getApplicationIcon(packageName)
        } catch (e: PackageManager.NameNotFoundException) {
            packageManager.defaultActivityIcon
        }

    private fun dpToPx(dp: Int): Int =
        (dp * resources.displayMetrics.density + 0.5f).toInt()

    private fun openProfileEditor(profileId: Long) {
        val intent = Intent(this, ProfileEditorActivity::class.java).apply {
            putExtra(ProfileEditorActivity.EXTRA_PROFILE_ID, profileId)
        }
        startActivity(intent)
    }
}
