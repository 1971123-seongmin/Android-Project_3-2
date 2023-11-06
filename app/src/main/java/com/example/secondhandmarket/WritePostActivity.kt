package com.example.secondhandmarket

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore

class WritePostActivity : AppCompatActivity() {
    private lateinit var db: FirebaseFirestore
    private lateinit var titleEditText: EditText
    private lateinit var descriptionEditText: EditText
    private lateinit var priceEditText: EditText
    private lateinit var uploadButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.writepost)

        // Firebase Firestore 초기화
        db = FirebaseFirestore.getInstance()

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
        // Firestore 컬렉션 "items"를 참조
        val itemsCollection = db.collection("items")

        // 새로운 문서를 생성하고 데이터를 설정
        val itemData = hashMapOf(
            "title" to title,
            "description" to description,
            "price" to price.toInt(),
            "seller" to seller
        )

        // Firestore에 데이터 업로드
        itemsCollection.add(itemData)
            .addOnSuccessListener { documentReference ->
                // 성공적으로 업로드된 경우
                val documentId = documentReference.id
                // 여기에서 추가적인 작업을 수행할 수 있음
            }
            .addOnFailureListener { e ->
                // 업로드 실패 시 처리
            }
    }
}