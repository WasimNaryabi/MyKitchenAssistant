package com.example.myrecipeapp.Activities

import androidx.appcompat.app.AppCompatActivity

import android.content.Intent
import android.os.Bundle
import android.os.Handler

import com.example.myrecipeapp.R

class SplashScreen : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        Handler().postDelayed({
            // This method will be executed once the timer is over
            val i = Intent(this@SplashScreen, MainActivity::class.java)
            startActivity(i)
            finish()
        }, 1000)
    }
}
