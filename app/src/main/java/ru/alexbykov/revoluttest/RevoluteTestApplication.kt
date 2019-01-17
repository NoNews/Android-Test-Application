package ru.alexbykov.revoluttest

import android.app.Application
import ru.alexbykov.revoluttest.common.di.Injector

class RevoluteTestApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        Injector.initAppComponent(this)
    }

}