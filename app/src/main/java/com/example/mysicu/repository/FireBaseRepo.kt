package com.example.mysicu.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.mysicu.models.HomeMenuData
import com.example.mysicu.models.StaffModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

private const val TAG = "FireBaseRepo"

class FireBaseRepo {

    private var mDatabase: DatabaseReference

    init {
        mDatabase = FirebaseDatabase.getInstance().getReference()
    }


    fun getHomeMenuData(): LiveData<List<HomeMenuData>> {
        val homeMenuMutableLiveData: MutableLiveData<List<HomeMenuData>> = MutableLiveData()
        val serviceList: ArrayList<HomeMenuData> = ArrayList()

        mDatabase.child("Home Menu").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                serviceList.clear()
                for (postSnapshot in snapshot.children) {
                    try {
                        val code: HomeMenuData = postSnapshot.getValue(HomeMenuData::class.java)!!
                        serviceList.add(code)
                    } catch (e: Exception) {
                    }
                }
                homeMenuMutableLiveData.postValue(serviceList)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e(TAG, "onCancelled: ${error.message}")
            }

        })
        return homeMenuMutableLiveData
    }


     fun getStaffData(): LiveData<List<StaffModel>> {
        val staffListingMutableLiveData: MutableLiveData<List<StaffModel>> = MutableLiveData()
        val staffList: ArrayList<StaffModel> = ArrayList()
        mDatabase.child("Stafflist").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {


                if (snapshot.exists()) {

                    for (staffsnap in snapshot.children) {

                        val staff = staffsnap.getValue(StaffModel::class.java)
                        staffList.add(staff!!)
                    }
                }

                staffListingMutableLiveData.postValue(staffList)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e(TAG, "onCancelled: ${error.message}")
            }
        })
        return staffListingMutableLiveData
    }


}