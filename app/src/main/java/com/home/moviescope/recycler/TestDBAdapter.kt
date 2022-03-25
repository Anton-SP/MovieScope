package com.home.moviescope.recycler

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.home.moviescope.R
import com.home.moviescope.databinding.TestDBItemBinding
import com.home.moviescope.model.Genres

class TestDBAdapter : RecyclerView.Adapter<TestDBAdapter.RecyclerItemViewHolder>() {
    private var data: List<Genres> = arrayListOf()

    fun setData(data: List<Genres>) {
        this.data = data
        notifyDataSetChanged()
    }

    inner class RecyclerItemViewHolder(val binding: TestDBItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(data: Genres) {
            if (layoutPosition != RecyclerView.NO_POSITION) {
                binding.recyclerViewItem.text = data.id.toString() + " " + data.name

            }
            itemView.setOnClickListener {
                Toast.makeText(
                    itemView.context,
                    "on click: ${data.name}",
                    Toast.LENGTH_SHORT
                )
                    .show()
            }
        }
    }


    override fun onBindViewHolder(holder: RecyclerItemViewHolder, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerItemViewHolder {
        val binding = TestDBItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return RecyclerItemViewHolder(binding)
    }


}