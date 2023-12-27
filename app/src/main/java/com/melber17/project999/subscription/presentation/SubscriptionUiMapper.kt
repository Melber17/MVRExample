package com.melber17.project999.subscription.presentation

import com.melber17.project999.core.UiUpdate
import com.melber17.project999.subscription.domain.SubscriptionResult

class SubscriptionUiMapper(
    private val observable: UiUpdate<SubscriptionUiState>
): SubscriptionResult.Mapper {
    override fun mapSuccess() {
        observable.update(SubscriptionUiState.Success)
    }
}