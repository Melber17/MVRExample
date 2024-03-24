package com.melber17.project999.subscription.progress.presentation

import android.content.Context
import android.os.Parcelable
import android.util.AttributeSet
import android.view.View
import android.widget.ProgressBar
import com.melber17.project999.core.HideAndShow
import com.melber17.project999.core.ProvideRepresentative
import com.melber17.project999.core.UiObserver

class SubscriptionProgressBar : SubscriptionProgressActions, ProgressBar {


    private val representative: SubscriptionProgressRepresentative by lazy {
        (context.applicationContext as ProvideRepresentative).provideRepresentative(
            SubscriptionProgressRepresentative::class.java
        )
    }
    private val observer: UiObserver<SubscriptionProgressUiState> =
        object : UiObserver<SubscriptionProgressUiState> {
            override fun update(data: SubscriptionProgressUiState) {
                data.show(this@SubscriptionProgressBar)
                data.observed(representative)
            }

            override fun isEmpty(): Boolean = false
        }


    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context) : super(context)

    override fun resume() {
        representative.startGettingUpdates(observer)
    }


    override fun hide() {
        visibility = View.GONE
    }


    override fun init(firstRun: Boolean) = representative.init(firstRun)


    override fun pause() = representative.stopGettingUpdates()

    override fun subscribe() = representative.subscribe()


    override fun onSaveInstanceState(): Parcelable? {
        val onSaveInstanceState = super.onSaveInstanceState()
        return onSaveInstanceState?.let {
            val restoreState = SubscriptionProgressSavedState(it)
            restoreState.save(this)
            representative.save(restoreState)
            return restoreState
        }
    }

    override fun onRestoreInstanceState(state: Parcelable?) {
        val restoreState = state as SubscriptionProgressSavedState
        super.onRestoreInstanceState(restoreState.superState)
        restoreState.restore(this)
        representative.restore(restoreState)
    }


    override fun show() {
        visibility = View.VISIBLE
    }

    override fun comeback(data: ComeBack<Boolean>) {
        representative.comeback(data)
    }
}

interface ComeBack<T> {
    fun comeback(data: T)
}

interface SubscriptionProgressActions : Subscribe, HideAndShow, ComeBack<ComeBack<Boolean>>, Init {
    fun resume()

    fun pause()
}

interface Subscribe {
    fun subscribe()
}

interface Init {
    fun init(firstRun: Boolean)
}