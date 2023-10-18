package com.melber17.project999.subscription.presentation

import androidx.annotation.MainThread
import com.melber17.project999.core.ClearRepresentative
import com.melber17.project999.core.HandleDeath
import com.melber17.project999.core.Representative
import com.melber17.project999.core.UiObserver
import com.melber17.project999.dashboard.DashboardRepresentative
import com.melber17.project999.dashboard.DashboardScreen
import com.melber17.project999.main.Navigation
import com.melber17.project999.subscription.domain.SubscriptionInteractor

interface SubscriptionRepresentative : Representative<SubscriptionUiState>, SubscriptionInner,
    SubscriptionObserved,
    SaveSubscriptionUiState {
    fun init(restoreState: SaveAndRestoreSubscriptionUiState.Restore)

    @MainThread
    fun subscribe()



    fun finish()

    class Base(
        private val handleDeath: HandleDeath,
        private val observable: SubscriptionObservable,
        private val clear: ClearRepresentative,
        private val interactor: SubscriptionInteractor,
        private val navigation: Navigation.Update
    ) : SubscriptionRepresentative {
        override fun init(restoreState: SaveAndRestoreSubscriptionUiState.Restore) {
            if (restoreState.isEmpty()) {
                handleDeath.firstOpening()
                observable.update(SubscriptionUiState.Initial)
            } else {
                if (handleDeath.isDeathHappened()) {
                    handleDeath.deathHandled()
                }
                restoreState.restore().restoreAfterDeath(this, observable)
            }
        }



        override fun subscribe() {
            observable.update(SubscriptionUiState.Loading)
            subscribeInner()
        }

        override fun subscribeInner() = interactor.subscribe {
            observable.update(SubscriptionUiState.Success)
        }

        override fun observed() = observable.clear()

        override fun save(saveState: SaveAndRestoreSubscriptionUiState.Save) {
            observable.save(saveState)
        }

        override fun finish() {
            clear.clear(SubscriptionRepresentative::class.java)
            navigation.update(DashboardScreen)
        }

        override fun startGettingUpdates(callback: UiObserver<SubscriptionUiState>) {
            observable.updateObserver(callback)
        }

        override fun stopGettingUpdates() {
            observable.updateObserver(EmptySubscriptionObserver)
        }
    }


}

object EmptySubscriptionObserver: SubscriptionObserver {
    override fun update(data: SubscriptionUiState) = Unit
}

interface SaveSubscriptionUiState {
    fun save(saveState: SaveAndRestoreSubscriptionUiState.Save)
}

interface SubscriptionObserved {
    fun observed()
}

interface SubscriptionInner {
    fun subscribeInner()
}