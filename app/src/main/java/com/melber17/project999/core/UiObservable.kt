package com.melber17.project999.core

import android.annotation.SuppressLint
import androidx.annotation.MainThread

interface UiObservable<T : Any> : UiUpdate<T>, UpdateObserver<T> {

    fun clear()

    abstract class Single<T : Any>(
        private val empty: T
    ) : UiObservable<T> {


        @Volatile
        protected var cache: T = empty

        @Volatile
        private var observer: UiObserver<T> = UiObserver.Empty()


        override fun clear() {
            cache = empty
        }

        @SuppressLint("SuspiciousIndentation")
        @MainThread
        override fun updateObserver(uiObserver: UiObserver<T>) {
            observer = uiObserver
            observer.update(cache)
        }

        override fun update(data: T) = synchronized(lock) {
            cache = data
            observer.update(cache)
        }
    }

    companion object {
        private val lock = Object()
    }
}

interface UiUpdate<T : Any> {
    fun update(data: T)
}

interface UpdateObserver<T : Any> {
    fun updateObserver(uiObserver: UiObserver<T> = UiObserver.Empty())
}

interface UiObserver<T : Any> : UiUpdate<T>, IsEmpty {

    class Empty<T : Any> : UiObserver<T> {
        override fun isEmpty(): Boolean = true
        override fun update(data: T) = Unit
    }
}