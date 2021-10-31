package com.tejas.stackoverflow.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tejas.stackoverflow.model.Question
import com.tejas.stackoverflow.model.QuestionListResponse
import com.tejas.stackoverflow.repository.MainRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainViewModel @Inject constructor(
    private val mainRepository: MainRepository
) : ViewModel() {
    private var liveData = MutableLiveData<QuestionListResponse>()


    fun getQuestionList(): LiveData<QuestionListResponse> {
        viewModelScope.launch(Dispatchers.IO) {
            mainRepository.getQuestionList().collect {
                liveData.postValue(addAdvertisements(it))
            }
        }
        return liveData
    }

    private fun addAdvertisements(it: QuestionListResponse): QuestionListResponse {
        val list = mutableListOf<Question?>()
        for (i in it.items.indices) {
            if (i % 3 == 0 && i != 0)
                list.add(null)
            list.add(it.items[i])
        }
        it.items = list
        return it
    }
}