package com.tejas.stackoverflow.di

import android.app.Application

class StackOverflowApp : Application() {

    lateinit var applicationComponent: ApplicationComponent

    override fun onCreate() {
        applicationComponent =
            DaggerApplicationComponent.factory().create(applicationContext)
        super.onCreate()
    }
}