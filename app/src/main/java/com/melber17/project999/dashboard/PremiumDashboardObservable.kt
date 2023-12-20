package com.melber17.project999.dashboard

import com.melber17.project999.core.UiObservable

interface PremiumDashboardObservable: UiObservable<PremiumDashboardUiState> {
    class Base: UiObservable.Base<PremiumDashboardUiState>(PremiumDashboardUiState.Empty), PremiumDashboardObservable
}