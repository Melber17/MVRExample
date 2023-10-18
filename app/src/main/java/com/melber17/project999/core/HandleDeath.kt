package com.melber17.project999.core

interface HandleDeath {
    fun firstOpening()

    fun isDeathHappened(): Boolean

    fun deathHandled()

    class Base: HandleDeath {
        private var deathHappened = true

        override fun firstOpening() {
           deathHappened = false
        }

        override fun isDeathHappened(): Boolean {
           return deathHappened
        }

        override fun deathHandled() {
            deathHappened = false
        }
    }
}