package com.example.secondhandmarket

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class showChatActivity : AppCompatActivity() {
    private val itemList = ArrayList<MessageItem>()
    private lateinit var msgStorageRef: DatabaseReference
    private lateinit var messageAdapter: MessageAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_message)

        val recyclerView: RecyclerView = findViewById(R.id.rc_message)
        recyclerView.layoutManager = LinearLayoutManager(this)
        messageAdapter = MessageAdapter(itemList)
        recyclerView.adapter = messageAdapter

        //val senderID = findViewById<TextView>(R.id.senderID)
        val receiver = intent.getStringExtra("userEmail")
        val userEmail = FirebaseAuth.getInstance().currentUser?.email
        msgStorageRef = FirebaseDatabase.getInstance().getReference("Message")

        msgStorageRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                itemList.clear()

                for (snapshot in snapshot.children) {

                    // 여기에서 필요한 데이터를 추출하고 Message 객체를 생성하여 어댑터에 추가
                    val senderId: String? = snapshot.child("sender").getValue(String::class.java)
                    val receiverId: String? = snapshot.child("receiver").getValue(String::class.java)
                    val messageText: String? = snapshot.child("msg").getValue(String::class.java)
                    val time: Long? = snapshot.child("time").getValue(Long::class.java)

                    val messageItem = MessageItem(messageText, time, null, senderId, receiverId)
                    if (receiverId == userEmail) {
                        itemList.add(messageItem)
                    }
                }
                messageAdapter.notifyDataSetChanged()
            }
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@showChatActivity, "메시지 출력 실패", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
