package com.example.myrecipeapp.Activities

import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView

import com.example.myrecipeapp.R

class AboutApp : AppCompatActivity() {

    internal lateinit var mBackBtn: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about_app)
        toolbarSupport()
        mBackBtn = findViewById(R.id.btnBack)

        mBackBtn.setOnClickListener { goBack() }
    }

    override fun onBackPressed() {
        goBack()
    }

    private fun goBack() {
        val intent = Intent(this@AboutApp, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun toolbarSupport() {
        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        val actionBar = supportActionBar
        actionBar!!.setDisplayShowTitleEnabled(false)
    }
}
