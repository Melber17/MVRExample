package com.melber17.project999.subscription.data

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.melber17.project999.core.ProvideRepresentative
import com.melber17.project999.subscription.presentation.SubscriptionRepresentative

interface ForegroundServiceWrapper {
    fun start()

    class Base(
        private val workManager: WorkManager
    ) : ForegroundServiceWrapper {
        override fun start() {
            val work = OneTimeWorkRequestBuilder<Worker>()
                .build()
            workManager.beginUniqueWork(WORK_NAME, ExistingWorkPolicy.APPEND, work).enqueue()
        }

    }

}

private const val WORK_NAME = "load data"

class Worker(context: Context, params: WorkerParameters) : CoroutineWorker(context, params) {
    override suspend fun doWork(): Result {
        val representative = (applicationContext as ProvideRepresentative).provideRepresentative(
            SubscriptionRepresentative::class.java
        )

        representative.subscribeInternal()
        return Result.success()
    }

}