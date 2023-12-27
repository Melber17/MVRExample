package com.melber17.project999.subscription.data

import com.melber17.project999.main.UserPremiumCache
import com.melber17.project999.subscription.domain.SubscriptionRepository

class BaseSubscriptionRepository(
    private val foregroundServiceWrapper: ForegroundServiceWrapper,
    private val userPremiumCache: UserPremiumCache.Mutable,
    private val cloudDataSource: SubscriptionCloudDataSource
): SubscriptionRepository {
    override fun isPremiumUser() = userPremiumCache.isUserPremium()

    override fun subscribe() {
        foregroundServiceWrapper.start()
    }

    override suspend fun subscribeInternal() {
        cloudDataSource.subscribe()
        userPremiumCache.saveUserPremium()
    }
}