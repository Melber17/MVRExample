package com.melber17.project999.subscription.progress.presentation

import com.melber17.project999.core.HandleDeath
import com.melber17.project999.core.Representative
import com.melber17.project999.core.RunAsync
import com.melber17.project999.core.UiObserver
import com.melber17.project999.subscription.progress.domain.SubscriptionInteractor
import com.melber17.project999.subscription.progress.domain.SubscriptionResult
import com.melber17.project999.subscription.screen.presentation.SubscriptionInner
import com.melber17.project999.subscription.screen.presentation.SubscriptionObserved

interface SubscriptionProgressRepresentative : SubscriptionInner, Init,
    Representative<SubscriptionProgressUiState>, ComeBack<ComeBack<Boolean>>, SubscriptionObserved,
    Subscribe {

    suspend fun subscribeInternal()
    fun save(saveState: SaveAndRestoreSubscriptionUiState.Save)
    fun restore(restoreState: SaveAndRestoreSubscriptionUiState.Restore)


    class Base(
        runAsync: RunAsync,
        private val handleDeath: HandleDeath,
        private val interactor: SubscriptionInteractor,
        private val observable: SubscriptionProgressObservable,
        private val mapper: SubscriptionResult.Mapper,
    ) : Representative.Abstract<SubscriptionProgressUiState>(runAsync),
        SubscriptionProgressRepresentative {

        override suspend fun subscribeInternal() = handleAsyncInternal({
            interactor.subscribeInternal()
        }) { result ->
            result.map(mapper)
        }


        override fun save(saveState: SaveAndRestoreSubscriptionUiState.Save) {
            observable.save(saveState)
        }


        override fun restore(restoreState: SaveAndRestoreSubscriptionUiState.Restore) {
            if (handleDeath.isDeathHappened()) {
                handleDeath.deathHandled()
                val uiState: SubscriptionProgressUiState = restoreState.restore()
                uiState.restoreAfterDeath(this, observable)
            }
        }

        override fun subscribeInner() {
            handleAsync({
                interactor.subscribe()
            }) { result ->
                result.map(mapper)
            }
        }

        override fun init(firstRun: Boolean) {
            if (firstRun) {
                handleDeath.firstOpening()
                observable.update(SubscriptionProgressUiState.Hide)
            }
        }

        override fun comeback(data: ComeBack<Boolean>) {
            data.comeback(observable.canGoBack())
        }

        override fun observed() {
            observable.clear()
        }

        override fun subscribe() {
            observable.update(SubscriptionProgressUiState.Show)
            subscribeInner()
        }

        override fun startGettingUpdates(callback: UiObserver<SubscriptionProgressUiState>) {
            observable.updateObserver(callback)
        }

        override fun stopGettingUpdates() {
            observable.updateObserver()
        }
    }
}

