package com.example.mysicu.fragment.Caretaker

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.example.mysicu.R
import com.example.mysicu.databinding.FragmentAddCartakerStaffBinding
import com.example.mysicu.models.StaffModel
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import java.util.Calendar

class AddCartakerStaffFragment : Fragment() {

    lateinit var mBinding: FragmentAddCartakerStaffBinding
    lateinit var dbRef: DatabaseReference
    private val navController: NavController by lazy {
        Navigation.findNavController(mBinding.root)
    }

    private var storageRef = Firebase.storage.reference


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        mBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_add_cartaker_staff,
            container,
            false
        )
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dbRef = FirebaseDatabase.getInstance().getReference("caretakerList")
        storageRef = FirebaseStorage.getInstance().getReference()


        mBinding.titleBar.tvToolbarTitle.text = "Add Staff"
        mBinding.titleBar.ivprofile.visibility = View.GONE
        mBinding.titleBar.ivBack.setOnClickListener {
            navController.navigateUp()
        }

        mBinding.btnAdd.setOnClickListener {
            addStaff()
        }


        mBinding.edtDob.setOnClickListener {

            val c = Calendar.getInstance()


            val year = c[Calendar.YEAR]
            val month = c[Calendar.MONTH]
            val day = c[Calendar.DAY_OF_MONTH]


            val datePickerDialog = DatePickerDialog(
                requireContext(), { view, year, monthOfYear, dayOfMonth ->
                    mBinding.edtDob.setText(dayOfMonth.toString() + "/" + (monthOfYear + 1) + "/" + year)
                }, year, month, day
            )

            datePickerDialog.show()


        }
        mBinding.edtDoj.setOnClickListener {

            val c = Calendar.getInstance()


            val year = c[Calendar.YEAR]
            val month = c[Calendar.MONTH]
            val day = c[Calendar.DAY_OF_MONTH]


            val datePickerDialog = DatePickerDialog(
                requireContext(), { view, year, monthOfYear, dayOfMonth ->
                    mBinding.edtDoj.setText(dayOfMonth.toString() + "/" + (monthOfYear + 1) + "/" + year)
                }, year, month, day
            )
            datePickerDialog.show()
        }

    }

    private fun addStaff() {
        // get data from editText
        val name = mBinding.edtName.text.toString()
        val email = mBinding.edtEmailId.text.toString()
        val phoneNo = mBinding.edtMobileNo.text.toString()
        val dob = mBinding.edtDob.text.toString()
        val doj = mBinding.edtDoj.text.toString()
        val place = mBinding.edtPlace.text.toString()
        val experience = mBinding.edtExperience.text.toString()




        if (name.isNullOrEmpty()) {
            mBinding.edtName.error = "Please enter name"
        } else if (email.isEmpty()) {
            mBinding.edtEmailId.error = "Please enter empId"
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email.trim { it <= ' ' })
                .matches() && !Patterns.PHONE.matcher(email.trim { it <= ' ' }).matches()
        ) {
            mBinding.edtEmailId.error = "enter valid email"
        } else if (phoneNo.isNullOrEmpty()) {
            mBinding.edtMobileNo.error = "Please Enter MobileNo"
        } else if (!Patterns.PHONE.matcher(
                mBinding.edtMobileNo.getText().toString().trim { it <= ' ' })
                .matches() || mBinding.edtMobileNo.getText().toString()
                .trim { it <= ' ' }.length < 10 || mBinding.edtMobileNo.getText().toString()
                .trim { it <= ' ' }.length > 10
        ) {
            mBinding.edtMobileNo.error = "valid Mobile No"
        } else if (dob.isEmpty()) {
            mBinding.edtDob.error = "Please enter dob"
        } else if (doj.isEmpty()) {
            mBinding.edtDoj.error = "Please enter doj"
        } else if (place.isEmpty()) {
            mBinding.edtPlace.error = "Please enter place"
        } else {
            val staffId = dbRef.push().key!!
            val staffModel = StaffModel(
                staffId,
                name,
                email,
                phoneNo,
                dob,
                doj,
                "",
                "qualification",
                experience,
                place,
                "", ""
            )

            dbRef.child(staffId).setValue(staffModel).addOnCompleteListener {
                Toast.makeText(context, "Add data Successfully", Toast.LENGTH_SHORT).show()

                findNavController().navigate(R.id.action_addCartakerStaffFragment_to_careTakerListFragment)

            }.addOnFailureListener {

            }
        }
    }

}