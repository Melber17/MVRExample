package com.melber17.project999.subscription.presentation

import android.os.Bundle
import com.melber17.project999.core.SaveAndRestoreState

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