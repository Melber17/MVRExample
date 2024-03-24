package com.melber17.project999.dashboard

import com.melber17.project999.core.ClearRepresentative
import com.melber17.project999.core.Representative
import com.melber17.project999.core.UiObserver
import com.melber17.project999.main.Navigation
import com.melber17.project999.subscription.screen.presentation.SubscriptionScreen

interface DashboardRepresentative : Representative<PremiumDashboardUiState> {
    fun play()

    fun observed() = Unit

    class Premium(
        private val observable: PremiumDashboardObservable
    ) : DashboardRepresentative {
        override fun play() {
            observable.update(PremiumDashboardUiState.Playing)
        }

        override fun observed() = observable.clear()

        override fun startGettingUpdates(callback: UiObserver<PremiumDashboardUiState>) =
            observable.updateObserver(callback)

        override fun stopGettingUpdates() {
            observable.updateObserver(EmptyDashboardObserver)
        }

    }

    class Base(private val navigation: Navigation.Update, private val clear: ClearRepresentative) :
        DashboardRepresentative {
        override fun play() {
            clear.clear(DashboardRepresentative::class.java)

            navigation.update(SubscriptionScreen)
        }
    }
}

object EmptyDashboardObserver: DashboardObserver {
    override fun update(data: PremiumDashboardUiState) = Unit
}