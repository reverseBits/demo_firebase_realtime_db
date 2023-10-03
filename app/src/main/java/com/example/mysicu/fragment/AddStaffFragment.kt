package com.example.mysicu.fragment

import android.os.Bundle
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
import com.example.mysicu.databinding.FragmentAddStaffBinding
import com.example.mysicu.models.StaffModel
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class AddStaffFragment : Fragment() {

    lateinit var mBinding: FragmentAddStaffBinding
    lateinit var dbRef: DatabaseReference
    lateinit var storageRef: StorageReference
    private val navController: NavController by lazy {
        Navigation.findNavController(mBinding.root)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_add_staff, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dbRef = FirebaseDatabase.getInstance().getReference("Stafflist")
        storageRef = FirebaseStorage.getInstance().getReference("Images")

        mBinding.titleBar.tvToolbarTitle.text = "Add Staff"
        mBinding.titleBar.ivprofile.visibility = View.GONE
        mBinding.titleBar.ivBack.setOnClickListener{
            navController.navigateUp()
        }

        mBinding.btnAdd.setOnClickListener {
            addStaff()
        }


    }
    private fun addStaff() {
        // get data from editText
        val name = mBinding.edtName.text.toString()
        val empId = mBinding.edtEmpId.text.toString()
        val age = mBinding.edtAge.text.toString()
        val dob = mBinding.edtDob.text.toString()
        val doj = mBinding.edtDoj.text.toString()
        val place = mBinding.edtPlace.text.toString()
        val experience = mBinding.edtExperience.text.toString()


        if (name.isEmpty()) {
            mBinding.edtName.error = "Please enter name"
        } else if (empId.isEmpty()) {
            mBinding.edtEmpId.error = "Please enter empId"
        } else if (age.isEmpty()) {
            mBinding.edtAge.error = "Please enter age"
        } else if (dob.isEmpty()) {
            mBinding.edtDob.error = "Please enter dob"
        } else if (doj.isEmpty()) {
            mBinding.edtDoj.error = "Please enter doj"
        } else if (place.isEmpty()) {
            mBinding.edtPlace.error = "Please enter place"
        } else if (experience.isEmpty()) {
            mBinding.edtExperience.error = "Please enter experience"
        } else {

            val staffId = dbRef.push().key!!
            val staffModel = StaffModel(staffId, name, empId, age, dob, doj, place, experience)

            dbRef.child(staffId).setValue(staffModel).addOnCompleteListener {
                Toast.makeText(context, "Add data Successfully", Toast.LENGTH_SHORT).show()

                findNavController().navigate(R.id.action_addStaffFragment_to_staffListFragment)

            }.addOnFailureListener {

            }
        }
    }
}