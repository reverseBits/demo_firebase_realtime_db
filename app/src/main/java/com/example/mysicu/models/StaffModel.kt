package com.example.mysicu.models

import android.net.Uri
import java.io.Serializable

data class StaffModel(
    val staffId: String? = null,
    val name: String? = null,
    val emailId: String? = null,
    val phoneNo: String? = null,
    val dob: String? = null,
    val doj: String? = null,
    val type: String? = null,
    val qualification: String? = null,
    val experience: String? = null,
    val place: String? = null,
    val noOfPatients: String? = null,
    val image: String? = null
) : Serializable
