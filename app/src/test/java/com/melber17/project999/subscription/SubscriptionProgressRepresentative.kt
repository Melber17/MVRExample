package com.melber17.project999.subscription

import com.melber17.project999.core.RunAsync
import com.melber17.project999.core.UiObserver
import com.melber17.project999.subscription.progress.presentation.SaveAndRestoreSubscriptionUiState
import com.melber17.project999.subscription.progress.presentation.SubscriptionProgressObservable
import com.melber17.project999.subscription.progress.presentation.SubscriptionProgressRepresentative
import com.melber17.project999.subscription.progress.presentation.SubscriptionProgressUiState
import com.melber17.project999.subscription.screen.presentation.SubscriptionObservable
import com.melber17.project999.subscription.screen.presentation.SubscriptionObserver
import com.melber17.project999.subscription.screen.presentation.SubscriptionUiState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.runBlocking
import org.junit.Before

class SubscriptionProgressRepresentativeTest {
    private lateinit var representative: SubscriptionProgressRepresentative
    private lateinit var observable: FakeObservable
//    private lateinit var clear: FakeClear
    private lateinit var interactor: FakeInteractor
//    private lateinit var navigation: FakeNavigation
    private lateinit var handleDeath: FakeHandleDeath
    private lateinit var runAsync: FakeRunAsync

    @Before
    fun setup() {
        runAsync = FakeRunAsync.Base()
        handleDeath = FakeHandleDeath.Base()
        interactor = FakeInteractor.Base()

        representative = SubscriptionProgressRepresentative.Base(
            runAsync,
            handleDeath,
            interactor,


        )
    }
}


private interface FakeSubscriptionProgressObservable: SubscriptionProgressObservable {


    class Base: FakeSubscriptionProgressObservable {
        private var clearCalled = false
        private var cache: SubscriptionProgressUiState = SubscriptionProgressUiState.Hide
        private var updateCalledCount = 0
        override fun save(saveState: SaveAndRestoreSubscriptionUiState.Save) {
           saveState.save(cache)
        }

        override fun clear() {
            TODO("Not yet implemented")
        }

        override fun update(data: SubscriptionProgressUiState) {
            TODO("Not yet implemented")
        }

        private var observerCached: UiObserver<SubscriptionUiState> =
            object : SubscriptionObservable {
                override fun update(data: SubscriptionUiState) = Unit
            }

        override fun updateObserver(uiObserver: UiObserver<SubscriptionProgressUiState>) {
            TODO("Not yet implemented")
        }

        override fun canGoBack(): Boolean {
            TODO("Not yet implemented")
        }

    }
}