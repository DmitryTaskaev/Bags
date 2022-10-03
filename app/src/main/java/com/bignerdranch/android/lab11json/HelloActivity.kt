package com.bignerdranch.android.lab11json

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler

class HelloActivity : AppCompatActivity() {

    private lateinit var time: Handler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hello)

        time = Handler()
        time.postDelayed({
            val reDir = Intent(this, TaskInfo::class.java)
            startActivity(reDir)
            finish()
        }, 3000)
    }
}