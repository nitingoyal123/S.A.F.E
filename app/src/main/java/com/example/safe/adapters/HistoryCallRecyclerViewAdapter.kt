package com.example.safe

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.safe.databinding.HistoryCallItemLayoutBinding
import com.example.safe.databinding.HistoryContactItemLayoutBinding
import com.example.safe.databinding.HistoryMessageItemLayoutBinding
import com.example.safe.model.Call
import com.example.safe.model.HistoryDataItem
import com.example.safe.model.Message
import com.example.safe.HistoryMessageRecyclerViewAdapter.MyViewHolder as MyViewHolder1

class HistoryCallRecyclerViewAdapter(var context : Context, var list : MutableList<Call>) : RecyclerView.Adapter<HistoryCallRecyclerViewAdapter.MyViewHolder>() {


    class MyViewHolder(var binding : HistoryCallItemLayoutBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(call: Call) {
            binding.call = call
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int ): MyViewHolder {
        var binding = HistoryCallItemLayoutBinding.inflate(LayoutInflater.from(context))
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }


}