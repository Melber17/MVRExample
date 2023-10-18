package com.melber17.project999.core

import com.melber17.project999.dashboard.DashboardModule
import com.melber17.project999.dashboard.DashboardRepresentative
import com.melber17.project999.main.MainModule
import com.melber17.project999.main.MainRepresentative
import com.melber17.project999.subscription.SubscriptionModule
import com.melber17.project999.subscription.presentation.SubscriptionRepresentative
import java.lang.IllegalStateException

interface ProvideRepresentative {
    fun <T : Representative<*>> provideRepresentative(clasz: Class<T>): T

    class CreateDependency(
        private val core: Core,
        private val clear: ClearRepresentative,
    ) : ProvideRepresentative {
        override fun <T : Representative<*>> provideRepresentative(clasz: Class<T>): T {
            return when (clasz) {
                MainRepresentative::class.java -> MainModule(core).representative()
                DashboardRepresentative::class.java -> DashboardModule(core, clear).representative()
                SubscriptionRepresentative::class.java -> SubscriptionModule(
                    core,
                    clear
                ).representative()

                else -> throw IllegalStateException("unknown class")
            } as T
        }

    }

    class Factory(
        private val createDependency: ProvideRepresentative
    ) : ProvideRepresentative, ClearRepresentative {
        private val representativeMap =
            mutableMapOf<Class<out Representative<*>>, Representative<*>>()

        override fun <T : Representative<*>> provideRepresentative(clasz: Class<T>): T {
            return if (representativeMap.containsKey(clasz)) {
                representativeMap[clasz] as T
            } else {
                val representative = createDependency.provideRepresentative(clasz)

                representativeMap[clasz] = representative

                representative
            }
        }

        override fun clear(clasz: Class<out Representative<*>>) {
            representativeMap.remove(clasz)
        }
    }
}