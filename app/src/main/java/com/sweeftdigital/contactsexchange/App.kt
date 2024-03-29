package com.sweeftdigital.contactsexchange

import android.app.Application
import com.sweeftdigital.contactsexchange.di.module.LOCAL_STORAGE_MODULE
import com.sweeftdigital.contactsexchange.di.module.REPOSITORY_MODULE
import com.sweeftdigital.contactsexchange.di.module.USE_CASES_MODULE
import com.sweeftdigital.contactsexchange.di.module.UTILS_MODULE
import com.sweeftdigital.contactsexchange.di.module.VIEW_MODELS_MODULE
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(applicationContext)
            modules(
                LOCAL_STORAGE_MODULE,
                REPOSITORY_MODULE,
                USE_CASES_MODULE,
                VIEW_MODELS_MODULE,
                UTILS_MODULE
            )
        }
    }
}