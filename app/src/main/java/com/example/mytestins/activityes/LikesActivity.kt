package com.example.mytestins.activityes

import android.os.Bundle
import android.util.Log
import com.example.mytestins.R
import com.example.mytestins.activityes.BaseActivity


class LikesActivity : BaseActivity(3) {
    private val TAG = "LikesActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        setupBottomNavigation()
        Log.d(TAG, "onCreate")
    }
}



