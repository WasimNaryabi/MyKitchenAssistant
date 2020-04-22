package com.example.myrecipeapp.Activities

import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import android.widget.Spinner
import android.widget.Toast

import com.example.myrecipeapp.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.Query
import com.google.firebase.database.ValueEventListener

import java.util.ArrayList

class MainActivity : AppCompatActivity() {

    private var spinner: Spinner? = null
    private var mNonVegeRef: DatabaseReference? = null
    private var mVegeRef: DatabaseReference? = null
    private var mDessertsRef: DatabaseReference? = null
    private var mNonVege: LinearLayout? = null
    private var mVege: LinearLayout? = null
    private var mDesserts: LinearLayout? = null
    private var mFavorite: LinearLayout? = null
    private var mAboutApp: LinearLayout? = null
    private var mAuth: FirebaseAuth? = null

    private var currentUser: FirebaseUser? = null
    private var rootRef: DatabaseReference? = null

    private var netConnection: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        init()
        toolbarSupport()

        mAuth = FirebaseAuth.getInstance()
        currentUser = FirebaseAuth.getInstance().currentUser
        Log.e("Message : ",currentUser.toString())
        rootRef = FirebaseDatabase.getInstance().reference

        mNonVegeRef = FirebaseDatabase.getInstance().reference.child("Recipe").child("NonVage")
        mVegeRef = FirebaseDatabase.getInstance().reference.child("Recipe").child("Vage")
        mDessertsRef = FirebaseDatabase.getInstance().reference.child("Recipe").child("Desserts")

        mNonVege!!.setOnClickListener {
            val mBuilder = AlertDialog.Builder(this@MainActivity)
            val vView = layoutInflater.inflate(R.layout.alert_dialog, null)
            spinner = vView.findViewById(R.id.spinner)
            mBuilder.setTitle("Select People")
            mNonVegeRef!!.addChildEventListener(object : ChildEventListener {
                override fun onChildAdded(dataSnapshot: DataSnapshot, s: String?) {

                    if (dataSnapshot.exists()) {
                        val titleList = ArrayList<String>()
                        for (dataSnapshot1 in dataSnapshot.children) {
                            val titlename = dataSnapshot1.key
                            titleList.add(titlename!!)

                        }
                        val arrayAdapter = ArrayAdapter(this@MainActivity, android.R.layout.simple_spinner_item, titleList)
                        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                        spinner!!.adapter = arrayAdapter

                    } else {
                        Toast.makeText(this@MainActivity, "data not exist", Toast.LENGTH_SHORT).show()
                    }

                }

                override fun onChildChanged(dataSnapshot: DataSnapshot, s: String?) {

                }

                override fun onChildRemoved(dataSnapshot: DataSnapshot) {

                }

                override fun onChildMoved(dataSnapshot: DataSnapshot, s: String?) {

                }

                override fun onCancelled(databaseError: DatabaseError) {

                }
            })
            mBuilder.setPositiveButton("Select") { dialogInterface, i ->
                val item = spinner!!.selectedItem.toString()
                if (item.isEmpty()) {
                    Toast.makeText(this@MainActivity, "Plz Select option", Toast.LENGTH_SHORT).show()
                    dialogInterface.dismiss()
                } else {

                    val nonVegeHome = Intent(this@MainActivity, NonVegeHome::class.java)
                    nonVegeHome.putExtra("Child", spinner!!.selectedItem.toString())
                    startActivity(nonVegeHome)
                    finish()


                }


                //Toast.makeText(MainActivity.this, "Selected "+spinner.getSelectedItem().toString(), Toast.LENGTH_SHORT).show();
            }
            mBuilder.setNegativeButton("Dismiss") { dialogInterface, i -> dialogInterface.dismiss() }
            mBuilder.setView(vView)
            val dialog = mBuilder.create()
            dialog.show()
        }

        mVege!!.setOnClickListener {
            val mBuilder = AlertDialog.Builder(this@MainActivity)
            val vView = layoutInflater.inflate(R.layout.alert_dialog, null)
            spinner = vView.findViewById(R.id.spinner)
            mBuilder.setTitle("Select People")
            mVegeRef!!.addChildEventListener(object : ChildEventListener {
                override fun onChildAdded(dataSnapshot: DataSnapshot, s: String?) {

                    if (dataSnapshot.exists()) {
                        val titleList = ArrayList<String>()
                        for (dataSnapshot1 in dataSnapshot.children) {
                            val titlename = dataSnapshot1.key
                            titleList.add(titlename!!)

                        }
                        val arrayAdapter = ArrayAdapter(this@MainActivity, android.R.layout.simple_spinner_item, titleList)
                        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                        spinner!!.adapter = arrayAdapter

                    } else {
                        Toast.makeText(this@MainActivity, "data not exist", Toast.LENGTH_SHORT).show()
                    }

                }

                override fun onChildChanged(dataSnapshot: DataSnapshot, s: String?) {

                }

                override fun onChildRemoved(dataSnapshot: DataSnapshot) {

                }

                override fun onChildMoved(dataSnapshot: DataSnapshot, s: String?) {

                }

                override fun onCancelled(databaseError: DatabaseError) {

                }
            })
            mBuilder.setPositiveButton("Select") { dialogInterface, i ->
                val item = spinner!!.selectedItem.toString()
                if (item.isEmpty()) {
                    Toast.makeText(this@MainActivity, "Plz Select option", Toast.LENGTH_SHORT).show()
                    dialogInterface.dismiss()
                } else {

                    val nonVegeHome = Intent(this@MainActivity, VegeHome::class.java)
                    nonVegeHome.putExtra("Child", spinner!!.selectedItem.toString())
                    startActivity(nonVegeHome)
                    finish()

                }


                //Toast.makeText(MainActivity.this, "Selected "+spinner.getSelectedItem().toString(), Toast.LENGTH_SHORT).show();
            }
            mBuilder.setNegativeButton("Dismiss") { dialogInterface, i -> dialogInterface.dismiss() }
            mBuilder.setView(vView)
            val dialog = mBuilder.create()
            dialog.show()
        }

