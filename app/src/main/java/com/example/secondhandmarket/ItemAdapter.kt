package com.example.secondhandmarket

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.secondhandmarket.databinding.ItemBinding
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso

class ItemAdapter(private var itemList: List<ItemModel>): RecyclerView.Adapter<ItemAdapter.ViewHolder>() {

    private lateinit var mListener: onItemClickListener
    private var imgUri : String = ""

    interface onItemClickListener {
        fun onItemClick(position: Int)
    }
    fun updateList(newList: List<ItemModel>) {
        itemList = newList
        notifyDataSetChanged()
    }

    fun setOnItemClickListener(clickListener: ItemAdapter.onItemClickListener) {
        mListener = clickListener
    }

    inner class ViewHolder(var binding : ItemBinding) : RecyclerView.ViewHolder(binding.root)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    fun setImgUri(imgUri : String)  {
        this.imgUri = imgUri
    }
    fun getImgUri() : String {
        return this.imgUri
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = itemList[position]
        val imgUri = this.getImgUri()

        //이미지를 firebase storage에서 불러오기
        val storageReference = FirebaseStorage.getInstance().reference
        val imageReference = storageReference.child("image/item1.jpg")
        Log.d("로그", imgUri)

        imageReference.downloadUrl.addOnSuccessListener { uri ->
            Picasso.get()
                .load(uri)
                .into(holder.binding.itemImg)
        }.addOnFailureListener { e ->
            Log.e("loadItemImage", "Failed to load image. Error: $e")
        }


        holder.binding.itemTitle.text = currentItem.title

        //리사이클러 뷰의 아이템 클릭리스너
        holder.binding.root.setOnClickListener {
            mListener?.onItemClick(position)
        }

        // Check the status and set the appropriate text
        val statusText = if (currentItem.status == "판매 중") {
            "판매 중"
        } else {
            "판매완료"
        }

        holder.binding.itemStatus.text = statusText
        holder.binding.itemPrice.text = currentItem.price
    }
}




