package com.example.myrecipeapp.Activities

import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast

import com.example.myrecipeapp.R
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.auth.api.signin.GoogleSignInResult
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider

class UserLogin : AppCompatActivity() {

    private var emailField: EditText? = null
    private var passwordField: EditText? = null
    private var loginBtn: Button? = null
    private var signupBtn: TextView? = null
    private var userEmail: String? = null
    private var userPassword: String? = null
    internal lateinit var registorAuth: FirebaseAuth

    internal lateinit var regProgress: ProgressBar
    internal var netConnection: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_login)
        init()
        regProgress.visibility = View.GONE
        registorAuth = FirebaseAuth.getInstance()
        checkConnection()
        loginBtn!!.setOnClickListener {v ->

            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            imm?.hideSoftInputFromWindow(v.windowToken, 0)

            userEmail = emailField!!.text.toString().trim { it <= ' ' }
            userPassword = passwordField!!.text.toString().trim { it <= ' ' }


            if (userEmail!!.isEmpty()) {
                emailField!!.error = "Email Required"
            } else if (userPassword!!.isEmpty()) {
                passwordField!!.error = "password Required"
            } else if (userPassword!!.length < 6) {
                passwordField!!.error = "At least 6 character password"
            } else if (!Patterns.EMAIL_ADDRESS.matcher(userEmail!!).matches()) {
                emailField!!.error = "Wrong format"
            } else if (netConnection) {
                regProgress.visibility = View.VISIBLE

                registorAuth.signInWithEmailAndPassword(userEmail!!, userPassword!!).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val userData = Intent(this@UserLogin, MainActivity::class.java)
                        userData.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(userData)
                        finish()
                    } else {
                        val massage = task.exception!!.toString()
                        regProgress.visibility = View.GONE
                        window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
                        emailField!!.setText("")
                        passwordField!!.setText("")
                        Toast.makeText(this@UserLogin, massage, Toast.LENGTH_LONG).show()
                    }
                }
            } else {
                Toast.makeText(this@UserLogin, "Please Check Internet connection", Toast.LENGTH_SHORT).show()
            }
        }

        signupBtn!!.setOnClickListener {
            val intent = Intent(this@UserLogin, UserSignup::class.java)
            startActivity(intent)
            finish()
        }

    }

    override fun onBackPressed() {
        AlertDialog.Builder(this@UserLogin)
                .setTitle("Exit Application !")
                .setIcon(R.drawable.exit)
                .setMessage("Are you sure to Exit!")
                .setPositiveButton("yes") { dialogInterface, i -> finish() }
                .setNegativeButton("No") { dialogInterface, i -> dialogInterface.cancel() }.show()
    }

    private fun init() {
        emailField = findViewById(R.id.emailLoginField)
        passwordField = findViewById(R.id.passwordLoginField)
        loginBtn = findViewById(R.id.loginButton)
        signupBtn = findViewById(R.id.newUserBtn)
        regProgress = findViewById(R.id.progressBar)
    }

    fun checkConnection() {
        netConnection = false
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)!!.state == NetworkInfo.State.CONNECTED || connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)!!.state == NetworkInfo.State.CONNECTED) {
            //we are connected to a network
            netConnection = true
        } else {
            netConnection = false
        }
    }

}
