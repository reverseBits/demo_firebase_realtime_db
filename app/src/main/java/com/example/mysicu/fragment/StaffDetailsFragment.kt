package com.example.mysicu.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.mysicu.MainViewModel
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

        mainViewModel = ViewModelProvider(requireActivity()).get()

        mainViewModel.staffDetailsMutableLiveData.observe(viewLifecycleOwner) {
            it?.let {
                setStaffDetailModel(it)
            }
        }

        setStaffDetailModel(args.staffModel)

        mBinding.imgEdit.setOnClickListener {

            val action =
                StaffDetailsFragmentDirections.actionStaffDetailsFragmentToUpdateStaffFragment(args.staffModel)
            findNavController().navigate(action)
        }

        mBinding.ivBack.setOnClickListener {
            navController.navigateUp()
        }

    }

    private fun setStaffDetailModel(staffModel: StaffModel?) {
        // set data in TextView of staffDetails fragment
        mBinding.tvResFullName.text = staffModel?.name
        mBinding.tvResEmpId.text = staffModel?.empId
        mBinding.tvResAge.text = staffModel?.age
        mBinding.tvResDob.text = staffModel?.dob
        mBinding.tvResDoj.text = staffModel?.doj
        mBinding.tvResPlace.text = staffModel?.place
        mBinding.tvResExperience.text = staffModel?.experience
    }
}