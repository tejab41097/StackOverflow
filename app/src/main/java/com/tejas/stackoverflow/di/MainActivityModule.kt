package com.tejas.stackoverflow.di

import com.tejas.stackoverflow.api.ApiClient
import com.tejas.stackoverflow.api.ApiService
import dagger.Module
import dagger.Provides

@Module(subcomponents = [MainActivityComponent::class])
class MainActivityModule {
    @Provides
    fun apiService(apiClient: ApiClient): ApiService {
        return apiClient.getRetrofitInstance().create(ApiService::class.java)
    }
}