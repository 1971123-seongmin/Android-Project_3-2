package com.example.secondhandmarket

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class chatActivity : AppCompatActivity() {

    private lateinit var sendBtn: Button
    private lateinit var editChat: EditText
    private lateinit var userStorageRef: DatabaseReference
    private lateinit var msgStorageRef: DatabaseReference
    private lateinit var recyclerView: RecyclerView
    private lateinit var chatAdapter: ChatAdapter
    private val itemList = ArrayList<chatItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        editChat = findViewById<EditText>(R.id.chat)
        sendBtn = findViewById<Button>(R.id.sendBtn)
        recyclerView = findViewById<RecyclerView>(R.id.rv_chat)
        recyclerView.visibility = View.GONE
        chatAdapter = ChatAdapter(itemList)

        val sender = FirebaseAuth.getInstance().currentUser?.email.toString() //현재 사용자 이메일
        val receiver = intent.getStringExtra("userEmail") //판매자 이메일 (메시지 수신)
        val senderEmail = findViewById<TextView>(R.id.senderEmail)
        senderEmail.text = receiver.toString() + "님과의 대화방"

        recyclerView.adapter = chatAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        msgStorageRef = FirebaseDatabase.getInstance().reference.child("Message")
        msgStorageRef.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                itemList.clear() // 새로운 데이터를 받을 때마다 리스트를 초기화

                    for (itemSnap in snapshot.children) {
                        val messageData = itemSnap.getValue(chatItem::class.java)

                        if (messageData != null) {
                            //현재 로그인한 사용자와 메시지를 보낸 사용자가 동일하며,
                            // 메시지 수신자와 액티비티에서 인텐트로 받은 메시지를 받는 판매자가 동일한 경우
                            if (messageData.sender == sender && messageData.receiver == receiver) {

                                // timestamp를 받아와서 변환
                                val timestamp = messageData.time ?: 0
                                val calendar = Calendar.getInstance()
                                calendar.timeInMillis = timestamp

                                // 시간에 9를 더함
                                calendar.add(Calendar.HOUR_OF_DAY, 9)

                                // SimpleDateFormat을 사용하여 원하는 형식으로 시간 문자열로 변환
                                val simpleDateFormat = SimpleDateFormat("H:mm", Locale.getDefault())
                                val formattedTime = simpleDateFormat.format(calendar.time)

                                // 변환된 시간 문자열을 chatItem에 설정
                                messageData.timeString = formattedTime
                                itemList.add(messageData)
                            }
                        } else {
                            Toast.makeText(this@chatActivity, "메시지 전송 오류", Toast.LENGTH_SHORT).show()
                        }
                    }
                    chatAdapter.notifyDataSetChanged()
                    if(chatAdapter.itemCount > 1)
                        recyclerView.smoothScrollToPosition(chatAdapter.itemCount - 1)
                    recyclerView.visibility = View.VISIBLE
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@chatActivity, "메시지 전송 실패", Toast.LENGTH_SHORT).show()
            }
        })


        sendBtn.setOnClickListener {
            val msg = editChat.text.toString()
            sendMsg(sender, receiver, msg)

            editChat.text.clear()
        }
    }

    private fun sendMsg(sender: String?, receiver: String?, msg: String?) {
        userStorageRef = FirebaseDatabase.getInstance().getReference("Items")
        msgStorageRef = FirebaseDatabase.getInstance().getReference("Message")

        val messageData = hashMapOf(
            "sender" to sender,
            "receiver" to receiver,
            "msg" to msg,
            "time" to ServerValue.TIMESTAMP
        )

        val newMessageRef = msgStorageRef.push()
        newMessageRef.setValue(messageData)
            .addOnSuccessListener {
                val messageId = newMessageRef.key
                Toast.makeText(this, "메시지 전송 성공", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "메시지 전송 실패", Toast.LENGTH_SHORT).show()
            }
    }
}
