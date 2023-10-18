package com.melber17.project999.subscription.domain

import com.melber17.project999.main.UserPremiumCache

interface SubscriptionInteractor {
    fun subscribe(callback: () -> Unit)
    class Base(
        private val userPremiumCache: UserPremiumCache.Save
    ): SubscriptionInteractor {

        override fun subscribe(callback: () -> Unit) {
            Thread {
                Thread.sleep(1_000)
                userPremiumCache.saveUserPremium()
                callback.invoke()
            }.start()
        }
    }
}