        mDesserts!!.setOnClickListener {
            val mBuilder = AlertDialog.Builder(this@MainActivity)
            val vView = layoutInflater.inflate(R.layout.alert_dialog, null)
            spinner = vView.findViewById(R.id.spinner)
            mBuilder.setTitle("Select People")
            mDessertsRef!!.addChildEventListener(object : ChildEventListener {
                override fun onChildAdded(dataSnapshot: DataSnapshot, s: String?) {

                    if (dataSnapshot.exists()) {
                        val titleList = ArrayList<String>()
                        for (dataSnapshot1 in dataSnapshot.children) {
                            val titlename = dataSnapshot1.key
                            titleList.add(titlename!!)

                        }
                        val arrayAdapter = ArrayAdapter(this@MainActivity, android.R.layout.simple_spinner_item, titleList)
                        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                        spinner!!.adapter = arrayAdapter

                    } else {
                        Toast.makeText(this@MainActivity, "data not exist", Toast.LENGTH_SHORT).show()
                    }

                }

                override fun onChildChanged(dataSnapshot: DataSnapshot, s: String?) {

                }

                override fun onChildRemoved(dataSnapshot: DataSnapshot) {

                }

                override fun onChildMoved(dataSnapshot: DataSnapshot, s: String?) {

                }

                override fun onCancelled(databaseError: DatabaseError) {

                }
            })
            mBuilder.setPositiveButton("Select") { dialogInterface, i ->
                val item = spinner!!.selectedItem.toString()

                if (item.isEmpty()) {
                    Toast.makeText(this@MainActivity, "Plz Select option", Toast.LENGTH_SHORT).show()
                    dialogInterface.dismiss()
                } else {

                    val nonVegeHome = Intent(this@MainActivity, DessertsHome::class.java)
                    nonVegeHome.putExtra("Child", spinner!!.selectedItem.toString())
                    startActivity(nonVegeHome)
                    finish()
                }

                //Toast.makeText(MainActivity.this, "Selected "+spinner.getSelectedItem().toString(), Toast.LENGTH_SHORT).show();
            }
            mBuilder.setNegativeButton("Dismiss") { dialogInterface, i -> dialogInterface.dismiss() }
            mBuilder.setView(vView)
            val dialog = mBuilder.create()
            dialog.show()
        }

        mFavorite!!.setOnClickListener {
            val myFavorite = Intent(this@MainActivity, MyFavorite::class.java)
            startActivity(myFavorite)
            finish()
        }

        mAboutApp!!.setOnClickListener {
            val myFavorite = Intent(this@MainActivity, AboutApp::class.java)
            startActivity(myFavorite)
            finish()
        }


    }

    private fun init() {

        mNonVege = findViewById(R.id.non_vege)
        mVege = findViewById(R.id.vege)
        mDesserts = findViewById(R.id.desserts)
        mFavorite = findViewById(R.id.favorite)
        mAboutApp = findViewById(R.id.aboutapp)

    }

    private fun toolbarSupport() {
        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        val actionBar = supportActionBar
        actionBar!!.setDisplayShowTitleEnabled(false)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.home_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle item selection
        when (item.itemId) {

            R.id.action_logout -> {
                mAuth!!.signOut()
                startActivity(Intent(this@MainActivity, UserLogin::class.java))
                finish()
                Toast.makeText(this, "Profile clicked", Toast.LENGTH_SHORT).show()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onStart() {
        super.onStart()
        if (currentUser == null) {
            gotologinPage()
        } else {
            checkUserExistence()
        }
    }

    override fun onBackPressed() {
        androidx.appcompat.app.AlertDialog.Builder(this@MainActivity)
                .setTitle("Exit Application !")
                .setIcon(R.drawable.exit)
                .setMessage("Are you sure to Exit!")
                .setPositiveButton("yes") { dialogInterface, i -> finish() }
                .setNegativeButton("No") { dialogInterface, i -> dialogInterface.cancel() }.show()
    }

    private fun checkUserExistence() {
        val currentUserId = mAuth!!.currentUser!!.uid
        rootRef!!.child("Users").child(currentUserId).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.child("name").exists()) {
                    Toast.makeText(this@MainActivity, "Welcome", Toast.LENGTH_SHORT).show()
                }

            }

            override fun onCancelled(databaseError: DatabaseError) {

            }
        })

    }

    private fun gotologinPage() {
        val intent = Intent(this@MainActivity, UserLogin::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
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
