package com.example.mysicu.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.mysicu.R
import com.example.mysicu.databinding.FragmentHomeBinding
import com.example.mysicu.models.HomeMenuData
import com.example.mysicu.viewModels.HomeViewModels
import com.google.firebase.auth.FirebaseAuth


class HomeFragment : Fragment() {

    lateinit var mBinding: FragmentHomeBinding
    lateinit var auth: FirebaseAuth
    private val viewModel: HomeViewModels by viewModels()
    private val homeMenuDataList: ArrayList<HomeMenuData> = ArrayList()
//    private lateinit var homeAdaptor: HomeAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


//        homeAdaptor = HomeAdapter(homeMenuDataList, this)
//        mBinding.recMenu.adapter = homeAdaptor


        mBinding.cvNursingStaff.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_staffListFragment)
        }
        mBinding.ivprofile.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_profileFragment)
        }
        mBinding.cvDoctor.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_doctorListFragment)
        }


        viewModel.getHomeMenuDataData()
            .observe(viewLifecycleOwner, object : Observer<List<HomeMenuData>> {
                override fun onChanged(value: List<HomeMenuData>) {
                    homeMenuDataList.clear()
                    homeMenuDataList.addAll(value)
//                    homeAdaptor.notifyDataSetChanged()
                }

            })

        auth = FirebaseAuth.getInstance()


    }
    

}