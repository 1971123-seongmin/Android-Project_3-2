package com.example.secondhandmarket

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
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
            val title = titleEditText.text.toString() + " 원"
            val description = descriptionEditText.text.toString()
            val price = priceEditText.text.toString()
            val userName = currentUser?.email
            val status = true

            // 글 작성 및 업로드
            uploadPost(title, description, price, userName, status)
            finish()
        })
    }
    // 글 작성 및 업로드
    private fun uploadPost(title: String, description: String, price: String, seller: String?, status:Boolean?) {
        // Firestore 컬렉션 "items"를 참조
        val itemsCollection = db.collection("items")

        // 새로운 문서를 생성하고 데이터를 설정
        val itemData = hashMapOf(
            "title" to title,
            "description" to description,
            "price" to price,
            "seller" to seller,
            "status" to status
        )

        // Firestore에 데이터 업로드
        itemsCollection.add(itemData)
            .addOnSuccessListener { documentReference ->
                val documentId = documentReference.id
                Toast.makeText(this, "글이 성공적으로 업로드되었습니다.", Toast.LENGTH_SHORT).show()

                // 업로드가 완료되었으므로 액티비티를 종료하여 이전 화면으로 돌아갈 수 있습니다.
                finish()
            }
            .addOnFailureListener { e ->
                // 업로드 실패 시 처리
                Log.d("글 업로드에 실패했습니다: $e","글 업로드에 실패했습니다: $e" )
            }
    }}