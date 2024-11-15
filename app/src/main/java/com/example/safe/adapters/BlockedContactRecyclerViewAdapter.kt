package com.example.safe.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.safe.databinding.ContactItemLayoutBinding
import com.example.safe.model.Contact

class BlockedContactRecyclerViewAdapter(var context: Context, var list : List<String>) : RecyclerView.Adapter<BlockedContactRecyclerViewAdapter.MyViewHolder>() {

    var onItemClick : ((Contact) -> Unit)? = null

    fun updateData(newBlockedContacts: List<String>) {
        list = newBlockedContacts
        notifyDataSetChanged()
    }

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
        var curr = Contact(phoneNumber = list[position], name = context.getSharedPreferences("contacts", Context.MODE_PRIVATE).getString(list[position], list[position]).toString(), blocked = true)
        holder.bind(curr)
        holder.itemView.setOnClickListener {
            onItemClick?.invoke(curr)
        }
    }
}