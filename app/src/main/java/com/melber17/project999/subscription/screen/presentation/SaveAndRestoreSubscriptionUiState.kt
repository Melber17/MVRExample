package com.melber17.project999.subscription.screen.presentation

import android.os.Bundle
import com.melber17.project999.core.SaveAndRestoreState
import com.melber17.project999.subscription.progress.presentation.SubscriptionProgressUiState

interface SaveAndRestoreSubscriptionUiState {
    interface Save : SaveAndRestoreState.Save<SubscriptionUiState>

    interface Restore : SaveAndRestoreState.Restore<SubscriptionUiState>


    interface Mutable : Save, Restore


    class Base(bundle: Bundle?) : SaveAndRestoreState.Abstract<SubscriptionUiState>(
        bundle,
        KEY,
        SubscriptionUiState::class.java
    ), Mutable
}

private const val KEY = "SubscriptionUiStateSaveAndRestoreKey"