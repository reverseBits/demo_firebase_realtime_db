package com.example.mysicu.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.graphics.toColorFilter
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.mysicu.viewModels.MainViewModel
import com.example.mysicu.R
import com.example.mysicu.databinding.FragmentStaffDetailsBinding
import com.example.mysicu.models.StaffModel

class StaffDetailsFragment : Fragment() {

    lateinit var mBinding: FragmentStaffDetailsBinding

    private val args: StaffDetailsFragmentArgs by navArgs()

    private lateinit var mainViewModel: MainViewModel

    private val navController: NavController by lazy {

        Navigation.findNavController(mBinding.root)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_staff_details, container, false)
        return mBinding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mBinding.titalBar.ivprofile.setImageResource(R.drawable.edit_24)

        mainViewModel = ViewModelProvider(requireActivity()).get()


        mainViewModel.staffDetailsMutableLiveData.observe(viewLifecycleOwner) {
            it?.let {
                setStaffDetailModel(it)
            }
        }

        setStaffDetailModel(args.staffModel)



        mBinding.titalBar.ivprofile.setOnClickListener {

            val action =
                StaffDetailsFragmentDirections.actionStaffDetailsFragmentToUpdateStaffFragment(args.staffModel)
            findNavController().navigate(action)
        }

        mBinding.titalBar.ivBack.setOnClickListener {
            navController.navigateUp()
        }


    }

    private fun setStaffDetailModel(staffModel: StaffModel?) {
        // set data in TextView of staffDetails fragment
        mBinding.tvName.text = staffModel?.name
        mBinding.tvType.text = "Nurse"
//        mBinding.tvResEmpId.text = staffModel?.empId
//        mBinding.tvResDob.text = staffModel?.dob
//        mBinding.tvResDoj.text = staffModel?.doj
//        mBinding.tvResPlace.text = staffModel?.place
        mBinding.tvExp.text = staffModel?.experience + "yrs+"

        Glide.with(this)
            .load(staffModel?.image)
            .placeholder(R.drawable.icon_doctor)
            .circleCrop()
            .into(mBinding.ivEmp)
        Log.d("TAG", "setStaffDetailModel: ${staffModel?.image}")

    }
}