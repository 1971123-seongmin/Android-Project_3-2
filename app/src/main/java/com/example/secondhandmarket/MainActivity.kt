package com.example.secondhandmarket

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

import com.example.secondhandmarket.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private val HomeFragment by lazy { HomeFragment() }
    private val ChatFragment by lazy { ChatFragment() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val user = FirebaseAuth.getInstance().currentUser
        val uid = user?.uid
        val email = user?.email

        Toast.makeText(this, email, Toast.LENGTH_SHORT).show()

        changeFragment(HomeFragment)
    }

    private fun changeFragment(homeFragment: HomeFragment) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_container, homeFragment)
            .commit()
    }
}
