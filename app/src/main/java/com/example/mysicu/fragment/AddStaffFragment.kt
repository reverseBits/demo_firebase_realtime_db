package com.example.mysicu.fragment

import android.annotation.SuppressLint
import android.app.Activity
import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.mysicu.R
import com.example.mysicu.databinding.FragmentAddStaffBinding
import com.example.mysicu.models.StaffModel
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import java.util.Calendar


class AddStaffFragment : Fragment() {

    lateinit var mBinding: FragmentAddStaffBinding
    lateinit var dbRef: DatabaseReference
    private val navController: NavController by lazy {
        Navigation.findNavController(mBinding.root)
    }
    var image: String? = null

    // creating a storage reference
    private var storageRef = Firebase.storage.reference;


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
        storageRef = FirebaseStorage.getInstance().getReference()

        mBinding.titleBar.tvToolbarTitle.text = "Add Staff"
        mBinding.titleBar.ivprofile.visibility = View.GONE
        mBinding.titleBar.ivBack.setOnClickListener {
            navController.navigateUp()
        }

        mBinding.ivprofile.setOnClickListener {

            openGallary()

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
                requireContext(),
                { view, year, monthOfYear, dayOfMonth ->
                    mBinding.edtDob.setText(dayOfMonth.toString() + "/" + (monthOfYear + 1) + "/" + year)
                },
                year, month, day
            )

            datePickerDialog.show()


        }
        mBinding.edtDoj.setOnClickListener {

            val c = Calendar.getInstance()


            val year = c[Calendar.YEAR]
            val month = c[Calendar.MONTH]
            val day = c[Calendar.DAY_OF_MONTH]


            val datePickerDialog = DatePickerDialog(
                requireContext(),
                { view, year, monthOfYear, dayOfMonth ->
                    mBinding.edtDoj.setText(dayOfMonth.toString() + "/" + (monthOfYear + 1) + "/" + year)
                },
                year, month, day
            )

            datePickerDialog.show()


        }


    }

    private fun addStaff() {
        // get data from editText
        val name = mBinding.edtName.text.toString()
        val empId = mBinding.edtEmpId.text.toString()
        val dob = mBinding.edtDob.text.toString()
        val doj = mBinding.edtDoj.text.toString()
        val place = mBinding.edtPlace.text.toString()
        val experience = mBinding.edtExperience.text.toString()



        if (name.isEmpty()) {
            mBinding.edtName.error = "Please enter name"
        } else if (empId.isEmpty()) {
            mBinding.edtEmpId.error = "Please enter empId"
        }  else if (dob.isEmpty()) {
            mBinding.edtDob.error = "Please enter dob"
        } else if (doj.isEmpty()) {
            mBinding.edtDoj.error = "Please enter doj"
        } else if (place.isEmpty()) {
            mBinding.edtPlace.error = "Please enter place"
        } else if (experience.isEmpty()) {
            mBinding.edtExperience.error = "Please enter experience"
        } else {

            val staffId = dbRef.push().key!!
            val staffModel =
                StaffModel(staffId, name, empId, dob, doj, place, experience, image)

            dbRef.child(staffId).setValue(staffModel).addOnCompleteListener {
                Toast.makeText(context, "Add data Successfully", Toast.LENGTH_SHORT).show()

                findNavController().navigate(R.id.action_addStaffFragment_to_staffListFragment)

            }.addOnFailureListener {

            }
        }
    }

    private fun openGallary() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        changeImage.launch(intent)
    }

    private val changeImage =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) {
            if (it.resultCode == Activity.RESULT_OK) {
                val data = it.data
                val imgUri = data?.data

                val sd = getFileName(requireContext(), imgUri!!)

                val uploadTask = storageRef.child("profile/$sd").putFile(imgUri)
                uploadTask.addOnSuccessListener {

                    storageRef.child("profile/$sd").downloadUrl.addOnSuccessListener {
                        Glide.with(this)
                            .load(imgUri)
                            .circleCrop()
                            .into(mBinding.imgUserImage)

                        image = imgUri.toString()

                        mBinding.ivprofile.visibility = View.GONE
                    }.addOnFailureListener {
                        Log.e("Firebase", "Failed in downloading")
                    }
                }.addOnFailureListener {
                    Log.e("Firebase", "Image Upload fail")
                }


            }
        }

    @SuppressLint("Range")
    private fun getFileName(context: Context, uri: Uri): String? {
        if (uri.scheme == "content") {
            val cursor = context.contentResolver.query(uri, null, null, null, null)
            cursor.use {
                if (cursor != null) {
                    if (cursor.moveToFirst()) {
                        return cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME))
                    }
                }
            }
        }
        return uri.path?.lastIndexOf('/')?.let { uri.path?.substring(it) }
    }
}