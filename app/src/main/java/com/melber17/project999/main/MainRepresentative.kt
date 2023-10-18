package com.melber17.project999.main

import com.melber17.project999.core.Representative
import com.melber17.project999.core.UiObserver
import com.melber17.project999.dashboard.DashboardScreen

interface MainRepresentative: Representative<Screen> {

    fun observed()
    fun startAsync()

    fun showDashboard(firstTime: Boolean)

    class Base(private val navigation: Navigation.Mutable) : MainRepresentative {
        private val thread = Thread {
            Thread.sleep(3000)
        }
        override fun startGettingUpdates(callback: UiObserver<Screen>) =
            navigation.updateObserver(callback)

        override fun stopGettingUpdates() = navigation.updateObserver()
        override fun observed() = navigation.clear()

        override fun startAsync() {
            thread.start()
        }

        override fun showDashboard(firstTime: Boolean) {
            if (firstTime) {
                navigation.update(DashboardScreen)
            }
        }
    }
}

