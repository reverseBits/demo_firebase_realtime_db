package com.example.mysicu.fragment

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.Navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.mysicu.R
import com.example.mysicu.databinding.FragmentStaffListBinding
import com.example.mysicu.databinding.ItemStaffListBinding
import com.example.mysicu.models.StaffModel
import com.google.firebase.database.FirebaseDatabase


class StaffListFragment : Fragment() {

    lateinit var mBinding: FragmentStaffListBinding
    private lateinit var viewModel: StaffListingViewModel
    private val navController: NavController by lazy {
        findNavController(mBinding.root)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_staff_list, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this).get(StaffListingViewModel::class.java)

        getData()

        mBinding.titleBar.tvToolbarTitle.text = "Nursing Staff"

        mBinding.titleBar.ivBack.setOnClickListener {
            navController.navigateUp()
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

        viewModel.staffListingLiveData.observe(
            viewLifecycleOwner,
            object : Observer<List<StaffModel>> {
                override fun onChanged(staffList: List<StaffModel>) {

                    val staffAdapter = StaffAdapter(staffList)
                    mBinding.recyclerView.adapter = staffAdapter
                    mBinding.progressBar.visibility = View.INVISIBLE

                }
            })
    }
}

class StaffAdapter(val staffList: List<StaffModel>) : RecyclerView.Adapter<StaffViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StaffViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<ItemStaffListBinding>(
            inflater,
            R.layout.item_staff_list,
            parent,
            false
        )
        return StaffViewHolder(binding)
    }

    override fun onBindViewHolder(holder: StaffViewHolder, position: Int) {
        val staffModel = staffList[position]
        holder.binding.tvName.text = staffModel.name
        holder.binding.tvEmpId.text = staffModel.empId

        holder.binding.cvStaff.setOnClickListener {

            val action =
                StaffListFragmentDirections.actionStaffListFragmentToStaffDetailsFragment(staffModel)
            findNavController(holder.itemView).navigate(action)
        }

        holder.binding.cvStaff.setOnLongClickListener {

            val builder = AlertDialog.Builder(it.context)
            builder.setTitle("Delete")
            builder.setMessage("Do you want to delete it ?")
            builder.setIcon(R.drawable.ic_delete_24)

            builder.setPositiveButton("yes", object : DialogInterface.OnClickListener {
                override fun onClick(dialog: DialogInterface?, which: Int) {

                    val dbRef = FirebaseDatabase.getInstance().getReference("Stafflist")
                    dbRef.child(staffModel.staffId.toString()).removeValue()

                }
            }).setNegativeButton("No") { dialog, which ->

            }
            builder.create().show()
            true
        }
    }

    override fun getItemCount(): Int {
        return staffList.size
    }
}

class StaffViewHolder(val binding: ItemStaffListBinding) : RecyclerView.ViewHolder(binding.root)