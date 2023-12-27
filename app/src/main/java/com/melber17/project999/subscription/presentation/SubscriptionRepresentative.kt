package com.melber17.project999.subscription.presentation

import androidx.annotation.MainThread
import com.melber17.project999.core.ClearRepresentative
import com.melber17.project999.core.DispatchersList
import com.melber17.project999.core.HandleDeath
import com.melber17.project999.core.Representative
import com.melber17.project999.core.RunAsync
import com.melber17.project999.core.UiObserver
import com.melber17.project999.dashboard.DashboardRepresentative
import com.melber17.project999.dashboard.DashboardScreen
import com.melber17.project999.main.Navigation
import com.melber17.project999.main.Screen
import com.melber17.project999.subscription.domain.SubscriptionInteractor
import com.melber17.project999.subscription.domain.SubscriptionResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

interface SubscriptionRepresentative : Representative<SubscriptionUiState>, SubscriptionInner,
    SubscriptionObserved,
    SaveSubscriptionUiState {
    fun init(restoreState: SaveAndRestoreSubscriptionUiState.Restore)

    @MainThread
    fun subscribe()

    suspend fun subscribeInternal()


    fun finish()
    fun comeback()

    class Base(
        private val handleDeath: HandleDeath,
        private val observable: SubscriptionObservable,
        private val clear: ClearRepresentative,
        private val interactor: SubscriptionInteractor,
        private val navigation: Navigation.Update,
        private val mapper: SubscriptionResult.Mapper,
        runAsync: RunAsync
    ) : Representative.Abstract<SubscriptionUiState>(runAsync), SubscriptionRepresentative {
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

        override suspend fun subscribeInternal() = handleAsyncInternal({
            interactor.subscribeInternal()
        }) { result ->
            result.map(mapper)
        }

        override fun subscribeInner() = handleAsync({
            interactor.subscribe()
        }) { result ->
            result.map(mapper)
        }

        override fun observed() = observable.clear()

        override fun save(saveState: SaveAndRestoreSubscriptionUiState.Save) {
            observable.save(saveState)
        }

        override fun finish() {
            clear()
            clear.clear(SubscriptionRepresentative::class.java)
            navigation.update(DashboardScreen)
        }

        override fun comeback() {
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