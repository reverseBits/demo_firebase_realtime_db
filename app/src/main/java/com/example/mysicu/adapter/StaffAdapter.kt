package com.example.mysicu.adapter

import android.app.AlertDialog
import android.content.DialogInterface
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mysicu.R
import com.example.mysicu.databinding.ItemStaffListBinding
import com.example.mysicu.fragment.doctor.DoctorListFragmentDirections
//import com.example.mysicu.fragment.StaffListFragmentDirections
import com.example.mysicu.fragment.nurse.NursingStaffListFragmentDirections
import com.example.mysicu.models.StaffModel
import com.google.firebase.database.FirebaseDatabase

class StaffAdapter(val staffList: List<StaffModel>, val screen: String) :
    RecyclerView.Adapter<StaffViewHolder>() {

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


//        holder.binding.tvType.text = "Gynaecologist"

        when (screen) {

            "Nurse List" -> {

                val staffModel = staffList[position]

                holder.binding.tvName.text = staffModel.name

                if (staffModel.type.isNullOrEmpty()) {
                    holder.binding.tvType.text = "Nurse"
                } else {
                    holder.binding.tvType.text = staffModel.type
                }

                Glide.with(holder.binding.root)
                    .load(staffModel.image)
                    .circleCrop()
                    .placeholder(R.drawable.icon_nurse)
                    .into(holder.binding.ivEmp)

                holder.binding.cvStaff.setOnClickListener {

                    val action =
                        NursingStaffListFragmentDirections.actionStaffListFragmentToStaffDetailsFragment(
                            staffModel
                        )
                    Navigation.findNavController(holder.itemView).navigate(action)
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
                            notifyDataSetChanged()

                        }
                    }).setNegativeButton("No") { dialog, which ->

                    }
                    builder.create().show()
                    true
                }
            }

            "Doctor List" -> {
                val staffModel = staffList[position]
                holder.binding.tvName.text = staffModel.name

                if (staffModel.type.isNullOrEmpty()) {
                    holder.binding.tvType.text = "gynecologist"
                } else {
                    holder.binding.tvType.text = staffModel.type
                }

                Glide.with(holder.binding.root)
                    .load(staffModel.image)
                    .circleCrop()
                    .placeholder(R.drawable.icon_doctor)
                    .into(holder.binding.ivEmp)

                holder.binding.cvStaff.setOnClickListener {

                    val action =
                        DoctorListFragmentDirections.actionDoctorListFragmentToDoctorDetailsFragment(
                            staffModel
                        )
                    Navigation.findNavController(holder.itemView).navigate(action)
                }

//                holder.binding.cvStaff.setOnLongClickListener {
//
//                    val builder = AlertDialog.Builder(it.context)
//                    builder.setTitle("Delete")
//                    builder.setMessage("Do you want to delete it ?")
//                    builder.setIcon(R.drawable.ic_delete_24)
//
//                    builder.setPositiveButton("yes", object : DialogInterface.OnClickListener {
//                        override fun onClick(dialog: DialogInterface?, which: Int) {
//
//                            val dbRef = FirebaseDatabase.getInstance().getReference("Stafflist")
//                            dbRef.child(staffModel.staffId.toString()).removeValue()
//                            notifyDataSetChanged()
//
//                        }
//                    }).setNegativeButton("No") { dialog, which ->
//
//                    }
//                    builder.create().show()
//                    true
//                }
            }
        }
    }

    override fun getItemCount(): Int {
        return staffList.size
    }
}

class StaffViewHolder(val binding: ItemStaffListBinding) : RecyclerView.ViewHolder(binding.root)