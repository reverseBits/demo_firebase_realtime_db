package com.example.mysicu.fragment.doctor

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.example.mysicu.R
import com.example.mysicu.adapter.QualificationListAdapter
import com.example.mysicu.databinding.FragmentAddNewDoctorBinding
import com.example.mysicu.models.StaffModel
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import java.util.Calendar

class AddNewDoctorFragment : Fragment() {

    lateinit var mBinding: FragmentAddNewDoctorBinding
    lateinit var dbRef: DatabaseReference
    private val navController: NavController by lazy {
        Navigation.findNavController(mBinding.root)
    }
    private val qualificationList: ArrayList<String> = ArrayList()
    private val doctorTypeList: ArrayList<String> = ArrayList()
    private lateinit var qualificationListAdapter: QualificationListAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        mBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_add_new_doctor, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dbRef = FirebaseDatabase.getInstance().getReference("DoctorList")


        qualificationListAdapter = QualificationListAdapter(qualificationList, "AddStaff")
        mBinding.rvQualificationList.adapter = qualificationListAdapter

        if (qualificationList.isEmpty()) {
            mBinding.cvEdu.visibility = View.GONE
        } else {
            mBinding.cvEdu.visibility = View.VISIBLE
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


        var adapter = ArrayAdapter(requireContext(), R.layout.item_dropdownmenu, doctorTypeList)

        mBinding.ddDoctorType.setAdapter(adapter)


        mBinding.titleBar.tvToolbarTitle.text = "Add New"
        mBinding.titleBar.ivprofile.visibility = View.GONE
        mBinding.titleBar.ivBack.setOnClickListener {
            navController.navigateUp()
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
        val delQualification = qualificationList.toString()
        val nurseType = mBinding.ddDoctorType.text.toString()

        val refQualification = delQualification.replace("[", "")
        val qualification = refQualification.replace("]", "")



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
        } else if (qualificationList.isEmpty()) {
            mBinding.edtQualification.error = "Please Add Qualification"
        } else if (nurseType.equals("Select")) {
            mBinding.ddDoctorType.error = "Please select Doctor Type"
        } else {
//            val empId = employeeId + 1
            val staffId = dbRef.push().key!!
            val staffModel = StaffModel(
                staffId,
                name,
                email,
                phoneNo,
                dob,
                doj,
                nurseType,
                qualification,
                experience,
                place,
                ""
            )

            dbRef.child(staffId).setValue(staffModel).addOnCompleteListener {
                Toast.makeText(context, "Add data Successfully", Toast.LENGTH_SHORT).show()

                findNavController().navigate(R.id.action_addStaffFragment_to_staffListFragment)

            }.addOnFailureListener {

            }
        }
    }

}