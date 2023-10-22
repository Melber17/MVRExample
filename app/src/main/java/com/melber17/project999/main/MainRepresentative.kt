package com.melber17.project999.main

import com.melber17.project999.core.Representative
import com.melber17.project999.core.UiObserver
import com.melber17.project999.dashboard.DashboardScreen

interface MainRepresentative: Representative<Screen> {

    fun observed()

    fun showDashboard(firstTime: Boolean)

    class Base(private val navigation: Navigation.Mutable) : MainRepresentative {
        override fun startGettingUpdates(callback: UiObserver<Screen>) =
            navigation.updateObserver(callback)

        override fun stopGettingUpdates() = navigation.updateObserver()
        override fun observed() = navigation.clear()

        override fun showDashboard(firstTime: Boolean) {
            if (firstTime) {
                navigation.update(DashboardScreen)
            }
        }
    }
}

