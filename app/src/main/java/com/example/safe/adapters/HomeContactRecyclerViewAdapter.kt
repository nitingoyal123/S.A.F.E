package com.example.safe.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.safe.databinding.ContactItemLayoutBinding
import com.example.safe.model.Contact

class HomeContactRecyclerViewAdapter(var context: Context, var list : MutableList<Contact>) : RecyclerView.Adapter<HomeContactRecyclerViewAdapter.MyViewHolder>() {

    class MyViewHolder(var binding : ContactItemLayoutBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(contact: Contact) {
            binding.contact = contact
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ContactItemLayoutBinding.inflate(LayoutInflater.from(context))
        return MyViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(list[position])
    }

}