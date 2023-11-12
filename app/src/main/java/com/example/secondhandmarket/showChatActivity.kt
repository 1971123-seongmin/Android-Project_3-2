package com.example.secondhandmarket

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class showChatActivity : AppCompatActivity() {
    private lateinit var msgStorageRef: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_chat)

        val senderID = findViewById<TextView>(R.id.senderID)
        msgStorageRef = FirebaseDatabase.getInstance().getReference("Message")

    }
}