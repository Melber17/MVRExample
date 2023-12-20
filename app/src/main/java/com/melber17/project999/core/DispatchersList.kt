package com.melber17.project999.core

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

interface DispatchersList {
    fun background(): CoroutineDispatcher
    fun ui(): CoroutineDispatcher

    class Base: DispatchersList {
        override fun background(): CoroutineDispatcher = Dispatchers.IO
        override fun ui(): CoroutineDispatcher = Dispatchers.Main
    }
}