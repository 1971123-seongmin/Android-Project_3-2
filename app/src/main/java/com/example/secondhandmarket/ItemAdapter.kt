package com.example.secondhandmarket

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.secondhandmarket.databinding.ItemBinding
import com.squareup.picasso.Picasso

class ItemAdapter(private val itemList: List<ItemModel>): RecyclerView.Adapter<ItemAdapter.ViewHolder>() {

    private lateinit var mListener: onItemClickListener

    interface onItemClickListener {
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(clickListener: ItemAdapter.onItemClickListener) {
        mListener = clickListener
    }

    inner class ViewHolder(var binding : ItemBinding) :
        RecyclerView.ViewHolder(binding.root)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = itemList[position]

        val imageUrl = currentItem.imgUri

        Picasso.get()
            .load(imageUrl)
            .into(holder.binding.itemImg)

        holder.binding.itemTitle.text = currentItem.title
        holder.binding.itemStatus.text = currentItem.status.toString()
        holder.binding.itemPrice.text = currentItem.price.toString()
    }
}




