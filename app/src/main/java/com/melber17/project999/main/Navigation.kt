package com.melber17.project999.main

import com.melber17.project999.core.UiObservable
import com.melber17.project999.core.UiUpdate
import com.melber17.project999.core.UpdateObserver

interface Navigation {
    interface Update: UiUpdate<Screen>

    interface Observe: UpdateObserver<Screen>

    interface Mutable: Update, Observe {
        fun clear()
    }

    class Base: UiObservable.Base<Screen>(Screen.Empty), Mutable
}