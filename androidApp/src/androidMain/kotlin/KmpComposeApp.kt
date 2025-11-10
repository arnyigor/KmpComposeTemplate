package com.arny.kmpprogress

import android.app.Application
import di.androidModules
import di.commonModules
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class KmpComposeApp: Application() {

    override fun onCreate() {
        super.onCreate()
        AndroidPlatform.init(this)
        startKoin {
            androidContext(this@KmpComposeApp)
            modules(commonModules + androidModules)
        }
    }
}