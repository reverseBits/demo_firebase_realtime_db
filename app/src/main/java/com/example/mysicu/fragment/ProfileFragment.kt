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
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.mysicu.R
import com.example.mysicu.databinding.FragmentProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import java.util.Calendar


class ProfileFragment : Fragment() {

    lateinit var mBinding: FragmentProfileBinding
    lateinit var database: DatabaseReference
    lateinit var auth: FirebaseAuth
    var image: String? = null
    private var storageRef = Firebase.storage.reference
    private val navController: NavController by lazy {
        Navigation.findNavController(mBinding.root)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile, container, false)
        return mBinding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mBinding.btnUpdate.visibility = View.GONE
        mBinding.ivUpload.visibility = View.GONE


        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().getReference("Admin")
            .child(auth.currentUser?.uid.toString())
        storageRef = FirebaseStorage.getInstance().getReference()
        mBinding.titalBar.tvToolbarTitle.text = "Profile"
        mBinding.titalBar.ivprofile.setImageResource(R.drawable.edit_24)

        mBinding.titalBar.ivprofile.setOnClickListener {
            mBinding.edtResName.isEnabled = true
            mBinding.edtResPhoneNo.isEnabled = true
            mBinding.edtResDOB.isEnabled = true
            mBinding.edtResDOB.isClickable = true
            mBinding.edtResPlace.isEnabled = true
            mBinding.edtResExperience.isEnabled = true
            mBinding.edtResEducation.isEnabled = true
            mBinding.btnLogOut.visibility = View.GONE
            mBinding.ivUpload.visibility = View.VISIBLE
            mBinding.btnUpdate.visibility = View.VISIBLE
            mBinding.titalBar.ivprofile.visibility = View.GONE
        }

        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                mBinding.edtResName.setText(snapshot.child("name").getValue(String::class.java))
                mBinding.edtResEmailId.setText(snapshot.child("email").getValue(String::class.java))
                mBinding.edtResPhoneNo.setText(
                    snapshot.child("phoneNo").getValue(String::class.java)
                )
                mBinding.edtResDOB.setText(snapshot.child("dob").getValue(String::class.java))
                mBinding.edtResPlace.setText(snapshot.child("place").getValue(String::class.java))
                mBinding.edtResExperience.setText(
                    snapshot.child("experience").getValue(String::class.java)
                )
                mBinding.edtResEducation.setText(
                    snapshot.child("qualification").getValue(String::class.java)
                )

                val uri = Uri.parse(snapshot.child("image").getValue(String::class.java))
                Glide.with(requireContext()).load(uri).placeholder(R.drawable.user_profile_icon)
                    .into(mBinding.imgUserImage)




                Toast.makeText(requireContext(), "Successful", Toast.LENGTH_SHORT).show()
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("TAG", "error: ${error.message}")
                Toast.makeText(requireContext(), "Failed ${error.message}", Toast.LENGTH_SHORT)
                    .show()
            }
        })

        mBinding.ivUpload.setOnClickListener {
            openGallary()
        }

        mBinding.titalBar.ivBack.setOnClickListener {
            navController.navigateUp()
        }

        mBinding.edtResDOB.setOnClickListener {

            val c = Calendar.getInstance()

            val year = c[Calendar.YEAR]
            val month = c[Calendar.MONTH]
            val day = c[Calendar.DAY_OF_MONTH]


            val datePickerDialog = DatePickerDialog(
                requireContext(), { view, year, monthOfYear, dayOfMonth ->
                    mBinding.edtResDOB.setText(dayOfMonth.toString() + "/" + (monthOfYear + 1) + "/" + year)
                }, year, month, day
            )

            datePickerDialog.show()
        }

        mBinding.btnLogOut.setOnClickListener {

            auth.signOut()
            findNavController().navigate(ProfileFragmentDirections.actionProfileFragmentToLoginFragment())
        }


        mBinding.btnUpdate.setOnClickListener {
            UpdateData()
        }

    }

    private fun UpdateData() {

        val name = mBinding.edtResName.text.toString()
        val phoneNo = mBinding.edtResPhoneNo.text.toString()
        val dob = mBinding.edtResDOB.text.toString()
        val location = mBinding.edtResPlace.text.toString()
        val exp = mBinding.edtResExperience.text.toString()
        val que = mBinding.edtResEducation.text.toString()


        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                database.child("name").setValue(name)
                database.child("phoneNo").setValue(phoneNo)
                database.child("dob").setValue(dob)
                database.child("place").setValue(location)
                database.child("qualification").setValue(que)
                database.child("experience").setValue(exp)
                database.child("image").setValue(image)


                mBinding.edtResName.isEnabled = false
                mBinding.edtResPhoneNo.isEnabled = false
                mBinding.edtResDOB.isEnabled = false
                mBinding.edtResDOB.isClickable = false
                mBinding.edtResPlace.isEnabled = false
                mBinding.edtResExperience.isEnabled = false
                mBinding.edtResEducation.isEnabled = false
                mBinding.btnLogOut.visibility = View.VISIBLE
                mBinding.ivUpload.visibility = View.GONE
                mBinding.btnUpdate.visibility = View.GONE
                mBinding.titalBar.ivprofile.visibility = View.VISIBLE
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(requireContext(), "Failed", Toast.LENGTH_SHORT).show()
            }

        })


    }


    private fun openGallary() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        changeImage.launch(intent)
    }

    private val changeImage = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == Activity.RESULT_OK) {
            val data = it.data
            val imgUri = data?.data

            val sd = getFileName(requireContext(), imgUri!!)

            val uploadTask = storageRef.child("profile/$sd").putFile(imgUri)
            uploadTask.addOnSuccessListener {

                storageRef.child("profile/$sd").downloadUrl.addOnSuccessListener {
                    Glide.with(this).load(imgUri).circleCrop().into(mBinding.imgUserImage)

                    image = imgUri.toString()

                    mBinding.ivUpload.visibility = View.GONE
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