package com.melber17.project999.main

import com.melber17.project999.core.UiObserver
import com.melber17.project999.dashboard.DashboardScreen
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class MainRepresentativeTest {
    private lateinit var navigation: FakeNavigation
    private lateinit var representative: MainRepresentative

    @Before
    fun setup() {
        navigation = FakeNavigation.Base()
        representative = MainRepresentative.Base(navigation)
    }

    @Test
    fun test_first_entrance() {
        val callback = object: UiObserver<Screen> {
            override fun update(data: Screen) = Unit

            override fun isEmpty(): Boolean = true
        }

        representative.startGettingUpdates(callback)
        representative.showDashboard(true)
        navigation.checkActiveScreen(DashboardScreen)
        representative.stopGettingUpdates()
        representative.observed()
        navigation.checkActiveScreen(Screen.Empty)
        navigation.checkCachedUiObserver(callback)
    }

    @Test
    fun test_not_first_entrance() {
        val callback = object: UiObserver<Screen> {
            override fun update(data: Screen) = Unit

            override fun isEmpty(): Boolean = false
        }

        representative.startGettingUpdates(callback)
        representative.showDashboard(false)
        navigation.checkActiveScreen(Screen.Empty)

        representative.stopGettingUpdates()
        navigation.checkCachedUiObserver(callback)
    }
}


private interface FakeNavigation: Navigation.Mutable {
    fun checkActiveScreen(screen: Screen)
    fun checkCachedUiObserver(uiObserver: UiObserver<Screen>)

    class Base(): FakeNavigation {
        private var activeScreen: Screen = Screen.Empty
        private var cachedUiObserver: UiObserver<Screen> = UiObserver.Empty()
        override fun checkActiveScreen(screen: Screen) {
            assertEquals(screen, activeScreen)
        }

        override fun checkCachedUiObserver(uiObserver: UiObserver<Screen>) {
            assertNotEquals(cachedUiObserver, uiObserver)
        }

        override fun clear() {
            activeScreen = Screen.Empty
        }

        override fun update(data: Screen) {
            activeScreen = data
        }

        override fun updateObserver(uiObserver: UiObserver<Screen>) {
            cachedUiObserver = uiObserver
        }

    }
}