package com.tejas.stackoverflow.api

import com.google.gson.GsonBuilder
import com.tejas.stackoverflow.BuildConfig
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ApiClient @Inject constructor() {

    fun getRetrofitInstance(): Retrofit {
        val interceptor = HttpLoggingInterceptor()
        interceptor.apply { interceptor.level = HttpLoggingInterceptor.Level.BODY }
        val okHttpClient = OkHttpClient.Builder()
        okHttpClient.addInterceptor(interceptor)
        okHttpClient.connectTimeout(1, TimeUnit.MINUTES)
        okHttpClient.readTimeout(1, TimeUnit.MINUTES)
        okHttpClient.writeTimeout(1, TimeUnit.MINUTES)
        okHttpClient.addInterceptor(Interceptor { chain: Interceptor.Chain ->
            val request =
                chain.request().newBuilder()
                    .addHeader("Accept", "application/json")
                    .build()
            chain.proceed(request)
        })

        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(okHttpClient.build())
            .build()
    }
}