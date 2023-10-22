package com.melber17.project999.dashboard

import com.melber17.project999.core.ClearRepresentative
import com.melber17.project999.core.Representative
import com.melber17.project999.core.UiObserver
import com.melber17.project999.main.Navigation
import com.melber17.project999.main.Screen
import com.melber17.project999.subscription.presentation.SubscriptionScreen
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class DashboardRepresentativeTest {
    private lateinit var baseRepresentative: DashboardRepresentative
    private lateinit var premiumRepresentative: DashboardRepresentative
    private lateinit var observable: FakeObservable
    private lateinit var navigation: FakeNavigation
    private lateinit var clear: FakeClear

    @Before
    fun setup() {
        observable = FakeObservable.Base()
        navigation = FakeNavigation.Base()
        clear = FakeClear.Base()

        baseRepresentative = DashboardRepresentative.Base(navigation, clear)
        premiumRepresentative = DashboardRepresentative.Premium(observable)
    }

    @Test
    fun test_base_scenario() {
        baseRepresentative.play()
        clear.checkClearCalledWith(DashboardRepresentative::class.java)
        navigation.checkActiveScreen(SubscriptionScreen)
    }

    @Test
    fun test_premium_scenario() {
        val callback = object : DashboardObserver {
            override fun update(data: PremiumDashboardUiState) = Unit
        }
        premiumRepresentative.startGettingUpdates(callback)
        observable.checkUpdateObserverCalled(callback)
        observable.checkUiState(PremiumDashboardUiState.Empty)
        premiumRepresentative.play()
        observable.checkUiState(PremiumDashboardUiState.Playing)

        premiumRepresentative.observed()
        observable.checkClearCalled()

        premiumRepresentative.stopGettingUpdates()
        observable.checkUpdateObserverCalled(EmptyDashboardObserver)
    }
}

private interface FakeNavigation: Navigation.Update {
    fun checkActiveScreen(screen: Screen)

    class Base(): FakeNavigation {
        private var activeScreen: Screen = Screen.Empty
        override fun checkActiveScreen(screen: Screen) {
          assertEquals(screen, activeScreen)
        }
        override fun update(data: Screen) {
            activeScreen = data
        }

    }
}

private interface FakeClear : ClearRepresentative {
    fun checkClearCalledWith(clasz: Class<out Representative<*>>)
    class Base : FakeClear {
        private var clearCalledClass: Class<out Representative<*>>? = null

        override fun checkClearCalledWith(clasz: Class<out Representative<*>>) {
            assertEquals(clasz, clearCalledClass)
        }

        override fun clear(clasz: Class<out Representative<*>>) {
            clearCalledClass = clasz
        }

    }
}


private interface FakeObservable : PremiumDashboardObservable {
    fun checkUpdateCalledCount(times: Int)
    fun checkClearCalled()
    fun checkUiState(uiState: PremiumDashboardUiState)
    fun checkUpdateObserverCalled(observer: DashboardObserver)


    class Base() : FakeObservable {
        private var isClearCalled = false
        private var cache: PremiumDashboardUiState = PremiumDashboardUiState.Empty
        private var updateCalledCount = 0
        private var observerCached: UiObserver<PremiumDashboardUiState> =
            object : DashboardObserver {
                override fun update(data: PremiumDashboardUiState) = Unit
            }

        override fun checkUpdateCalledCount(times: Int) {
            assertEquals(times, updateCalledCount)
        }

        override fun checkClearCalled() {
            assertEquals(true, isClearCalled)
            isClearCalled = false
        }

        override fun checkUiState(uiState: PremiumDashboardUiState) {
            assertEquals(uiState, cache)
        }

        override fun checkUpdateObserverCalled(observer: DashboardObserver) {
            assertEquals(observerCached, observer)
        }


        override fun clear() {
            cache = PremiumDashboardUiState.Empty
            isClearCalled = true
        }


        override fun update(data: PremiumDashboardUiState) {
            cache = data
            updateCalledCount++
        }

        override fun updateObserver(uiObserver: UiObserver<PremiumDashboardUiState>) {
            observerCached = uiObserver
        }
    }
}