package com.melber17.project999.dashboard

import com.melber17.project999.core.HideAndShow

interface PremiumDashboardUiState {
    fun observed(representative: DashboardRepresentative) = representative.observed()
    fun show(button: HideAndShow, textView: HideAndShow)

    object Playing : PremiumDashboardUiState {
        override fun show(button: HideAndShow, textView: HideAndShow) {
            button.hide()
            textView.show()
        }
    }

    object Empty: PremiumDashboardUiState {
        override fun show(button: HideAndShow, textView: HideAndShow) = Unit
    }
}