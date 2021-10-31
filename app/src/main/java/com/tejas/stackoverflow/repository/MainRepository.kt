package com.tejas.stackoverflow.repository

import com.tejas.stackoverflow.api.ApiService
import com.tejas.stackoverflow.api.RequestHandler
import com.tejas.stackoverflow.model.QuestionListResponse
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class MainRepository @Inject constructor(
    private val apiService: ApiService
) : RequestHandler() {

    fun getQuestionList() = flow {
        val result = apiRequest { apiService.getQuestions() }
        if (result.successBody != null) {
            emit(result.successBody as QuestionListResponse)

        } else {

        }
    }
}