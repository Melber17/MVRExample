package com.melber17.project999.subscription.presentation

import com.melber17.project999.core.UiObservable

interface SubscriptionObservable: UiObservable<SubscriptionUiState>, SaveSubscriptionUiState {
    class Base:  UiObservable.Single<SubscriptionUiState>(SubscriptionUiState.Empty),
        SubscriptionObservable {
        override fun save(saveState: SaveAndRestoreSubscriptionUiState.Save) {
          saveState.save(cache)
        }
    }
}