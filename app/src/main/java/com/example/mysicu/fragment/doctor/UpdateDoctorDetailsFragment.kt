package com.example.mysicu.fragment.doctor

import android.annotation.SuppressLint
import android.app.Activity
import android.app.DatePickerDialog
import android.app.Dialog
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
import android.view.Window
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
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
import com.example.mysicu.adapter.QualificationListAdapter
import com.example.mysicu.databinding.FragmentDoctorDetailsBinding
import com.example.mysicu.databinding.FragmentUpdateDoctorDetailsBinding
import com.example.mysicu.models.StaffModel
import com.example.mysicu.viewModels.MainViewModel
import com.example.mysicu.viewModels.UpdatingDoctorStaffViewModel
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.util.Calendar

class UpdateDoctorDetailsFragment : Fragment() {

    lateinit var mBinding: FragmentUpdateDoctorDetailsBinding
    private val args: UpdateDoctorDetailsFragmentArgs by navArgs()
    private lateinit var dbRef: DatabaseReference
    var image: String? = null
    private var storageRef = Firebase.storage.reference
    lateinit var viewModel: UpdatingDoctorStaffViewModel
    private lateinit var mainViewModel: MainViewModel
    private val navController: NavController by lazy {
        Navigation.findNavController(mBinding.root)
    }
    private val qualificationList: ArrayList<String> = ArrayList()
    private lateinit var qualificationListAdapter: QualificationListAdapter
    private val doctorTypeList: ArrayList<String> = ArrayList()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        mBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_update_doctor_details,
            container,
            false
        )
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get()
        mainViewModel = ViewModelProvider(requireActivity()).get()
        dbRef = FirebaseDatabase.getInstance().getReference("Stafflist")

        mBinding.titleBar.tvToolbarTitle.setText("Update")
        mBinding.titleBar.ivprofile.visibility = View.GONE

        qualificationListAdapter = QualificationListAdapter(qualificationList, "StaffDetails")
        mBinding.rvQualificationList.adapter = qualificationListAdapter


        val refQualification = args.staffModel?.qualification

        val qualification = refQualification?.split(", ")
        Log.d("TAG", "setStaffDetailModel: $qualification")

        if (qualification != null) {
            for (substring in qualification) {
                qualificationList.add(substring)
            }
            mBinding.cvEdu.visibility = View.VISIBLE
            qualificationListAdapter.notifyDataSetChanged()
        } else {
            mBinding.cvEdu.visibility = View.GONE
        }

        doctorTypeList.add("gynecologist")
        doctorTypeList.add("Cardiologist")
        doctorTypeList.add("Audiologist")
        doctorTypeList.add("Dentist")
        doctorTypeList.add("ENT Specialists")
        doctorTypeList.add("Gynecologist")
        doctorTypeList.add("Orthopedic ")
        doctorTypeList.add("Radiologist ")
        doctorTypeList.add("Pediatrician ")
        doctorTypeList.add("Psychiatrist ")
        doctorTypeList.add("Pulmonologist ")
        doctorTypeList.add("Endocrinologist ")
        doctorTypeList.add("Oncologist ")
        doctorTypeList.add("Neurologist ")
        doctorTypeList.add("Cardiothoracic Surgeon")


        //    get data from staffDetails Fragment
        val name = args.staffModel?.name
        val email = args.staffModel?.emailId
        val phoneNo = args.staffModel?.phoneNo
        val dob = args.staffModel?.dob
        val doj = args.staffModel?.doj
        val place = args.staffModel?.place
        val type = args.staffModel?.type
        val experience = args.staffModel?.experience
        val img = args.staffModel?.image

        //    set data in editText of updateStaff fragment
        mBinding.edtName.setText(name)
        mBinding.edtEmailId.setText(email)
        mBinding.edtMobileNo.setText(phoneNo)
        mBinding.edtDob.setText(dob)
        mBinding.edtDoj.setText(doj)
        mBinding.edtPlace.setText(place)
        mBinding.ddNurseType.setText(type)
        mBinding.edtExperience.setText(experience)

        if (!img.isNullOrEmpty()) {
            Glide.with(this).load(img).circleCrop().into(mBinding.imgUserImage)
            mBinding.ivUpload.visibility = View.GONE
        } else {
            mBinding.ivUpload.visibility = View.VISIBLE

        }

        viewModel.updatingDoctorStaffLiveData.observe(viewLifecycleOwner) { staffModel ->
            if (staffModel != null) {
                Toast.makeText(context, "update successfully", Toast.LENGTH_SHORT).show()

                mainViewModel.staffDetailsMutableLiveData.value = staffModel
                findNavController().navigateUp()
            } else {
                Toast.makeText(
                    context,
                    "Something went wrong please,try again!",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        mBinding.ddNurseType.setOnDismissListener {
            var adapter = ArrayAdapter(requireContext(), R.layout.item_dropdownmenu, doctorTypeList)

            mBinding.ddNurseType.setAdapter(adapter)
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

        mBinding.edtQualification.setOnClickListener {

            Log.d("TAG", "onViewCreated: ")

            val dialog = context.let { it1 -> Dialog(it1!!) }

            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setContentView(R.layout.dialog_education)
            dialog.window?.setLayout(1450, 1100)

            val edtQualification = dialog.findViewById<EditText>(R.id.edtQualification)
            val edtUniversity = dialog.findViewById<EditText>(R.id.edtUniversity)
            val add = dialog.findViewById<Button>(R.id.btnAdd)

            add.setOnClickListener {

                if (edtQualification.text.isNullOrEmpty()) {
                    edtQualification.error = "Please enter Field"
                } else if (edtUniversity.text.isNullOrEmpty()) {
                    edtUniversity.error = "Please enter Field"
                } else {
                    var qualification =
                        edtQualification.text.toString() + " - " + edtUniversity.text.toString()
                    qualificationList.add(qualification)
                    qualificationListAdapter.notifyDataSetChanged()
                    mBinding.cvEdu.visibility = View.VISIBLE
                    dialog.dismiss()
                }

            }
            dialog.show()
        }

    }

    private fun updateData() {

        //    get updated data
        val name = mBinding.edtName.text.toString()
        val email = mBinding.edtEmailId.text.toString()
        val phoneNo = mBinding.edtMobileNo.text.toString()
        val dob = mBinding.edtDob.text.toString()
        val doj = mBinding.edtDoj.text.toString()
        val type = mBinding.ddNurseType.text.toString()
        val place = mBinding.edtPlace.text.toString()
        val experience = mBinding.edtExperience.text.toString()

        if (image.isNullOrEmpty()) {
            image = args.staffModel?.image
        } else {

        }

        val delQualification = qualificationList.toString()


        val refQualification = delQualification.replace("[", "")
        val qualification = refQualification.replace("]", "")

        Log.d("TAG", "updateData: $qualification")

        val staffModel =
            StaffModel(
                args.staffModel?.staffId,
                name,
                email,
                phoneNo,
                dob,
                doj,
                type,
                qualification,
                experience,
                place,
                image
            )

        viewModel.updateDetails(staffModel)
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