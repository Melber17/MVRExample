package com.melber17.project999.subscription.screen

import com.melber17.project999.core.ClearRepresentative
import com.melber17.project999.core.Core
import com.melber17.project999.core.DispatchersList
import com.melber17.project999.core.HandleDeath
import com.melber17.project999.core.Module
import com.melber17.project999.core.RunAsync
import com.melber17.project999.main.UserPremiumCache
import com.melber17.project999.subscription.common.SubscriptionScopeModule
import com.melber17.project999.subscription.progress.data.BaseSubscriptionRepository
import com.melber17.project999.subscription.progress.data.ForegroundServiceWrapper
import com.melber17.project999.subscription.progress.data.SubscriptionCloudDataSource
import com.melber17.project999.subscription.progress.domain.SubscriptionInteractor
import com.melber17.project999.subscription.screen.presentation.SubscriptionObservable
import com.melber17.project999.subscription.screen.presentation.SubscriptionRepresentative
import com.melber17.project999.subscription.screen.presentation.SubscriptionUiMapper

class SubscriptionModule(
    private val core: Core,
    private val clear: () -> Unit,
    private val provideScopeModule: () -> SubscriptionScopeModule
) : Module<SubscriptionRepresentative> {

    override fun representative(): SubscriptionRepresentative =
        SubscriptionRepresentative.Base(
            HandleDeath.Base(),
            provideScopeModule.invoke().provideSubscriptionObservable(),
            clear,
            core.navigation(),
        )
}
