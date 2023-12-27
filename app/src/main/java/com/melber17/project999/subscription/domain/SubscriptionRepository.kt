package com.melber17.project999.subscription.domain

interface SubscriptionRepository {
    fun isPremiumUser(): Boolean
    fun subscribe()

    suspend fun subscribeInternal()
}