package com.melber17.project999.core

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.melber17.project999.main.Navigation

interface ProvideSharedPreferences {
    fun sharedPreferences(): SharedPreferences
}

interface ProvideNavigation {
    fun navigation(): Navigation.Mutable
}

interface ProvideRunAsync {
    fun runAsync(): RunAsync
}

interface Core : ProvideNavigation, ProvideSharedPreferences, ProvideRunAsync {
    class Base(private val context: Context) : Core {
        private val navigation = Navigation.Base()
        override fun navigation(): Navigation.Mutable = navigation

        override fun sharedPreferences(): SharedPreferences {
            return context.getSharedPreferences("project999", Context.MODE_PRIVATE)
        }

        override fun runAsync(): RunAsync = RunAsync.Base(DispatchersList.Base())
    }
}