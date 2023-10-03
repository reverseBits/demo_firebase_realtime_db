package com.example.mysicu

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mysicu.models.StaffModel

class MainViewModel : ViewModel() {

    val staffDetailsMutableLiveData = MutableLiveData<StaffModel>()
}