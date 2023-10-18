package com.melber17.project999.core

import android.app.Application

class App : Application(), ProvideRepresentative, ClearRepresentative {
    private lateinit var factory: ProvideRepresentative.Factory


    override fun onCreate() {
        super.onCreate()
        factory = ProvideRepresentative.Factory(ProvideRepresentative.CreateDependency(Core.Base(this), this))
    }

    override fun <T : Representative<*>> provideRepresentative(clasz: Class<T>): T = factory.provideRepresentative(clasz)

    override fun clear(clasz: Class<out Representative<*>>) {
        factory.clear(clasz)
    }
}