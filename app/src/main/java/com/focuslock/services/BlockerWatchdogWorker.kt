package com.focuslock.services

import android.content.Context
import androidx.work.*
import com.focuslock.utils.PermissionUtils
import java.util.concurrent.TimeUnit

/**
 * WorkManager periodic task that acts as a watchdog to restart the BlockerService
 * if it was killed by the OEM's aggressive battery optimization.
 *
 * Runs every 15 minutes (minimum WorkManager interval). If the blocker service
 * is not running and all required permissions are granted, it restarts it.
 */
class BlockerWatchdogWorker(
    private val context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        if (PermissionUtils.allCorePermissionsGranted(context)) {
            BlockerService.start(context)
        }
        return Result.success()
    }

    companion object {
        private const val WORK_NAME = "focuslock_watchdog"

        fun schedule(context: Context) {
            val constraints = Constraints.Builder()
                .setRequiresBatteryNotLow(false)
                .build()

            val request = PeriodicWorkRequestBuilder<BlockerWatchdogWorker>(
                15, TimeUnit.MINUTES
            )
                .setConstraints(constraints)
                .setBackoffCriteria(
                    BackoffPolicy.LINEAR,
                    WorkRequest.MIN_BACKOFF_MILLIS,
                    TimeUnit.MILLISECONDS
                )
                .build()

            WorkManager.getInstance(context).enqueueUniquePeriodicWork(
                WORK_NAME,
                ExistingPeriodicWorkPolicy.KEEP,
                request
            )
        }

        fun cancel(context: Context) {
            WorkManager.getInstance(context).cancelUniqueWork(WORK_NAME)
        }
    }
}
