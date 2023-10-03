package com.example.mysicu.models

import java.io.Serializable

data class StaffModel(val staffId:String? = null,
                      val name:String? = null,
                      val empId:String? = null,
                      val age:String? = null,
                      val dob:String? = null,
                      val doj:String? = null,
                      val place:String? = null,
                      val experience:String? = null) : Serializable
