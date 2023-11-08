package com.example.mysicu.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.example.mysicu.models.HomeMenuData
import com.example.mysicu.repository.FireBaseRepo

class HomeViewModels() : ViewModel() {

    private val firebaseRepository: FireBaseRepo = FireBaseRepo()
    private val homeMenuMutableLiveData: MutableLiveData<List<HomeMenuData>> = MutableLiveData()

    init {
        getHomeMenuData()
    }

    fun getHomeMenuDataData(): LiveData<List<HomeMenuData>> {

        return homeMenuMutableLiveData
    }

    fun getHomeMenuData() {
        firebaseRepository.getHomeMenuData().observeForever(object : Observer<List<HomeMenuData>> {
            override fun onChanged(value: List<HomeMenuData>) {
                homeMenuMutableLiveData.postValue(value)
            }

        })
    }

}