package com.example.mysicu.fragment.doctor

import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
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
import com.example.mysicu.fragment.nurse.NursingStaffDetailsFragmentDirections
import com.example.mysicu.models.StaffModel
import com.example.mysicu.viewModels.MainViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage

class DoctorDetailsFragment : Fragment() {

    lateinit var mBinding: FragmentDoctorDetailsBinding
    lateinit var database: DatabaseReference
    private val args: DoctorDetailsFragmentArgs by navArgs()
    private val navController: NavController by lazy {
        Navigation.findNavController(mBinding.root)
    }
    private lateinit var mainViewModel: MainViewModel
    private val qualificationList: ArrayList<String> = ArrayList()
    private lateinit var qualificationListAdapter: QualificationListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        mBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_doctor_details, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mBinding.titalBar.ivprofile.setImageResource(R.drawable.edit_24)

        mainViewModel = ViewModelProvider(requireActivity()).get()


        qualificationListAdapter = QualificationListAdapter(qualificationList, "StaffDetails")
        mBinding.rvQualificationList.adapter = qualificationListAdapter


        mainViewModel.staffDetailsMutableLiveData.observe(viewLifecycleOwner) {
            it?.let {
                setStaffDetailModel(it)
            }
        }

        setStaffDetailModel(args.staffmodel)


        mBinding.titalBar.ivprofile.setOnClickListener {

            val action =
                DoctorDetailsFragmentDirections.actionDoctorDetailsFragmentToUpdateDoctorDetailsFragment(
                    args.staffmodel
                )
            findNavController().navigate(action)
        }

        mBinding.titalBar.ivBack.setOnClickListener {
            navController.navigateUp()
        }
    }

    private fun setStaffDetailModel(staffModel: StaffModel?) {
        // set data in TextView of staffDetails fragment


        val refQualification = staffModel?.qualification

        if (refQualification.equals("")) {
            mBinding.cvEdu.visibility = View.GONE
        } else {
            val qualification = refQualification?.split(", ")

            Log.d("TAG", "qualification: $qualification")

            if (qualification != null) {

                qualificationList.clear()
                qualificationList.addAll(qualification)
                qualificationListAdapter.notifyDataSetChanged()

            }
        }

        val noOfPatients = staffModel?.noOfPatients

        if (noOfPatients.isNullOrEmpty()) {
            mBinding.lnrPatients.visibility = View.GONE
        } else {
            mBinding.lnrPatients.visibility = View.VISIBLE
            mBinding.tvRsPatients.text = noOfPatients

        }

//        Log.d("TAG", "setStaffDetailModel: $qualification")

        mBinding.tvName.text = staffModel?.name
        mBinding.tvRsEmailId.text = staffModel?.emailId
        mBinding.tvRsPhoneNo.text = staffModel?.phoneNo
        mBinding.tvRsLocation.text = staffModel?.place
        mBinding.tvRsBirth.text = staffModel?.dob
        mBinding.tvRsdoj.text = staffModel?.doj
        if (staffModel != null) {
            if (staffModel.type.isNullOrEmpty()) {
                mBinding.tvType.text = "Nurse"
            } else {
                if (staffModel != null) {
                    mBinding.tvType.text = staffModel.type
                }
            }
        }
        if (staffModel?.experience.isNullOrEmpty()) {
//            mBinding.lnrExp.visibility = View.GONE
            mBinding.tvExp.text = "No"
        } else {
            mBinding.tvExp.text = staffModel?.experience + "yrs+"
        }

        Glide.with(this)
            .load(staffModel?.image)
            .placeholder(R.drawable.icon_doctor)
            .circleCrop()
            .into(mBinding.ivEmp)
//        Log.d("TAG", "setStaffDetailModel: ${staffModel?.image}")

    }
}