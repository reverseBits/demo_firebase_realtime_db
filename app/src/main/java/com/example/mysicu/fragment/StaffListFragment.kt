package com.example.mysicu.fragment

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
import androidx.navigation.Navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.mysicu.R
import com.example.mysicu.adapter.StaffAdapter
import com.example.mysicu.databinding.FragmentStaffListBinding
import com.example.mysicu.models.StaffModel
import com.example.mysicu.viewModels.StaffListingViewModel


class StaffListFragment : Fragment() {

    lateinit var mBinding: FragmentStaffListBinding
    private val viewModel: StaffListingViewModel by viewModels()
    private val navController: NavController by lazy {
        findNavController(mBinding.root)
    }
    lateinit var staffAdapter: StaffAdapter
    private val staffList: ArrayList<StaffModel> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_staff_list, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        staffAdapter = StaffAdapter(staffList)
        mBinding.recyclerView.adapter = staffAdapter
        getData()

        mBinding.titleBar.tvToolbarTitle.text = "Nursing Staff"

        mBinding.titleBar.ivBack.setOnClickListener {
            navController.navigateUp()
        }
        mBinding.titleBar.ivprofile.setOnClickListener {

            findNavController().navigate(R.id.profileFragment)

        }

        mBinding.fabAdd.setOnClickListener {
            findNavController().navigate(R.id.action_staffListFragment_to_addStaffFragment)
        }

        mBinding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {

                return true
            }
        })
    }

    private fun getData() {
        mBinding.progressBar.visibility = View.VISIBLE

        viewModel.getStaffListLiveData().observe(
            viewLifecycleOwner,
            object : Observer<List<StaffModel>> {
                override fun onChanged(value: List<StaffModel>) {

                    Log.d("TAG", "onChanged: $value")

                    staffList.clear()
                    staffList.addAll(value)

                    staffAdapter.notifyDataSetChanged()
                    mBinding.progressBar.visibility = View.INVISIBLE

                }
            })
    }
}

