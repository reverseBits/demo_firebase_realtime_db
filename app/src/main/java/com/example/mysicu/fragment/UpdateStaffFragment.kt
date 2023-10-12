package com.example.mysicu.fragment

import android.os.Bundle
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
import com.example.mysicu.viewModels.MainViewModel
import com.example.mysicu.R
import com.example.mysicu.databinding.FragmentUpdateStaffBinding
import com.example.mysicu.models.StaffModel
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class UpdateStaffFragment : Fragment() {

    private lateinit var mBinding: FragmentUpdateStaffBinding
    private val args: UpdateStaffFragmentArgs by navArgs()
    private lateinit var dbRef: DatabaseReference

    private lateinit var viewModel: UpdatingStaffViewModel
    private lateinit var mainViewModel: MainViewModel

    private val navController: NavController by lazy {
        Navigation.findNavController(mBinding.root)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_update_staff, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get()
        mainViewModel = ViewModelProvider(requireActivity()).get()
        dbRef = FirebaseDatabase.getInstance().getReference("Stafflist")

        mBinding.titleBar.tvToolbarTitle.setText("Nursing Staff")

        //    get data from staffDetails Fragment
        val name = args.staffModel?.name
        val empId = args.staffModel?.empId
        val dob = args.staffModel?.dob
        val doj = args.staffModel?.doj
        val place = args.staffModel?.place
        val experience = args.staffModel?.experience

        //    set data in editText of updateStaff fragment
        mBinding.edtName.setText(name)
        mBinding.edtEmpId.setText(empId)
        mBinding.edtDob.setText(dob)
        mBinding.edtDoj.setText(doj)
        mBinding.edtPlace.setText(place)
        mBinding.edtExperience.setText(experience)


        viewModel.updatingStaffLiveData.observe(viewLifecycleOwner) { staffModel ->
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

        mBinding.btnUpdate.setOnClickListener {
            updateData()
        }

        mBinding.titleBar.ivBack.setOnClickListener {
            navController.navigateUp()
        }
    }

    private fun updateData() {

        //    get updated data
        val name = mBinding.edtName.text.toString()
        val empId = mBinding.edtEmpId.text.toString()
        val age = mBinding.edtAge.text.toString()
        val dob = mBinding.edtDob.text.toString()
        val doj = mBinding.edtDoj.text.toString()
        val place = mBinding.edtPlace.text.toString()
        val experience = mBinding.edtExperience.text.toString()

        val staffModel =
            StaffModel(args.staffModel?.staffId, name, empId, age, dob, doj, place, experience)

        viewModel.updateDetails(staffModel)
    }
}