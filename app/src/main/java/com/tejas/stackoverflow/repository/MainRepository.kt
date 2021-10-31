package com.tejas.stackoverflow.repository

import com.tejas.stackoverflow.api.ApiService
import com.tejas.stackoverflow.api.RequestHandler
import com.tejas.stackoverflow.database.DatabaseHelper
import com.tejas.stackoverflow.model.QuestionListResponse
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class MainRepository @Inject constructor(
    private val apiService: ApiService,
    private val databaseHelper: DatabaseHelper
) : RequestHandler() {

    fun getQuestionList() = flow {
        val result = apiRequest { apiService.getQuestions() }
        if (result.successBody != null) {
            val questionList = (result.successBody as QuestionListResponse).items
            emit(questionList)
            databaseHelper.getDatabase().getMainDao().insertAllQuestions(questionList)
        } else {
            emit(databaseHelper.getDatabase().getMainDao().getAllQuestions())
        }
    }
}