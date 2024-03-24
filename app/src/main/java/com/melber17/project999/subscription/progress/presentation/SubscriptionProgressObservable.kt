package com.melber17.project999.subscription.progress.presentation

import com.melber17.project999.core.UiObservable

interface SubscriptionProgressObservable : UiObservable<SubscriptionProgressUiState>, CanGoBack {


    fun save(saveState: SaveAndRestoreSubscriptionUiState.Save)

    class Base : UiObservable.Base<SubscriptionProgressUiState>(SubscriptionProgressUiState.Empty),
        SubscriptionProgressObservable {
        override fun save(saveState: SaveAndRestoreSubscriptionUiState.Save) {
            saveState.save(cache)
        }

        override fun canGoBack(): Boolean = cache.canGoBack()
    }
}