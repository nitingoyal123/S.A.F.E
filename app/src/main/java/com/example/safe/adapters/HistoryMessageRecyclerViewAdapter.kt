package com.example.safe

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.safe.databinding.HistoryMessageItemLayoutBinding
import com.example.safe.model.Message
import com.example.safe.HistoryMessageRecyclerViewAdapter.MyViewHolder as MyViewHolder1

class HistoryMessageRecyclerViewAdapter(var context : Context, var list : MutableList<Message>) : RecyclerView.Adapter<HistoryMessageRecyclerViewAdapter.MyViewHolder>() {

    var onItemClick : ((Message) -> Unit)? = null

    class MyViewHolder(var binding : HistoryMessageItemLayoutBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(message: Message) {
            binding.message = message
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int ): MyViewHolder {
        var binding = HistoryMessageItemLayoutBinding.inflate(LayoutInflater.from(context))
        return MyViewHolder1(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(list[position])
        holder.itemView.setOnClickListener {
            onItemClick?.invoke(list[position])
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }


}