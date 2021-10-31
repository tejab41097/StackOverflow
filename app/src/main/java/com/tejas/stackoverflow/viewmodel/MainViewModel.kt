package com.tejas.stackoverflow.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tejas.stackoverflow.model.Question
import com.tejas.stackoverflow.repository.MainRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainViewModel @Inject constructor(
    private val mainRepository: MainRepository
) : ViewModel() {
    private var liveData = MutableLiveData<MutableList<Question?>>()
    private var selectedFilter = MutableLiveData<String>()
    private val filterListMutLiveData = MutableLiveData<MutableList<String>>()
    val filterListLiveData get() = filterListMutLiveData

    fun setSelectedFilter(text: String) {
        selectedFilter.postValue(text)
    }

    fun getSelectedFilter() = selectedFilter

    private fun setFilterList(list: MutableList<Question?>) {
        val tempFilterList = mutableListOf<String>()
        list.forEach {
            it?.tags?.forEach { tags ->
                if (!tempFilterList.contains(tags))
                    tags?.let { tag -> tempFilterList.add(tag) }
            }
        }

        filterListMutLiveData.postValue(tempFilterList)
    }

    fun getQuestionList(): LiveData<MutableList<Question?>> {
        viewModelScope.launch(Dispatchers.IO) {
            mainRepository.getQuestionList().collect {
                setFilterList(it)
                liveData.postValue(addAdvertisements(it))
            }
        }
        return liveData
    }

    private fun addAdvertisements(it: MutableList<Question?>): MutableList<Question?> {
        val list = mutableListOf<Question?>()
        for (i in it.indices) {
            if (i % 3 == 0 && i != 0)
                list.add(null)
            list.add(it[i])
        }
        return list
    }
}