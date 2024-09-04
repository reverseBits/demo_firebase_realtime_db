package com.example.mysicu.fragment.Caretaker

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
import com.bumptech.glide.Glide
import com.example.mysicu.R
import com.example.mysicu.databinding.FragmentCareTakerDetailsBinding
import com.example.mysicu.models.StaffModel
import com.example.mysicu.viewModels.MainViewModel
import com.google.firebase.database.DatabaseReference

class CareTakerDetailsFragment : Fragment() {

    lateinit var mBinding: FragmentCareTakerDetailsBinding
    lateinit var database: DatabaseReference
    private val args: CareTakerDetailsFragmentArgs by navArgs()
    private val navController: NavController by lazy {
        Navigation.findNavController(mBinding.root)
    }
    private lateinit var mainViewModel: MainViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        mBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_care_taker_details,
            container,
            false
        )
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mBinding.tvType.visibility = View.GONE
        mBinding.lnrPatients.visibility = View.GONE

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
                CareTakerDetailsFragmentDirections.actionCareTakerDetailsFragmentToUpdateCareTakerDetailsFragment(
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