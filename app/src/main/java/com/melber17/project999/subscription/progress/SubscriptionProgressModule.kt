package com.melber17.project999.subscription.progress

import com.melber17.project999.core.ClearRepresentative
import com.melber17.project999.core.Core
import com.melber17.project999.core.HandleDeath
import com.melber17.project999.core.Module
import com.melber17.project999.main.UserPremiumCache
import com.melber17.project999.subscription.common.SubscriptionScopeModule
import com.melber17.project999.subscription.progress.data.BaseSubscriptionRepository
import com.melber17.project999.subscription.progress.data.ForegroundServiceWrapper
import com.melber17.project999.subscription.progress.data.SubscriptionCloudDataSource
import com.melber17.project999.subscription.progress.domain.SubscriptionInteractor
import com.melber17.project999.subscription.progress.domain.SubscriptionRepository
import com.melber17.project999.subscription.progress.presentation.SubscriptionProgressObservable
import com.melber17.project999.subscription.progress.presentation.SubscriptionProgressRepresentative
import com.melber17.project999.subscription.screen.presentation.SubscriptionUiMapper

class SubscriptionProgressModule(
    private val core: Core,
    private val provideScopeModule: () -> SubscriptionScopeModule
) : Module<SubscriptionProgressRepresentative> {
    override fun representative(): SubscriptionProgressRepresentative {
        val observable = SubscriptionProgressObservable.Base()
        return SubscriptionProgressRepresentative.Base(
            core.runAsync(),
            HandleDeath.Base(),
            SubscriptionInteractor.Base(
                BaseSubscriptionRepository(
                    ForegroundServiceWrapper.Base(core.workManager()),
                    UserPremiumCache.Base(core.sharedPreferences()),
                    SubscriptionCloudDataSource.Base(),
                ),
            ),
            SubscriptionProgressObservable.Base(),
            SubscriptionUiMapper(
                provideScopeModule.invoke().provideSubscriptionObservable(),
                observable
            )
        )
    }

}