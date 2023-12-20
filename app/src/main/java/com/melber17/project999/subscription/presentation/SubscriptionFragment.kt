package com.melber17.project999.subscription.presentation

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import com.melber17.project999.R
import com.melber17.project999.core.CustomButton
import com.melber17.project999.core.CustomProgressBar
import com.melber17.project999.core.IsEmpty
import com.melber17.project999.core.UiObserver
import com.melber17.project999.main.BaseFragment

class SubscriptionFragment : BaseFragment<SubscriptionRepresentative>(R.layout.fragment_subscription) {
    private lateinit var observer: UiObserver<SubscriptionUiState>

    override val clasz =  SubscriptionRepresentative::class.java


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val subscriptionButton = view.findViewById<CustomButton>(R.id.subscribe)
        val progressBar = view.findViewById<CustomProgressBar>(R.id.progressBar)
        val finishButton = view.findViewById<CustomButton>(R.id.finishButton)

        activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner, object: OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                representative.comeback()
            }

        })

        subscriptionButton.setOnClickListener {
            representative.subscribe()
        }
        finishButton.setOnClickListener {
            representative.finish()
        }

        observer = object : SubscriptionObserver {
            override fun update(data: SubscriptionUiState) {
                data.observed(representative)
                data.show(subscriptionButton, progressBar, finishButton)
            }
        }
        representative.init(SaveAndRestoreSubscriptionUiState.Base(savedInstanceState))
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        representative.save(SaveAndRestoreSubscriptionUiState.Base(outState))
    }
    override fun onResume() {
        super.onResume()
        representative.startGettingUpdates(observer)
    }

    override fun onPause() {
        super.onPause()
        representative.stopGettingUpdates()
    }

}

interface SubscriptionObserver: UiObserver<SubscriptionUiState>, IsEmpty {
    override fun isEmpty(): Boolean = false
}