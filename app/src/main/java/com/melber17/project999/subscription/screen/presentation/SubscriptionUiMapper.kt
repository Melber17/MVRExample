package com.melber17.project999.subscription.screen.presentation

import com.melber17.project999.core.UiUpdate
import com.melber17.project999.subscription.progress.domain.SubscriptionResult
import com.melber17.project999.subscription.progress.presentation.SubscriptionProgressUiState

class SubscriptionUiMapper(
    private val observable: UiUpdate<SubscriptionUiState>,
    private val progressObservable: UiUpdate<SubscriptionProgressUiState>
): SubscriptionResult.Mapper {
    override fun mapSuccess() {
        observable.update(SubscriptionUiState.Success)
        progressObservable.update(SubscriptionProgressUiState.Hide)
    }
}