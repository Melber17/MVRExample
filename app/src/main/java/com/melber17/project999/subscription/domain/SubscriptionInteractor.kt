package com.melber17.project999.subscription.domain

import kotlinx.coroutines.delay

interface SubscriptionInteractor {
    suspend fun subscribe()
    class Base(
        private val repository: SubscriptionRepository
    ) : SubscriptionInteractor {

        override suspend fun subscribe() {
            delay(1000)
            repository.subscribe()
        }
    }
}