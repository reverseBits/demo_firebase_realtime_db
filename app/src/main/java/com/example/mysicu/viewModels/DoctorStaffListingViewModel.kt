package com.example.mysicu.viewModels

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.example.mysicu.models.HomeMenuData
import com.example.mysicu.models.StaffModel
import com.example.mysicu.repository.FireBaseRepo
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class DoctorStaffListingViewModel : ViewModel() {

    private val firebaseRepository: FireBaseRepo = FireBaseRepo()

    private val doctorStaffListingMutableLiveData = MutableLiveData<List<StaffModel>>()


    init {
        getDoctorStaffList()
    }


    fun getDoctorStaffListLiveData(): LiveData<List<StaffModel>> {

        return doctorStaffListingMutableLiveData
    }

    fun getDoctorStaffList() {
        firebaseRepository.getDoctorStaffData().observeForever(object : Observer<List<StaffModel>> {
            override fun onChanged(value: List<StaffModel>) {
                doctorStaffListingMutableLiveData.postValue(value)
            }

        })
    }
}