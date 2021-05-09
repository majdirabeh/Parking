package fr.com.majdi.parking

import android.app.Application
import android.content.Context
import com.mapbox.mapboxsdk.Mapbox
import fr.com.majdi.parking.module.mainModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

/**
 * Created by Majdi RABEH on 09/05/2021.
 * Email m.rabeh.majdi@gmail.com
 */
class AppParking : Application() {

    override fun onCreate() {
        super.onCreate()
        initMapBox()
        startKoin {
            androidLogger()
            androidContext(this@AppParking)
            modules(listOf(mainModule))
        }
    }

    private fun initMapBox() {
        Mapbox.getInstance(applicationContext, getString(R.string.mapbox_access_token))
    }
}