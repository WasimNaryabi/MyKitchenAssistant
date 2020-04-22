package com.example.myrecipeapp.Activities

import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast

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
import java.util.HashSet

class NonVegeHome : AppCompatActivity() {

    internal lateinit var mBackBtn: ImageView
    internal lateinit var mRecipesList: RecyclerView
    internal lateinit var mRecipes: ArrayList<Recipe>
    internal lateinit var mRecipeAdopter: RecipeAdopter
    internal lateinit var mLinearLayoutManager: LinearLayoutManager
    //String mImage,mTitle,mTime,mCost,mPeople,mDescription,mIngredients;
    internal var mGetChild: String? = null
    internal var mRecipeCat: String? = null
    internal var mRecipe: Recipe? = null

    private val mAuth: FirebaseAuth? = null

    private var mSelectedRecipe: DatabaseReference? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_non_vege_home)
        init()
        toolbarSupport()
        val mExtra = intent.extras
        mGetChild = mExtra!!.getString("Child")
        mRecipeCat = "NV"
        mRecipeAdopter = RecipeAdopter(this@NonVegeHome, mRecipes, mGetChild!!, mRecipeCat!!)
        mSelectedRecipe = FirebaseDatabase.getInstance().reference.child("Recipe").child("NonVage").child("NV").child(mGetChild!!)

        mSelectedRecipe!!.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(dataSnapshot: DataSnapshot, s: String?) {
                if (dataSnapshot.exists()) {

                    mRecipe = dataSnapshot.getValue<Recipe>(Recipe::class.java)
                    mRecipes.add(mRecipe!!)
                    mRecipeAdopter.notifyDataSetChanged()
                    mRecipesList.adapter = mRecipeAdopter

                } else {
                    Toast.makeText(this@NonVegeHome, "Sorry some error ", Toast.LENGTH_SHORT).show()
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

        mBackBtn.setOnClickListener {
            val intent = Intent(this@NonVegeHome, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun init() {
        //recipeItem = findViewById(R.id.recipe_Select);
        mRecipesList = findViewById(R.id.item_list)
        mRecipes = ArrayList()
        mLinearLayoutManager = LinearLayoutManager(this)
        mRecipesList.layoutManager = mLinearLayoutManager
        mBackBtn = findViewById(R.id.btnBack)

        //mRecipeAdopter= new RecipeAdopter(NonVegeHome.this,mRecipes);
    }

    private fun toolbarSupport() {
        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        val actionBar = supportActionBar
        actionBar!!.setDisplayShowTitleEnabled(false)
    }

    override fun onBackPressed() {
        val intent = Intent(this@NonVegeHome, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}
