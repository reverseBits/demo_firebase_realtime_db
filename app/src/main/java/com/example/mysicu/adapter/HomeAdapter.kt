package com.example.mysicu.adapter

import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mysicu.R
import com.example.mysicu.databinding.ItemHomeMenuBinding
import com.example.mysicu.models.HomeMenuData

class HomeAdapter(val list: ArrayList<HomeMenuData>,val fragment: Fragment) :
    RecyclerView.Adapter<ServicesViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ServicesViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val mBinding = DataBindingUtil.inflate<ItemHomeMenuBinding>(
            inflater,
            R.layout.item_home_menu,
            parent,
            false
        )

        return ServicesViewHolder(mBinding)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ServicesViewHolder, position: Int) {

        val doneList = list[position]


        holder.mBinding.tvMenuName.text = doneList.name

        holder.itemView.setOnClickListener {

            when(doneList.name){

                "Nursing Staff" -> {

                    fragment.findNavController().navigate(R.id.action_homeFragment_to_staffListFragment)
                }

                "Patient's" ->{

                }
                "Doctors Staff" ->
                {

                }

                "Inventory" -> {

                }

                "Profile" -> {
                    fragment.findNavController().navigate(R.id.action_homeFragment_to_profileFragment)
                }

                "Notes" -> {


                }

            }

        }

        if (!doneList.img.isNullOrEmpty()){
            Glide.with(holder.mBinding.root)
                .load(doneList.img)
                .error(R.drawable.loader)
                .thumbnail(0.5f)
                .into(holder.mBinding.imgMenu)
        }else{

            holder.mBinding.imgMenu.visibility = View.GONE
            holder.mBinding.tvMenuName.gravity = Gravity.CENTER_VERTICAL
        }
    }
}

class ServicesViewHolder(val mBinding: ItemHomeMenuBinding) :
    RecyclerView.ViewHolder(mBinding.root) {

}