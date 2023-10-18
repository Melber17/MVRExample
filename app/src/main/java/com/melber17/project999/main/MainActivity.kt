package com.melber17.project999.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.melber17.project999.core.App
import com.melber17.project999.R
import com.melber17.project999.core.ProvideRepresentative
import com.melber17.project999.core.Representative
import com.melber17.project999.core.UiObserver

class MainActivity : AppCompatActivity(), ProvideRepresentative {
    private lateinit var representative: MainRepresentative
    private lateinit var activityCallback: ActivityCallback

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        representative = provideRepresentative(MainRepresentative::class.java)

        activityCallback = object : ActivityCallback {
            override fun update(data: Screen) = runOnUiThread {
                data.show(supportFragmentManager, R.id.container)
                data.observed(representative)
            }
        }



        representative.showDashboard(savedInstanceState == null)
    }

    override fun onResume() {
        super.onResume()
        representative.startGettingUpdates(activityCallback)
    }

    override fun onPause() {
        super.onPause()
        representative.stopGettingUpdates()
    }

    override fun <T : Representative<*>> provideRepresentative(clasz: Class<T>): T =
        (application as ProvideRepresentative).provideRepresentative(clasz)


}

interface ActivityCallback : UiObserver<Screen> {
    override fun isEmpty() = false
}

