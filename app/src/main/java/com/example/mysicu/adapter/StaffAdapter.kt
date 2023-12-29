package com.example.mysicu.adapter

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.DialogInterface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mysicu.R
import com.example.mysicu.databinding.ItemStaffListBinding
import com.example.mysicu.fragment.Caretaker.CareTakerListFragmentDirections
import com.example.mysicu.fragment.doctor.DoctorListFragmentDirections
import com.example.mysicu.fragment.nurse.NursingStaffListFragmentDirections
import com.example.mysicu.models.StaffModel
import com.google.firebase.database.FirebaseDatabase
import java.util.Locale

class StaffAdapter(val staffList: List<StaffModel>, val screen: String) :
    RecyclerView.Adapter<StaffViewHolder>() {

//    private var filteredFragmentList: MutableList<StaffModel> = ArrayList(staffList)

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
                    holder.binding.tvType.text = "Staff Nurse"
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

            "CareTaker List" -> {
                val staffModel = staffList[position]
                holder.binding.tvName.text = staffModel.name



                holder.binding.tvType.visibility = View.GONE

                Glide.with(holder.binding.root)
                    .load(staffModel.image)
                    .circleCrop()
                    .placeholder(R.drawable.icon_doctor)
                    .into(holder.binding.ivEmp)

                holder.binding.cvStaff.setOnClickListener {

                    val action =
                        CareTakerListFragmentDirections.actionCareTakerListFragmentToCareTakerDetailsFragment(
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

//    fun filter(query: String) {
//        if (query.isEmpty()) {
//            filteredFragmentList.clear()
//            filteredFragmentList.addAll(staffList)
//        } else {
//            val lowercaseQuery = query.lowercase(Locale.getDefault())
//            filteredFragmentList.clear()
//            for (staff in staffList) {
//                if (staff.name!!.lowercase(Locale.getDefault()).contains(lowercaseQuery)
//                ) {
//                    filteredFragmentList.add(staff)
//                }
//            }
//        }
//        notifyDataSetChanged()
//    }


}

class StaffViewHolder(val binding: ItemStaffListBinding) : RecyclerView.ViewHolder(binding.root)

