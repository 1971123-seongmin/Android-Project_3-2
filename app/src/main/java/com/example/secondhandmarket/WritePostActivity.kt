package com.example.secondhandmarket

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class WritePostActivity : AppCompatActivity() {
    private lateinit var dbRef: DatabaseReference
    private lateinit var storageRef: StorageReference
    private lateinit var titleEditText: EditText
    private lateinit var descriptionEditText: EditText
    private lateinit var priceEditText: EditText
    private lateinit var uploadButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.writepost)

        // Firebase Database 및 Storage 초기화
        dbRef = FirebaseDatabase.getInstance().getReference("Items")
        storageRef = FirebaseStorage.getInstance().getReference()
        val mAuth = FirebaseAuth.getInstance()
        val currentUser: FirebaseUser? = mAuth.currentUser

        titleEditText = findViewById(R.id.cTitle)
        descriptionEditText = findViewById(R.id.cDes)
        priceEditText = findViewById(R.id.cPrice)
        uploadButton = findViewById(R.id.writepostbtn)
        uploadButton.setOnClickListener(View.OnClickListener {
            val title = titleEditText.text.toString()
            val description = descriptionEditText.text.toString()
            val price = priceEditText.text.toString()
            val userName = currentUser?.email

            // 글 작성 및 업로드
            uploadPost(title, description, price, userName)
            finish()
        })
    }

    // 글 작성 및 업로드
    private fun uploadPost(title: String, description: String, price: String, seller: String?) {
        val newItemRef: DatabaseReference = dbRef.push()

        // 글 정보를 Firebase Realtime Database에 업로드
        val item = ItemModel(title, description, true, price.toInt(), seller)
        newItemRef.setValue(item)
    }
}