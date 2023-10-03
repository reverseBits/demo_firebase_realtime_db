package com.example.mysicu.fragment

import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.fragment.findNavController
import com.example.mysicu.models.StaffModel
import com.google.firebase.database.FirebaseDatabase

class UpdatingStaffViewModel: ViewModel() {

    private val dbRef = FirebaseDatabase.getInstance().getReference("Stafflist")

    private val updatingStaffMutableLiveData = MutableLiveData<StaffModel?>()
    val updatingStaffLiveData : LiveData<StaffModel?> = updatingStaffMutableLiveData


    fun updateDetails(staffModel: StaffModel) {
        staffModel.staffId?.let { staffId ->
            dbRef.child(staffId).setValue(staffModel).addOnSuccessListener {
                updatingStaffMutableLiveData.value = staffModel
            }.addOnFailureListener {
                updatingStaffMutableLiveData.value = null
            }
        }
    }

}
