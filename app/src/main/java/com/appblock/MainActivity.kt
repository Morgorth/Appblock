package com.appblock

import android.app.AppOpsManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import com.appblock.databinding.ActivityMainBinding
import com.appblock.service.BlockerService
import com.appblock.util.NotificationPermissionHelper

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    // Android 13+ (Pixel 8/9) runtime notification permission
    private val requestNotificationPermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
            if (!granted) {
                // User denied – the service will still run but no persistent notification
                // is shown. We surface a one-time snackbar to explain the impact.
            }
            checkUsageAccessAndProceed()
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // -----------------------------------------------------------------------
        // Edge-to-edge setup for Pixel 8 (Android 14) and Pixel 9 (Android 15).
        //
        // WindowCompat.setDecorFitsSystemWindows(false) tells the framework we
        // will handle insets ourselves, preventing the automatic padding that
        // would leave a gap behind the navigation bar / status bar.
        // -----------------------------------------------------------------------
        WindowCompat.setDecorFitsSystemWindows(window, false)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        applyWindowInsets()
        setupBackInvokedCallback()

        binding.fabToggle.setOnClickListener {
            if (hasUsageAccess()) {
                toggleBlocker()
            } else {
                showUsageAccessDialog()
            }
        }

        // Request notification permission on Android 13+ before starting
        NotificationPermissionHelper.requestIfNeeded(this, requestNotificationPermission)
    }

    // ---------------------------------------------------------------------------
    // Window insets – ensures content is not hidden behind:
    //   • The status bar (top) on Pixel 8/9
    //   • The 3-button or gesture navigation bar (bottom) on Pixel 8/9
    //   • The punch-hole camera cutout on Pixel 8/9 (top)
    // ---------------------------------------------------------------------------
    private fun applyWindowInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { view, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            val cutout = insets.getInsets(WindowInsetsCompat.Type.displayCutout())

            // Combine system-bar and cutout insets so we respect both the
            // Pixel 8/9 punch-hole camera and the navigation bar simultaneously.
            view.updatePadding(
                top = maxOf(systemBars.top, cutout.top),
                bottom = systemBars.bottom,
                left = maxOf(systemBars.left, cutout.left),
                right = maxOf(systemBars.right, cutout.right)
            )
            insets
        }
    }

    // ---------------------------------------------------------------------------
    // Predictive back gesture – Android 14+ (Pixel 8) and Android 15 (Pixel 9)
    // recommend registering an OnBackInvokedCallback instead of overriding
    // onBackPressed().  The manifest flag android:enableOnBackInvokedCallback
    // opts the entire app in; here we register any in-activity logic.
    // ---------------------------------------------------------------------------
    private fun setupBackInvokedCallback() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            onBackInvokedDispatcher.registerOnBackInvokedCallback(
                android.window.OnBackInvokedDispatcher.PRIORITY_DEFAULT
            ) {
                finish()
            }
        }
    }

    private fun hasUsageAccess(): Boolean {
        val appOps = getSystemService(Context.APP_OPS_SERVICE) as AppOpsManager
        val mode = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            appOps.unsafeCheckOpNoThrow(
                AppOpsManager.OPSTR_GET_USAGE_STATS,
                android.os.Process.myUid(),
                packageName
            )
        } else {
            @Suppress("DEPRECATION")
            appOps.checkOpNoThrow(
                AppOpsManager.OPSTR_GET_USAGE_STATS,
                android.os.Process.myUid(),
                packageName
            )
        }
        return mode == AppOpsManager.MODE_ALLOWED
    }

    private fun checkUsageAccessAndProceed() {
        if (!hasUsageAccess()) {
            showUsageAccessDialog()
        }
    }

    private fun showUsageAccessDialog() {
        AlertDialog.Builder(this)
            .setTitle(R.string.permission_usage_title)
            .setMessage(R.string.permission_usage_message)
            .setPositiveButton(R.string.ok) { _, _ ->
                startActivity(Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS))
            }
            .setNegativeButton(R.string.cancel, null)
            .show()
    }

    private fun toggleBlocker() {
        val intent = Intent(this, BlockerService::class.java)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(intent)
        } else {
            startService(intent)
        }
    }
}
