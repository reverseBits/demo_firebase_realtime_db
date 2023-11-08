package com.example.mysicu.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mysicu.models.StaffModel
import com.google.firebase.database.FirebaseDatabase

class UpdatingDoctorStaffViewModel: ViewModel() {

    private val dbRef = FirebaseDatabase.getInstance().getReference("DoctorList")

    private val updatingDoctorStaffMutableLiveData = MutableLiveData<StaffModel?>()
    val updatingDoctorStaffLiveData : LiveData<StaffModel?> = updatingDoctorStaffMutableLiveData


    fun updateDetails(staffModel: StaffModel) {
        staffModel.staffId?.let { staffId ->
            dbRef.child(staffId).setValue(staffModel).addOnSuccessListener {
                updatingDoctorStaffMutableLiveData.value = staffModel
            }.addOnFailureListener {
                updatingDoctorStaffMutableLiveData.value = null
            }
        }
    }

}
