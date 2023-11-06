package com.example.secondhandmarket

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.util.ArrayList

@Suppress("DEPRECATION")
class ItemAdapter(val itemList: ArrayList<ItemModel>): RecyclerView.Adapter<ItemAdapter.ViewHolder>() {

    private lateinit var mListener: onItemClickListener

    interface onItemClickListener {
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(clickListener: ItemAdapter.onItemClickListener) {
        mListener = clickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item, parent, false)
        return ViewHolder(itemView, mListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = itemList[position]
        holder.itemImg.setImageResource(R.mipmap.ic_launcher)
        holder.itemTitle.text = currentItem.title
        holder.itemStatus.text = currentItem.status.toString()
        holder.itemPrice.text = currentItem.price.toString()
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    class ViewHolder(itemView: View, clickListener: onItemClickListener) :
        RecyclerView.ViewHolder(itemView) {
        val itemImg: ImageView = itemView.findViewById(R.id.item_img)
        val itemTitle: TextView = itemView.findViewById(R.id.item_title)
        val itemStatus: TextView = itemView.findViewById(R.id.item_status)
        val itemPrice: TextView = itemView.findViewById(R.id.item_price)
        init {
            itemView.setOnClickListener {
                clickListener.onItemClick(adapterPosition)
            }
        }
    }
}




