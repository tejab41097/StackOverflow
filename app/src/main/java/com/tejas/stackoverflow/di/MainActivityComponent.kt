package com.tejas.stackoverflow.di

import com.tejas.stackoverflow.ui.MainActivity
import dagger.Subcomponent

@Subcomponent
interface MainActivityComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(): MainActivityComponent
    }

    fun inject(mainActivity: MainActivity)

}