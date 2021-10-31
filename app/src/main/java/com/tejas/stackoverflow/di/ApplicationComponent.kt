package com.tejas.stackoverflow.di

import android.content.Context
import dagger.BindsInstance
import dagger.Component

@Component(
    modules = [ApplicationModule::class, MainActivityModule::class]
)
interface ApplicationComponent {

    fun mainActivityComponent(): MainActivityComponent.Factory

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance context: Context): ApplicationComponent
    }
}