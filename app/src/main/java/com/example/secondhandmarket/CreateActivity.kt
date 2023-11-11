@file:Suppress("DEPRECATION")

package com.example.secondhandmarket

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.secondhandmarket.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class CreateActivity : AppCompatActivity() {
    private lateinit var mFirebaseAuth: FirebaseAuth
    private lateinit var mDatabaseRef: DatabaseReference
    private lateinit var mEtEmail: EditText
    private lateinit var mEtPwd: EditText
    private lateinit var mEtName: EditText
    private lateinit var mEtBirth: EditText
    private lateinit var mBtnRegister: Button
    private lateinit var backButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create)

        mFirebaseAuth = FirebaseAuth.getInstance()
        mDatabaseRef = FirebaseDatabase.getInstance().reference

        mEtEmail = findViewById(R.id.createemail)
        mEtPwd = findViewById(R.id.createpwd)
        mEtName = findViewById(R.id.cname)
        mEtBirth = findViewById(R.id.cbirth)

        backButton = findViewById(R.id.backbutton)
        backButton.setOnClickListener{
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
        mBtnRegister = findViewById(R.id.realCreateBtn)
        mBtnRegister.setOnClickListener {
            val strEmail = mEtEmail.text.toString()
            val strPwd = mEtPwd.text.toString()
            val strName = mEtName.text.toString()
            val strBirth = mEtBirth.text.toString()

            mFirebaseAuth.createUserWithEmailAndPassword(strEmail, strPwd)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        val firebaseUser = mFirebaseAuth.currentUser
                        val account = UserAccount().apply {
                            idToken = firebaseUser?.uid
                            emailId = firebaseUser?.email
                            password = strPwd
                            name = strName
                            birth = strBirth
                        }

                        mDatabaseRef.child("UserAccount").child(firebaseUser?.uid ?: "").setValue(account)

                        Toast.makeText(this, "회원가입 성공!", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this, LoginActivity::class.java)
                        startActivity(intent)
                    } else {
                        Toast.makeText(this, "회원가입 실패!", Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }

}