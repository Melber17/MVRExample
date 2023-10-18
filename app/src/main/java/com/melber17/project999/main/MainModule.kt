package com.melber17.project999.main

import com.melber17.project999.core.Core
import com.melber17.project999.core.Module

class MainModule(private val core: Core): Module<MainRepresentative> {
    override fun representative(): MainRepresentative {
        return MainRepresentative.Base(core.navigation())
    }
}