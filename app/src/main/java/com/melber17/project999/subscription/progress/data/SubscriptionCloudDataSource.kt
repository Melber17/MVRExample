package com.melber17.project999.subscription.progress.data

import kotlinx.coroutines.delay

interface SubscriptionCloudDataSource {
    suspend fun subscribe()

    class Base(): SubscriptionCloudDataSource {
        override suspend fun subscribe() {
            delay(10000)
        }
    }

    class MockForUiTest(): SubscriptionCloudDataSource {
        override suspend fun subscribe() = Unit
    }
}