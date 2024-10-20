package com.example.safe

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.safe.databinding.ContactItemLayoutBinding
import com.example.safe.model.HomeDataItem

class HomeRecyclerViewAdapter(var context : Context, var list : ArrayList<HomeDataItem>) : RecyclerView.Adapter<HomeRecyclerViewAdapter.MyViewHolder>() {
    class MyViewHolder(var binding : ContactItemLayoutBinding) : RecyclerView.ViewHolder(binding.root){

        var name = binding.txtContactName
        var phone = binding.txtContactNumber

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        var binding = ContactItemLayoutBinding.inflate(LayoutInflater.from(context),parent,false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        holder.name.text = list[position].name
        holder.phone.text = list[position].phone
    }

    override fun getItemCount(): Int {
        return list.size
    }
}