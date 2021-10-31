package com.tejas.stackoverflow.di

import android.content.Context
import androidx.annotation.NonNull
import com.tejas.stackoverflow.api.ApiClient
import com.tejas.stackoverflow.database.DatabaseHelper
import dagger.Module
import dagger.Provides

@Module
class ApplicationModule {

    @Provides
    fun provideDbHelper(@NonNull context: Context): DatabaseHelper {
        return DatabaseHelper(context)
    }


    @Provides
    fun provideApiClient() = ApiClient()
}