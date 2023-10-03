package com.example.mysicu.repository

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class FireBaseRepo {

    private var mDatabase: DatabaseReference


    init {
        mDatabase = FirebaseDatabase.getInstance().getReference()
    }


}