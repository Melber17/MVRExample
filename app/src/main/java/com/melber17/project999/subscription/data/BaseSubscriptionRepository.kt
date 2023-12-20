package com.melber17.project999.subscription.data

import com.melber17.project999.main.UserPremiumCache
import com.melber17.project999.subscription.domain.SubscriptionRepository

class BaseSubscriptionRepository(
    private val userPremiumCache: UserPremiumCache.Save,
    private val cloudDataSource: SubscriptionCloudDataSource
): SubscriptionRepository {
    override suspend fun subscribe() {
        cloudDataSource.subscribe()
        userPremiumCache.saveUserPremium()
    }
}