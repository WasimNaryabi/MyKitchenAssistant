package com.example.myrecipeapp.Activities

import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Button
import android.widget.TextView
import android.widget.Toast

import com.example.myrecipeapp.Adopter.FavRecipeAdopter
import com.example.myrecipeapp.Adopter.RecipeAdopter
import com.example.myrecipeapp.Model.Recipe
import com.example.myrecipeapp.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

import java.util.ArrayList

class MyFavorite : AppCompatActivity() {


    private var mRecipesList: RecyclerView? = null
    private var mRecipes: ArrayList<Recipe>? = null
    private var mRecipeAdopter: FavRecipeAdopter? = null
    private var mLinearLayoutManager: LinearLayoutManager? = null

    private var mUserEmail: TextView? = null
    private var mUserName: TextView? = null

    private var mRecipe: Recipe? = null
    private val dpName: String? = null
    private val dpEmail: String? = null
    private var currentUserId: String? = null

    private var mAuth: FirebaseAuth? = null
    private var rootRef: DatabaseReference? = null
    private var mSelectedRecipe: DatabaseReference? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_favorite)
        init()
        toolbarSupport()
        mAuth = FirebaseAuth.getInstance()
        currentUserId = mAuth!!.currentUser!!.uid
        rootRef = FirebaseDatabase.getInstance().reference

        mSelectedRecipe = FirebaseDatabase.getInstance().reference.child("Favorite").child(currentUserId!!)

        mSelectedRecipe!!.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(dataSnapshot: DataSnapshot, s: String?) {
                if (dataSnapshot.exists()) {
                    mRecipe = dataSnapshot.getValue<Recipe>(Recipe::class.java!!)
                    mRecipes!!.add(mRecipe!!)
                    mRecipeAdopter!!.notifyDataSetChanged()
                    mRecipesList!!.adapter = mRecipeAdopter

                } else {
                    Toast.makeText(this@MyFavorite, "Sorry some error ", Toast.LENGTH_SHORT).show()
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


    }

    private fun init() {

        mRecipesList = findViewById(R.id.item_list)
        mRecipes = ArrayList()
        mLinearLayoutManager = LinearLayoutManager(this)
        mRecipesList!!.layoutManager = mLinearLayoutManager
        mRecipeAdopter = FavRecipeAdopter(this@MyFavorite, mRecipes!!)
        mUserName = findViewById(R.id.userName)
        mUserEmail = findViewById(R.id.userEmail)

    }

    private fun toolbarSupport() {
        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        val actionBar = supportActionBar
        actionBar!!.setDisplayShowTitleEnabled(false)
    }

    private fun getUserdata() {
        rootRef!!.child("Users").child(currentUserId!!).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    val retriveUserName = dataSnapshot.child("name").value!!.toString()
                    val retriveUserEmail = dataSnapshot.child("email").value!!.toString()

                    mUserName!!.text = retriveUserName
                    mUserEmail!!.text = retriveUserEmail

                } else {

                }
            }

            override fun onCancelled(databaseError: DatabaseError) {

            }
        })

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
                startActivity(Intent(this@MyFavorite, UserLogin::class.java))
                finish()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onStart() {
        super.onStart()
        getUserdata()
    }

    override fun onBackPressed() {
        val intent = Intent(this@MyFavorite, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}
