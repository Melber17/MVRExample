package com.melber17.project999.subscription

import com.melber17.project999.core.ClearRepresentative
import com.melber17.project999.core.Core
import com.melber17.project999.core.HandleDeath
import com.melber17.project999.core.Module
import com.melber17.project999.main.UserPremiumCache
import com.melber17.project999.subscription.domain.SubscriptionInteractor
import com.melber17.project999.subscription.presentation.SubscriptionObservable
import com.melber17.project999.subscription.presentation.SubscriptionRepresentative

class SubscriptionModule(
    private val core: Core,
    private val clear: ClearRepresentative
) : Module<SubscriptionRepresentative> {

    override fun representative(): SubscriptionRepresentative {
        return SubscriptionRepresentative.Base(
            HandleDeath.Base(),
            SubscriptionObservable.Base(),
            clear,
            SubscriptionInteractor.Base(UserPremiumCache.Base(core.sharedPreferences())),
            core.navigation()
        )
    }
}