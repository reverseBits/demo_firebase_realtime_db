package com.example.mysicu.fragment.nurse

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.mysicu.R
import com.example.mysicu.adapter.QualificationListAdapter
import com.example.mysicu.databinding.FragmentNursingStaffDetailsBinding
import com.example.mysicu.models.StaffModel
import com.example.mysicu.viewModels.MainViewModel

class NursingStaffDetailsFragment : Fragment() {

    lateinit var mBinding: FragmentNursingStaffDetailsBinding

    private val args: NursingStaffDetailsFragmentArgs by navArgs()

    private lateinit var mainViewModel: MainViewModel

    private val navController: NavController by lazy {

        Navigation.findNavController(mBinding.root)
    }
    private val qualificationList: ArrayList<String> = ArrayList()
    private lateinit var qualificationListAdapter: QualificationListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding =
            DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_nursing_staff_details,
                container,
                false
            )
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

        setStaffDetailModel(args.staffModel)



        mBinding.titalBar.ivprofile.setOnClickListener {
            val action =
                NursingStaffDetailsFragmentDirections.actionStaffDetailsFragmentToUpdateStaffFragment(
                    args.staffModel
                )
            findNavController().navigate(action)
        }

        mBinding.titalBar.ivBack.setOnClickListener {
            navController.navigateUp()
        }
    }

    private fun setStaffDetailModel(staffModel: StaffModel?) {
        // set data in TextView of staffDetails fragment


        var refQualification = staffModel?.qualification


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