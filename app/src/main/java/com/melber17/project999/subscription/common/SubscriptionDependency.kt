package com.melber17.project999.subscription.common

import com.melber17.project999.core.ClearRepresentative
import com.melber17.project999.core.Core
import com.melber17.project999.core.Module
import com.melber17.project999.core.ProvideModule
import com.melber17.project999.core.Representative
import com.melber17.project999.subscription.progress.SubscriptionProgressModule
import com.melber17.project999.subscription.progress.presentation.SubscriptionProgressRepresentative
import com.melber17.project999.subscription.screen.SubscriptionModule
import com.melber17.project999.subscription.screen.presentation.SubscriptionRepresentative
import java.lang.IllegalStateException

class SubscriptionDependency(
    private val core: Core,
    private val clear: ClearRepresentative
) : ProvideModule {
    private var scopeModule: SubscriptionScopeModule? = null
    private val provideScopeModule: () -> SubscriptionScopeModule = {
        if (scopeModule == null) {
            scopeModule = SubscriptionScopeModule.Base()
        }
        scopeModule!!
    }
    private val clearScopeModule: () -> Unit = {
        clear.clear(SubscriptionProgressRepresentative::class.java)
        clear.clear(SubscriptionRepresentative::class.java)
        scopeModule = null
    }


    override fun <T : Representative<*>> module(clasz: Class<T>): Module<T> =
        when (clasz) {
            SubscriptionRepresentative::class.java -> SubscriptionModule(
                core,
                clearScopeModule,
                provideScopeModule
            )

            SubscriptionProgressRepresentative::class.java -> SubscriptionProgressModule(
                core,
                provideScopeModule
            )

            else -> throw IllegalStateException("Unknown clasz $clasz")
        } as Module<T>
}
