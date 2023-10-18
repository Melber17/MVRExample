package com.melber17.project999.dashboard

import android.os.Bundle
import android.view.View
import com.melber17.project999.R
import com.melber17.project999.core.CustomButton
import com.melber17.project999.core.CustomTextView
import com.melber17.project999.core.UiObserver
import com.melber17.project999.main.BaseFragment

class DashboardFragment :BaseFragment<DashboardRepresentative>(R.layout.fragment_dashboard) {
    override val clasz = DashboardRepresentative::class.java
    private lateinit var callback: UiObserver<PremiumDashboardUiState>



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val button = view.findViewById<CustomButton>(R.id.playButton)
        val textView = view.findViewById<CustomTextView>(R.id.showPlayingTextView)
        button?.setOnClickListener {
            representative.play()
        }

        callback = object : DashboardObserver {
            override fun update(data: PremiumDashboardUiState) {
                data.show(button, textView)
                data.observed(representative)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        representative.startGettingUpdates(callback)
    }

    override fun onPause() {
        super.onPause()
        representative.stopGettingUpdates()
    }
}

interface DashboardObserver: UiObserver<PremiumDashboardUiState> {
    override fun isEmpty(): Boolean = false
}