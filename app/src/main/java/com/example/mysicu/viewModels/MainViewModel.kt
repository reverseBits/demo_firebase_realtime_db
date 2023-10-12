package com.example.mysicu.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mysicu.models.StaffModel

class MainViewModel : ViewModel() {

    val staffDetailsMutableLiveData = MutableLiveData<StaffModel>()
}