package com.example.mysicu.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.mysicu.R
import com.example.mysicu.databinding.ItemEductionBinding

class QualificationListAdapter(private val qualificationList: ArrayList<String>) :
    RecyclerView.Adapter<QualificationListViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QualificationListViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<ItemEductionBinding>(
            inflater,
            R.layout.item_eduction,
            parent,
            false
        )
        return QualificationListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: QualificationListViewHolder, position: Int) {
        val data = qualificationList[position]

        holder.binding.tvName.text = data


        holder.binding.ivDelete.setOnClickListener {
            qualificationList.remove(data)
            notifyItemRemoved(position)
            notifyDataSetChanged()
        }

    }

    override fun getItemCount(): Int {
        return qualificationList.size
    }
}

class QualificationListViewHolder(val binding: ItemEductionBinding) :
    RecyclerView.ViewHolder(binding.root)