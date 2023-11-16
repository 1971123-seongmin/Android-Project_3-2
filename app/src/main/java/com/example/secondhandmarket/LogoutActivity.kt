package com.example.secondhandmarket

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Window
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class LogoutActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_home)

        val logoutButton: Button = findViewById(R.id.menu_Logout)
        auth = FirebaseAuth.getInstance()

        logoutButton.setOnClickListener{
            showCustomDialog()
        }
    }
        private fun showCustomDialog() {
            val dialogBinding = layoutInflater.inflate(R.layout.logout_dialog, null)

            val myDialog = Dialog(this)
            myDialog.setContentView(dialogBinding)

            myDialog.setCancelable(true)
            myDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            myDialog.show()

            val logoutMsg : TextView = myDialog.findViewById(R.id.logoutMsg)
            val btnYes : Button = myDialog.findViewById(R.id.btnYes)
            val btnNo : Button = myDialog.findViewById(R.id.btnNo)

            logoutMsg.text = "로그아웃 하시겠습니까?"

            btnYes.setOnClickListener{
                auth.signOut()

                val intent = Intent(this, LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                startActivity(intent)
            }

            btnNo.setOnClickListener{
                myDialog.dismiss()
            }

            myDialog.show()
        }
}
