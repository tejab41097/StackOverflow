package com.tejas.stackoverflow.api

import com.tejas.stackoverflow.BuildConfig
import com.tejas.stackoverflow.model.QuestionListResponse
import retrofit2.Response
import retrofit2.http.GET

interface ApiService {

    @GET(BuildConfig.BASE_URL + "questions?key=ZiXCZbWaOwnDgpVT9Hx8IA((&order=desc&sort=activity&site=stackoverflow")
    suspend fun getQuestions(): QuestionListResponse
}