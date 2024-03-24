package com.melber17.project999.subscription.progress.domain

interface SubscriptionRepository {
    fun isPremiumUser(): Boolean
    fun subscribe()

    suspend fun subscribeInternal()
}