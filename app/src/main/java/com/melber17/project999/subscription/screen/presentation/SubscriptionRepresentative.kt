package com.melber17.project999.subscription.screen.presentation

import com.melber17.project999.core.HandleDeath
import com.melber17.project999.core.Representative
import com.melber17.project999.core.UiObserver
import com.melber17.project999.dashboard.DashboardScreen
import com.melber17.project999.main.Navigation
import com.melber17.project999.subscription.progress.presentation.ComeBack

interface SubscriptionRepresentative : Representative<SubscriptionUiState>,
    SubscriptionObserved,
    SaveSubscriptionUiState, ComeBack<Boolean> {
    fun init(restoreState: SaveAndRestoreSubscriptionUiState.Restore)
    fun finish()
    fun subscribe()


    class Base(
        private val handleDeath: HandleDeath,
        private val observable: SubscriptionObservable,
        private val clear: () -> Unit,
        private val navigation: Navigation.Update,
    ) : SubscriptionRepresentative {
        override fun init(restoreState: SaveAndRestoreSubscriptionUiState.Restore) {
            if (restoreState.isEmpty()) {
                handleDeath.firstOpening()
                observable.update(SubscriptionUiState.Initial)
            } else {
                if (handleDeath.isDeathHappened()) {
                    handleDeath.deathHandled()
                    val uiState = restoreState.restore()
                    uiState.restoreAfterDeath(observable)
                }
            }
        }


        override fun subscribe() {
            observable.update(SubscriptionUiState.Loading)
        }

        override fun observed() = observable.clear()

        override fun save(saveState: SaveAndRestoreSubscriptionUiState.Save) {
            observable.save(saveState)
        }

        override fun finish() {
            navigation.update(DashboardScreen)
            clear.invoke()
        }

        override fun comeback(data: Boolean) {
            if (data)
                finish()
        }

        override fun startGettingUpdates(callback: UiObserver<SubscriptionUiState>) {
            observable.updateObserver(callback)
        }

        override fun stopGettingUpdates() {
            observable.updateObserver(EmptySubscriptionObserver)
        }
    }


}

object EmptySubscriptionObserver : SubscriptionObserver {
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
