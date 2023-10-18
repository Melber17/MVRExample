package com.melber17.project999.main

import android.content.SharedPreferences

interface UserPremiumCache {
    interface Save {
        fun saveUserPremium()
    }

    interface Read {
        fun isUserPremium(): Boolean
    }

    interface Mutable : Save, Read

    class Base(
        private val sharedPreferences: SharedPreferences,
        private val key: String = "isUserPremium"
    ) : Mutable {
        override fun saveUserPremium() {
            val edit = sharedPreferences.edit()
            edit.putBoolean(key, true)
            edit.commit()
        }

        override fun isUserPremium(): Boolean {

            return sharedPreferences.getBoolean(key, false)
        }


    }
}