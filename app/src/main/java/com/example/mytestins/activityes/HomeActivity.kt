package com.example.mytestins.activityes

import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.example.mytestins.R
import com.example.mytestins.activityes.BaseActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_home.*


class HomeActivity : BaseActivity(0) {
    private val TAG = "HomeActivity"
    private lateinit var mAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        setupBottomNavigation()
        Log.d(TAG, "onCreate")
        setupBottomNavigation()
        mAuth = FirebaseAuth.getInstance()

        home_text.setOnClickListener {
            mAuth.signOut()

        }
        

    }

    override fun onStart() {
        super.onStart()
        if (mAuth.currentUser == null){
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }
}



