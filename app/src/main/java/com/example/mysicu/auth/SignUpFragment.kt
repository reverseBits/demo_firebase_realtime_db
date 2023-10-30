package com.example.mysicu.auth

import android.app.Activity
import android.app.DatePickerDialog
import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.example.mysicu.R
import com.example.mysicu.databinding.FragmentSignUpBinding
import com.example.mysicu.models.AdminData
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.util.Calendar

class SignUpFragment : Fragment() {

    lateinit var mBinding: FragmentSignUpBinding
    lateinit var auth: FirebaseAuth
    lateinit var database: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_sign_up, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        database = FirebaseDatabase.getInstance().getReference().child("Admin")
        auth = FirebaseAuth.getInstance()


        mBinding.edtDob.setOnClickListener {

            val c = Calendar.getInstance()


            val year = c[Calendar.YEAR]
            val month = c[Calendar.MONTH]
            val day = c[Calendar.DAY_OF_MONTH]


            val datePickerDialog = DatePickerDialog(
                requireContext(),
                { view, year, monthOfYear, dayOfMonth ->
                    mBinding.edtDob.setText(dayOfMonth.toString() + "/" + (monthOfYear + 1) + "/" + year)
                },
                year, month, day
            )

            datePickerDialog.show()


        }

        mBinding.tvLogin.setOnClickListener {

        }

        mBinding.btnSignUp.setOnClickListener {
            validation()
        }

    }


    private fun addData() {
        val name = mBinding.edtName.text.toString()
        val email = mBinding.edtEmail.text.toString()
        val phoneNo = mBinding.edtPhoneNo.text.toString()
        val dob = mBinding.edtDob.text.toString()
        val qualification = mBinding.edtQualification.text.toString()
        val exp = mBinding.edtExperience.text.toString()
        val place = mBinding.edtPlace.text.toString()

        val user = FirebaseAuth.getInstance().currentUser

        var userId = user?.uid

        val admin = AdminData(name, email, phoneNo, dob, qualification, exp, place,null)

        database.child(userId.toString()).setValue(admin).addOnSuccessListener {
            Toast.makeText(requireContext(), "Data Successfully Add", Toast.LENGTH_SHORT).show()
        }.addOnFailureListener {
            Toast.makeText(requireContext(), "data not save", Toast.LENGTH_SHORT).show()
        }
    }

    private fun validation() {

        val name = mBinding.edtName.text.toString()
        val email = mBinding.edtEmail.text.toString()
        val phoneNo = mBinding.edtPhoneNo.text.toString()
        val password = mBinding.edtPassword.text.toString()
        val confirmPassword = mBinding.edtPassword.text.toString()

        if (name.isNullOrEmpty()) {
            mBinding.edtName.error = "plz Enter Name"
        } else if (email.isNullOrEmpty()) {
            mBinding.edtEmail.error = "plz Enter Email"
        } else if (!Patterns.EMAIL_ADDRESS.matcher(
                email.trim { it <= ' ' }).matches()
        ) {
            mBinding.edtEmail.error = "valid email address"
        } else if (phoneNo.isNullOrEmpty()) {
            mBinding.edtPhoneNo.error = "Please Enter MobileNo"
        } else if (!Patterns.PHONE.matcher(
                phoneNo.trim { it <= ' ' })
                .matches() || phoneNo
                .trim { it <= ' ' }.length < 10 || phoneNo
                .trim { it <= ' ' }.length > 10
        ) {
            mBinding.edtPhoneNo.error = "valid Mobile No"
        } else if (password.isNullOrEmpty()) {
            mBinding.edtPassword.error = "plz Enter Password"
        } else if (confirmPassword.isNullOrEmpty()) {
            mBinding.edtConfirmPassword.error = "plz Enter Confirm Password"
        } else if (password.length <= 6) {
            mBinding.edtPassword.error = "plz Enter Minimum 6 Chr"
        } else if (confirmPassword.length <= 6) {
            mBinding.edtConfirmPassword.error = "plz Enter Minimum 6 Chr"
        } else if (password != confirmPassword) {
            mBinding.edtConfirmPassword.error = "Password is Not Match"
        } else {
            auth.createUserWithEmailAndPassword(email, password).addOnSuccessListener {
                val user = auth.currentUser
                user!!.sendEmailVerification().addOnSuccessListener {
                    Toast.makeText(requireContext(), "Send", Toast.LENGTH_SHORT).show()
                    addData()
                    findNavController().navigate(SignUpFragmentDirections.actionSignUpFragmentToLoginFragment())
                    Toast.makeText(requireContext(), "Successful", Toast.LENGTH_SHORT).show()
                }.addOnFailureListener {
                    Toast.makeText(requireContext(), "Failed", Toast.LENGTH_SHORT).show()
                }

            }.addOnFailureListener {
                Log.d(TAG, "what fail${it.message}")
                Toast.makeText(requireContext(), "Failed", Toast.LENGTH_SHORT).show()
            }
        }
    }

}