package com.example.mysicu.fragment.doctor

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.example.mysicu.R
import com.example.mysicu.adapter.StaffAdapter
import com.example.mysicu.databinding.FragmentDoctorListBinding
import com.example.mysicu.models.StaffModel
import com.example.mysicu.viewModels.DoctorStaffListingViewModel
import com.example.mysicu.viewModels.StaffListingViewModel

class DoctorListFragment : Fragment() {

    lateinit var mBinding: FragmentDoctorListBinding
    private val viewModel: DoctorStaffListingViewModel by viewModels()
    private val navController: NavController by lazy {
        Navigation.findNavController(mBinding.root)
    }
    lateinit var staffAdapter: StaffAdapter
    private val doctorStaffList: ArrayList<StaffModel> = ArrayList()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        mBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_doctor_list, container, false)

        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        staffAdapter = StaffAdapter(doctorStaffList, "Doctor List")
        mBinding.recyclerView.adapter = staffAdapter
        getData()

        mBinding.titleBar.tvToolbarTitle.text = "Doctor Staff"

        mBinding.titleBar.ivBack.setOnClickListener {
            navController.navigateUp()
        }
        mBinding.titleBar.ivprofile.setOnClickListener {

            findNavController().navigate(R.id.profileFragment)

        }

        mBinding.fabAdd.setOnClickListener {
            findNavController().navigate(R.id.action_doctorListFragment_to_addNewDoctorFragment)
        }

        mBinding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
//                staffAdapter.filter(newText.orEmpty())
                return false
            }
        })

    }

    private fun getData() {
        mBinding.progressBar.visibility = View.VISIBLE

        viewModel.getDoctorStaffListLiveData().observe(
            viewLifecycleOwner
        ) { value ->
            Log.d("TAG", "onChanged: $value")

            doctorStaffList.clear()
            doctorStaffList.addAll(value)

            staffAdapter.notifyDataSetChanged()
            mBinding.progressBar.visibility = View.INVISIBLE
        }
    }

}