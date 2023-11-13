package com.example.secondhandmarket

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

data class MessageItem(
    val msg : String? = null,
    val time : Long? = null,
    var timeString: String? = null,
    val sender: String? = null,
    val receiver: String? = null
)
class MessageAdapter (val itemList : ArrayList<MessageItem>) : RecyclerView.Adapter<MessageAdapter.MessageViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.message_item, parent, false)
        return MessageViewHolder(view)
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        val messageItem = itemList[position]
        holder.msg.text = messageItem.msg ?: "No"
        holder.time.text = messageItem.time?.let { convertTimeToString(it) } ?: "No time available"
        //holder.time.text = messageItem.timeString ?: "No time available"
        holder.sender.text = messageItem.sender ?: "No sender"
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    inner class MessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val msg: TextView = itemView.findViewById(R.id.messageTextView) ?: TextView(itemView.context)
        val time: TextView = itemView.findViewById(R.id.timeTextView) ?: TextView(itemView.context)
        val sender: TextView = itemView.findViewById(R.id.senderTextView) ?: TextView(itemView.context)
    }

    private fun convertTimeToString(time: Long): String {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = time
        calendar.add(Calendar.HOUR_OF_DAY, 9)
        val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
        return simpleDateFormat.format(calendar.time)
    }
}