package com.example.mysicu.fragment

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mysicu.models.StaffModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class StaffListingViewModel : ViewModel() {

    private val dbRef = FirebaseDatabase.getInstance().getReference("Stafflist")

    private val staffListingMutableLiveData = MutableLiveData<List<StaffModel>>()

    val staffListingLiveData : LiveData<List<StaffModel>> = staffListingMutableLiveData

    private fun getStaffList() {

        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                val staffList = ArrayList<StaffModel>()

                if (snapshot.exists()) {

                    for (staffsnap in snapshot.children) {

                        val staff = staffsnap.getValue(StaffModel::class.java)
                        staffList.add(staff!!)
                    }
                }

                staffListingMutableLiveData.value = staffList
            }

            override fun onCancelled(error: DatabaseError) {
                staffListingMutableLiveData.value = emptyList()
            }
        })
    }

    init {
        getStaffList()
    }

}