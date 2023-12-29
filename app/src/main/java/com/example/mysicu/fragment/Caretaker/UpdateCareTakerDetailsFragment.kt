package com.example.mysicu.fragment.Caretaker

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
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.mysicu.R
import com.example.mysicu.databinding.FragmentUpdateCareTakerDetailsBinding
import com.example.mysicu.models.StaffModel
import com.example.mysicu.viewModels.MainViewModel
import com.example.mysicu.viewModels.UpdatingCareTakerStaffViewModel
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.util.Calendar


class UpdateCareTakerDetailsFragment : Fragment() {

    lateinit var mBinding: FragmentUpdateCareTakerDetailsBinding
    private val args: UpdateCareTakerDetailsFragmentArgs by navArgs()
    private lateinit var dbRef: DatabaseReference
    var image: String? = null
    private var storageRef = Firebase.storage.reference
    lateinit var viewModel: UpdatingCareTakerStaffViewModel
    private lateinit var mainViewModel: MainViewModel
    private val navController: NavController by lazy {
        Navigation.findNavController(mBinding.root)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        mBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_update_care_taker_details, container, false
        )
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get()
        mainViewModel = ViewModelProvider(requireActivity()).get()
        dbRef = FirebaseDatabase.getInstance().getReference("caretakerList")

        mBinding.titleBar.tvToolbarTitle.setText("Update")
        mBinding.titleBar.ivprofile.visibility = View.GONE


        val refQualification = args.staffModel?.qualification

        val qualification = refQualification?.split(", ")
        Log.d("TAG", "setStaffDetailModel: $qualification")


        //    get data from staffDetails Fragment
        val name = args.staffModel?.name
        val email = args.staffModel?.emailId
        val phoneNo = args.staffModel?.phoneNo
        val dob = args.staffModel?.dob
        val doj = args.staffModel?.doj
        val place = args.staffModel?.place
        val experience = args.staffModel?.experience
        val img = args.staffModel?.image

        //    set data in editText of updateStaff fragment
        mBinding.edtName.setText(name)
        mBinding.edtEmailId.setText(email)
        mBinding.edtMobileNo.setText(phoneNo)
        mBinding.edtDob.setText(dob)
        mBinding.edtDoj.setText(doj)
        mBinding.edtPlace.setText(place)
        mBinding.edtExperience.setText(experience)

        if (!img.isNullOrEmpty()) {
            Glide.with(this).load(img).circleCrop().into(mBinding.imgUserImage)
            mBinding.ivUpload.visibility = View.GONE
        } else {
            mBinding.ivUpload.visibility = View.VISIBLE

        }

        viewModel.updatingCareTakerStaffLiveData.observe(viewLifecycleOwner) { staffModel ->
            if (staffModel != null) {
                Toast.makeText(context, "update successfully", Toast.LENGTH_SHORT).show()

                mainViewModel.staffDetailsMutableLiveData.value = staffModel
                findNavController().navigateUp()
            } else {
                Toast.makeText(
                    context, "Something went wrong please,try again!", Toast.LENGTH_SHORT
                ).show()
            }
        }


        mBinding.ivUpload.setOnClickListener {
            openGallary()
        }
        mBinding.btnUpdate.setOnClickListener {
            updateData()
        }

        mBinding.titleBar.ivBack.setOnClickListener {
            navController.navigateUp()
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

    private fun updateData() {

        //    get updated data
        val name = mBinding.edtName.text.toString()
        val email = mBinding.edtEmailId.text.toString()
        val phoneNo = mBinding.edtMobileNo.text.toString()
        val dob = mBinding.edtDob.text.toString()
        val doj = mBinding.edtDoj.text.toString()
        val place = mBinding.edtPlace.text.toString()
        val experience = mBinding.edtExperience.text.toString()

        if (image.isNullOrEmpty()) {
            image = args.staffModel?.image
        } else {

        }

        val staffModel = StaffModel(
            args.staffModel?.staffId,
            name,
            email,
            phoneNo,
            dob,
            doj,
            "",
            "",
            experience,
            place,
            "",
            image
        )

        viewModel.updateCareTakerDetails(staffModel)
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

                it.storage.downloadUrl.addOnSuccessListener {

                    Log.d("TAG", "uri$it: ")
                    Glide.with(this).load(it).circleCrop().into(mBinding.imgUserImage)
                    image = it.toString()
                }
                mBinding.ivUpload.visibility = View.GONE
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