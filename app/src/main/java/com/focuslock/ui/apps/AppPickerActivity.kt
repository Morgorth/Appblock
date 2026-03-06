package com.focuslock.ui.apps

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.focuslock.R
import com.focuslock.databinding.ActivityAppPickerBinding
import com.focuslock.viewmodel.AppPickerViewModel

class AppPickerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAppPickerBinding
    private val viewModel: AppPickerViewModel by viewModels()
    private lateinit var adapter: AppPickerAdapter

    private var showSystemApps = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAppPickerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            title = getString(R.string.select_apps)
        }

        val alreadySelected = intent
            .getStringArrayListExtra(EXTRA_SELECTED_PACKAGES)
            ?.toSet() ?: emptySet()

        setupRecyclerView()
        setupObservers()
        viewModel.loadApps(alreadySelected)

        binding.btnDone.setOnClickListener {
            returnSelectedApps()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_app_picker, menu)
        val searchItem = menu.findItem(R.id.action_search)
        val searchView = searchItem.actionView as SearchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?) = false
            override fun onQueryTextChange(newText: String?): Boolean {
                viewModel.filter(newText ?: "")
                return true
            }
        })
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> { finish(); true }
            R.id.action_show_system_apps -> {
                showSystemApps = !showSystemApps
                item.isChecked = showSystemApps
                val alreadySelected = intent
                    .getStringArrayListExtra(EXTRA_SELECTED_PACKAGES)
                    ?.toSet() ?: emptySet()
                viewModel.toggleSystemApps(showSystemApps, alreadySelected)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    private fun setupRecyclerView() {
        adapter = AppPickerAdapter { packageName ->
            viewModel.toggleSelection(packageName)
            updateDoneButton()
        }
        binding.rvApps.apply {
            layoutManager = LinearLayoutManager(this@AppPickerActivity)
            this.adapter = this@AppPickerActivity.adapter
        }
    }

    private fun setupObservers() {
        viewModel.apps.observe(this, Observer { apps ->
            adapter.submitList(apps)
        })
        viewModel.isLoading.observe(this, Observer { loading ->
            binding.progressBar.visibility = if (loading) View.VISIBLE else View.GONE
            binding.rvApps.visibility = if (loading) View.GONE else View.VISIBLE
        })
    }

    private fun updateDoneButton() {
        val count = viewModel.apps.value?.count { it.isSelected } ?: 0
        binding.btnDone.text = if (count == 0) {
            getString(R.string.done)
        } else {
            getString(R.string.done_with_count, count)
        }
    }

    private fun returnSelectedApps() {
        val selected = viewModel.getSelectedApps(-1L) // profileId assigned by editor
        val packages = ArrayList(selected.map { it.packageName })
        val names = ArrayList(selected.map { it.appName })
        val resultIntent = Intent().apply {
            putStringArrayListExtra(RESULT_SELECTED_PACKAGES, packages)
            putStringArrayListExtra(RESULT_SELECTED_NAMES, names)
        }
        setResult(RESULT_OK, resultIntent)
        finish()
    }

    companion object {
        const val EXTRA_SELECTED_PACKAGES = "extra_selected_packages"
        const val RESULT_SELECTED_PACKAGES = "result_selected_packages"
        const val RESULT_SELECTED_NAMES = "result_selected_names"
    }
}
