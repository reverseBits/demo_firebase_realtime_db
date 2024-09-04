package com.example.mysicu.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
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
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class HomeFragment : Fragment() {

    lateinit var mBinding: FragmentHomeBinding
    private val viewModel: HomeViewModels by viewModels()
    private val homeMenuDataList: ArrayList<HomeMenuData> = ArrayList()
    lateinit var auth: FirebaseAuth
    lateinit var database: DatabaseReference
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

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().getReference("Admin")
            .child(auth.currentUser?.uid.toString())

//        homeAdaptor = HomeAdapter(homeMenuDataList, this)
//        mBinding.recMenu.adapter = homeAdaptor

        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                mBinding.tvAdminName.setText(
                    snapshot.child("name").getValue(String::class.java) + " !"
                )
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(requireContext(), "Failed ${error.message}", Toast.LENGTH_SHORT)
                    .show()
            }

        })

        mBinding.cvNursingStaff.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_staffListFragment)
        }
        mBinding.ivprofile.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_profileFragment)
        }
        mBinding.cvDoctor.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_doctorListFragment)
        }
        mBinding.cvCareTakerStaff.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_careTakerListFragment)
        }

//        mBinding.


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