package com.melber17.project999.core

import com.melber17.project999.dashboard.DashboardModule
import com.melber17.project999.dashboard.DashboardRepresentative
import com.melber17.project999.main.MainModule
import com.melber17.project999.main.MainRepresentative
import com.melber17.project999.subscription.common.SubscriptionDependency

interface ProvideRepresentative {
    fun <T : Representative<*>> provideRepresentative(clasz: Class<T>): T


    class CreateDependency(
        private val core: Core,
        private val clear: ClearRepresentative,
        private val provideModule: ProvideModule
    ) : ProvideModule {
        override fun <T : Representative<*>> module(clasz: Class<T>) =
            when (clasz) {
                MainRepresentative::class.java -> MainModule(core)
                DashboardRepresentative::class.java -> DashboardModule(core, clear)
                else -> provideModule.module(clasz)
            } as Module<T>
    }


    class Factory(
        core: Core,
        clear: ClearRepresentative
    ) : ProvideRepresentative, ClearRepresentative {

        private val createDependency: ProvideModule = CreateDependency(
            core,
            clear,
            SubscriptionDependency(core, clear)
        )
        private val representativeMap =
            mutableMapOf<Class<out Representative<*>>, Representative<*>>()

        override fun <T : Representative<*>> provideRepresentative(clasz: Class<T>): T {
            return if (representativeMap.containsKey(clasz)) {
                representativeMap[clasz] as T
            } else {
                val representative = createDependency.module(clasz).representative()

                representativeMap[clasz] = representative

                representative
            }
        }

        override fun clear(clasz: Class<out Representative<*>>) {
            representativeMap.remove(clasz)
        }
    }
}


    interface ProvideModule {
        fun <T : Representative<*>> module(clasz: Class<T>): Module<T>
    }
