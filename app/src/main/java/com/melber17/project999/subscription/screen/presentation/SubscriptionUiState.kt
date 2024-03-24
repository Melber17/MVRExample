package com.melber17.project999.subscription.screen.presentation

import com.melber17.project999.core.HideAndShow
import com.melber17.project999.subscription.progress.presentation.Subscribe
import java.io.Serializable

interface SubscriptionUiState : Serializable {
    fun show(subscribeButton: HideAndShow, progressBar: Subscribe, finishButton: HideAndShow)

    fun observed(representative: SubscriptionObserved) = representative.observed()
    fun restoreAfterDeath(observable: SubscriptionObservable) = observable.update(this)
    object Initial : SubscriptionUiState {
        override fun show(
            subscribeButton: HideAndShow,
            progressBar: Subscribe,
            finishButton: HideAndShow
        ) {
            subscribeButton.show()
            finishButton.hide()
        }
    }

    object Loading : SubscriptionUiState {
        override fun show(
            subscribeButton: HideAndShow,
            progressBar: Subscribe,
            finishButton: HideAndShow
        ) {
            subscribeButton.hide()
            progressBar.subscribe()
            finishButton.hide()
        }

    }

    object Success : SubscriptionUiState {
        override fun show(
            subscribeButton: HideAndShow,
            progressBar: Subscribe,
            finishButton: HideAndShow
        ) {
            subscribeButton.hide()
            finishButton.show()
        }
    }

    object Empty : SubscriptionUiState {
        override fun show(
            subscribeButton: HideAndShow,
            progressBar: Subscribe,
            finishButton: HideAndShow
        ) = Unit

        override fun restoreAfterDeath(
            observable: SubscriptionObservable
        ) = Unit
    }
}