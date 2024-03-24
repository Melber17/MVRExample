package com.melber17.project999.subscription.common

import com.melber17.project999.subscription.screen.presentation.SubscriptionObservable

interface SubscriptionScopeModule {
    fun provideSubscriptionObservable(): SubscriptionObservable

    class Base: SubscriptionScopeModule {
        private val observable = SubscriptionObservable.Base()
        override fun provideSubscriptionObservable(): SubscriptionObservable {
            return observable
        }
    }
}