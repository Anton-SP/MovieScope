package com.home.moviescope.contacts

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.home.moviescope.databinding.ContactsItemBinding
import com.home.moviescope.model.Movie
import com.home.moviescope.recycler.CategoryAdapter

class ContactsAdapter():RecyclerView.Adapter<ContactsAdapter.RecyclerItemViewHolder>() {

    private var contactsList:MutableList<Contacts> = mutableListOf()

    fun insertData(data:Contacts) {
        this.contactsList.add(data)
        notifyDataSetChanged()
    }

    fun getContactsList():MutableList<Contacts>{
        return this.contactsList
    }

    interface onItemClickListener {
        fun onItemClick(itemView: View?, position: Int)
    }

    private lateinit var listener: onItemClickListener

    fun setOnItemClickListener(listener: ContactsAdapter.onItemClickListener) {
        this.listener = listener
    }

    inner class RecyclerItemViewHolder(val binding: ContactsItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

            fun bind(contact:Contacts) {
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    binding.name.text = contact.name
                    binding.phone.text = contact.phone
                }
            }

        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    listener.onItemClick(itemView, position)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerItemViewHolder {
       val binding = ContactsItemBinding.inflate(
           LayoutInflater.from(parent.context),
           parent,
           false
       )
        return  RecyclerItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerItemViewHolder, position: Int) {
        holder.bind(contactsList[position])
    }

    override fun getItemCount(): Int {
        return contactsList.size
    }



}