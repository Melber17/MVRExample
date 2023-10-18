package com.melber17.project999.core

interface Module< T: Representative<*>> {
    fun representative(): T
}