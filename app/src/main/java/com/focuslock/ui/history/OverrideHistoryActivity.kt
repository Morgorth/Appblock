package com.focuslock.ui.history

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.focuslock.R
import com.focuslock.databinding.ActivityOverrideHistoryBinding
import com.focuslock.viewmodel.OverrideHistoryViewModel

class OverrideHistoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOverrideHistoryBinding
    private val viewModel: OverrideHistoryViewModel by viewModels()
    private lateinit var historyAdapter: OverrideHistoryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOverrideHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            title = getString(R.string.override_history)
        }

        setupRecyclerView()
        observeLogs()

        binding.btnClearHistory.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle(R.string.clear_history_title)
                .setMessage(R.string.clear_history_message)
                .setPositiveButton(R.string.clear) { _, _ ->
                    viewModel.clearHistory()
                }
                .setNegativeButton(R.string.cancel, null)
                .show()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    private fun setupRecyclerView() {
        historyAdapter = OverrideHistoryAdapter()
        binding.rvHistory.apply {
            layoutManager = LinearLayoutManager(this@OverrideHistoryActivity)
            adapter = historyAdapter
        }
    }

    private fun observeLogs() {
        viewModel.logs.observe(this, Observer { logs ->
            historyAdapter.submitList(logs)
            binding.tvEmptyState.visibility = if (logs.isEmpty()) View.VISIBLE else View.GONE
            binding.btnClearHistory.isEnabled = logs.isNotEmpty()
        })
    }
}
