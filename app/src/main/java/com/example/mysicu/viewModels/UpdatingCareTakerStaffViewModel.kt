package com.example.mysicu.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mysicu.models.StaffModel
import com.google.firebase.database.FirebaseDatabase

class UpdatingCareTakerStaffViewModel: ViewModel() {

    private val dbRef = FirebaseDatabase.getInstance().getReference("caretakerList")

    private val updatingCareTakerStaffMutableLiveData = MutableLiveData<StaffModel?>()
    val updatingCareTakerStaffLiveData : LiveData<StaffModel?> = updatingCareTakerStaffMutableLiveData


    fun updateCareTakerDetails(staffModel: StaffModel) {
        staffModel.staffId?.let { staffId ->
            dbRef.child(staffId).setValue(staffModel).addOnSuccessListener {
                updatingCareTakerStaffMutableLiveData.value = staffModel
            }.addOnFailureListener {
                updatingCareTakerStaffMutableLiveData.value = null
            }
        }
    }

}
