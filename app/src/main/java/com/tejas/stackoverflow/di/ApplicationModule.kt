package com.tejas.stackoverflow.di

import com.tejas.stackoverflow.api.ApiClient
import dagger.Module
import dagger.Provides

@Module
class ApplicationModule {
    @Provides
    fun provideApiClient() = ApiClient()
}