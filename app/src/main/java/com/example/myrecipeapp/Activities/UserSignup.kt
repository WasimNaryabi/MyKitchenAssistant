package com.example.myrecipeapp.Activities

import androidx.appcompat.app.AppCompatActivity

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle
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
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

import java.util.HashMap

class UserSignup : AppCompatActivity() {

    private var nameField: EditText? = null
    private var emailField: EditText? = null
    private var passwordField: EditText? = null
    private var conPasswordField: EditText? = null
    private var signupBtn: Button? = null
    private var loginScreeBtn: TextView? = null
    private lateinit var userName: String
    private lateinit var userEmail: String
    private var userPassword: String? = null
    private var userConPassword: String? = null

    internal var netConnection: Boolean = false
    internal lateinit var registorAuth: FirebaseAuth
    internal lateinit var rootRef: DatabaseReference

    internal lateinit var regProgress: ProgressBar


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_signup)
        init()
        checkConnection()
        regProgress.visibility = View.GONE

        registorAuth = FirebaseAuth.getInstance()
        rootRef = FirebaseDatabase.getInstance().reference

        signupBtn!!.setOnClickListener { newUserRegistor() }

        loginScreeBtn!!.setOnClickListener {
            val loginScreen = Intent(this@UserSignup, UserLogin::class.java)
            startActivity(loginScreen)
            finish()
        }
    }

    private fun newUserRegistor() {
        userName = nameField!!.text.toString().trim { it <= ' ' }
        userEmail = emailField!!.text.toString().trim { it <= ' ' }
        userPassword = passwordField!!.text.toString().trim { it <= ' ' }
        userConPassword = conPasswordField!!.text.toString().trim { it <= ' ' }


        /*// use for keyboard hideing when button click
        InputMethodManager inputManager = (InputMethodManager)
                getSystemService(Context.INPUT_METHOD_SERVICE);

        inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);*/

        if (netConnection) {


            if (userName == "") {
                nameField!!.error = "Name required!"
            } else if (userEmail == "") {
                emailField!!.error = "Email required!"
            } else if (userPassword == "") {
                passwordField!!.error = "Password Required!"
            } else if (userPassword!!.length < 6) {
                passwordField!!.error = "At least 6 character password"
            } else if (!Patterns.EMAIL_ADDRESS.matcher(userEmail!!).matches()) {
                emailField!!.error = "Wrong format"
            } else if (userPassword != userConPassword) {
                conPasswordField!!.error = "password not match"
            } else {
                regProgress.visibility = View.VISIBLE
                window.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                        WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
                registorAuth.createUserWithEmailAndPassword(userEmail!!, userPassword!!)
                        .addOnCompleteListener(this@UserSignup) { task ->
                            if (task.isSuccessful) {

                                val currentUserId = registorAuth.currentUser!!.uid
                                val profileMap = HashMap<String, String>()
                                profileMap["userId"] = currentUserId
                                profileMap["name"] = userName
                                profileMap["email"] = userEmail
                                rootRef.child("Users").child(currentUserId).setValue(profileMap).addOnCompleteListener { task ->
                                    if (task.isComplete) {
                                        regProgress.visibility = View.GONE
                                        // getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

                                        /*Snackbar snackbar = Snackbar
                                            .make(findViewById(android.R.id.content), "Registration Successful", Snackbar.LENGTH_LONG);
                                    snackbar.show();*/


                                        Toast.makeText(this@UserSignup, "Registration Successful", Toast.LENGTH_SHORT).show()
                                        val userData = Intent(this@UserSignup, MainActivity::class.java)
                                        userData.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                        startActivity(userData)
                                        finish()
                                    } else {
                                        val massage = task.exception!!.toString()
                                        regProgress.visibility = View.GONE
                                        window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
                                        nameField!!.setText("")
                                        emailField!!.setText("")
                                        passwordField!!.setText("")
                                        conPasswordField!!.setText("")


                                        /* Snackbar snackbar = Snackbar
                                            .make(findViewById(android.R.id.content), massage, Snackbar.LENGTH_LONG);
                                    snackbar.show();
*/
                                        Toast.makeText(this@UserSignup, massage, Toast.LENGTH_SHORT).show()
                                    }
                                }


                            } else {
                                val massage = task.exception!!.toString()
                                regProgress.visibility = View.GONE
                                window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
                                nameField!!.setText("")
                                emailField!!.setText("")
                                passwordField!!.setText("")
                                conPasswordField!!.setText("")


                                /* Snackbar snackbar = Snackbar
                                            .make(findViewById(android.R.id.content), massage, Snackbar.LENGTH_LONG);
                                    snackbar.show();
*/
                                Toast.makeText(this@UserSignup, massage, Toast.LENGTH_SHORT).show()
                            }
                        }
                // Toast.makeText(StdLogin.this, "email is " + sEmail + " password is " + sPassword, Toast.LENGTH_SHORT).show();

            }
        } else {
            Toast.makeText(this@UserSignup, "check connection", Toast.LENGTH_SHORT).show()
        }

    }

    private fun init() {
        nameField = findViewById(R.id.nameSignUpField)
        emailField = findViewById(R.id.emailSignUpField)
        passwordField = findViewById(R.id.passwordSignUpField)
        conPasswordField = findViewById(R.id.conPasswordSignUpField)
        signupBtn = findViewById(R.id.signUpBtn)
        loginScreeBtn = findViewById(R.id.haveAccountBtn)
        regProgress = findViewById(R.id.progressBar)
    }

    override fun onBackPressed() {
        val intent = Intent(this@UserSignup, UserLogin::class.java)
        startActivity(intent)
        finish()
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
