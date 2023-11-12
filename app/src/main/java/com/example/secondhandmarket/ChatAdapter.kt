package com.example.secondhandmarket

import android.util.Log
import android.view.LayoutInflater
import android.view.View

import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import org.w3c.dom.Text

data class chatItem(
    val msg : String? = null,
    val time : Long? = null,
    var timeString: String? = null
)
class ChatAdapter (val itemList : ArrayList<chatItem>) : RecyclerView.Adapter<ChatAdapter.ChatViewHolder>() {

    //뷰가 만들어질때 호출됨
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.chat_item, parent, false)
        return ChatViewHolder(view)
    }

    override fun getItemCount(): Int {
        return itemList.count()
    }

    //뷰가 바인드될 때 호출 -> 뷰에 내용이 씌워질때
    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        holder.msg.text = itemList[position].msg ?: "No"
        holder.time.text = itemList[position].timeString ?: "No time available"
    }

    //각 뷰들을 itemView.findViewById를 사용하여 해당 뷰를 연결
    inner class ChatViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val msg: TextView = itemView.findViewById(R.id.chatText) ?: TextView(itemView.context)
        val time: TextView = itemView.findViewById(R.id.time) ?: TextView(itemView.context)
    }
}