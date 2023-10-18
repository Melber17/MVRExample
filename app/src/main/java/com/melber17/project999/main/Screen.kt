package com.melber17.project999.main

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager

interface Screen {
    fun show(fragmentManager: FragmentManager, containerId: Int)
    fun observed(representative: MainRepresentative) = representative.observed()

    abstract class Add(private val fragmentClass: Class<out Fragment>) : Screen {
        override fun show(fragmentManager: FragmentManager, containerId: Int) {
            fragmentManager.beginTransaction().replace(containerId, fragmentClass.newInstance())
                .addToBackStack(fragmentClass.name)
                .commit()
        }
    }

    abstract class Replace(private val fragmentClass: Class<out Fragment>) : Screen {
        override fun show(fragmentManager: FragmentManager, containerId: Int) {
            fragmentManager.beginTransaction().replace(containerId, fragmentClass.newInstance())
                .commit()
        }
    }

    object Pop: Screen {
        override fun show(fragmentManager: FragmentManager, containerId: Int) {
          fragmentManager.popBackStack()
        }
    }

    object Empty: Screen {
        override fun show(fragmentManager: FragmentManager, containerId: Int) = Unit
    }
}