package com.example.myrecipeapp.Activities

import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import android.widget.Toolbar

import com.bumptech.glide.Glide
import com.example.myrecipeapp.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

import java.util.HashMap

class RecipeHome : AppCompatActivity() {

    private var mRecipeTitle: TextView? = null
    private var mRecipeIngredients: TextView? = null
    private var mRecipeDescription: TextView? = null
    private var mRecipeTime: TextView? = null
    private var mRecipePeople: TextView? = null
    private var mRecipeCost: TextView? = null
    private lateinit var mImage: String
    private lateinit var mTitle: String
    private lateinit var mTime: String
    private lateinit var mCost: String
    private lateinit var mPeople: String
    private lateinit var mDescription: String
    private lateinit var mIngredients: String
    private var mChild: String? = null
    private var mCat: String? = null
    private var mRecipeImage: ImageView? = null
    private var mGetValue: Bundle? = null

    private var mBackBtn: ImageView? = null
    private var mShareBtn: ImageView? = null
    private var mFavBtnSelected: ImageView? = null
    private var mFavBtnNotSelected: ImageView? = null

    private var item: StringBuilder? = null

    private var rootRef: DatabaseReference? = null
    private var FavoriteNameRef: DatabaseReference? = null
    private var mFavoriteRecipe: DatabaseReference? = null
    private var mAuth: FirebaseAuth? = null

    private var currentUserId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recipe_home)
        init()
        toolbarSupport()

        mAuth = FirebaseAuth.getInstance()
        currentUserId = mAuth!!.currentUser!!.uid
        rootRef = FirebaseDatabase.getInstance().reference
        FavoriteNameRef = rootRef!!.child("Favorite")

        mGetValue = intent.extras
        mCat = mGetValue?.getString("eRecipeCat")
        mChild = mGetValue!!.getString("eRecipeChild")
        mTitle = mGetValue!!.getString("eRecipeTitle").toString()
        mCost = mGetValue!!.getString("eRecipeCost").toString()
        mTime = mGetValue!!.getString("eRecipeTime").toString()
        mPeople = mGetValue!!.getString("eRecipePeople").toString()
        mDescription = mGetValue!!.getString("eRecipeDescription").toString()
        mIngredients = mGetValue!!.getString("eRecipeIngredients").toString()
        mImage = mGetValue!!.getString("eRecipeImage").toString()
        mRecipeTitle!!.text = mTitle
        mRecipeCost!!.text = mCost
        mRecipeTime!!.text = mTime
        mRecipePeople!!.text = mPeople
        mRecipeIngredients!!.text = mIngredients
        mRecipeDescription!!.text = mDescription
        Glide.with(this@RecipeHome).load(mGetValue!!.getString("eRecipeImage")).into(mRecipeImage!!)

        mBackBtn!!.setOnClickListener { goBack() }

        mShareBtn!!.setOnClickListener {
            item!!.delete(0, item!!.length)
            item!!.append("Recipe:\n")
            item!!.append(mTitle!! + "\n")

            item!!.append("Ingredients:\n")
            item!!.append(mIngredients!! + "\n")

            item!!.append("Description:\n")
            item!!.append(mDescription!! + "\n")

            item!!.append("People:\n")
            item!!.append(mPeople!! + "\n")

            item!!.append("Cost:\n")
            item!!.append(mCost!! + "\n")

            item!!.append("Time:\n")
            item!!.append(mTime!! + "\n")

            val sharingIntent = Intent(android.content.Intent.ACTION_SEND)
            sharingIntent.type = "text/plain"
            val shareBody = item!!.toString()
            val shareSub = "Recipe :" + mTitle!!
            sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, shareSub)
            sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody)
            startActivity(Intent.createChooser(sharingIntent, "Share using"))
        }

        mFavBtnSelected!!.setOnClickListener {
            mFavBtnNotSelected!!.visibility = View.VISIBLE
            mFavBtnSelected!!.visibility = View.INVISIBLE
            removeFavRecipe()
            Toast.makeText(this@RecipeHome, "Selected Clicked", Toast.LENGTH_SHORT).show()
        }

        mFavBtnNotSelected!!.setOnClickListener {
            mFavBtnNotSelected!!.visibility = View.INVISIBLE
            mFavBtnSelected!!.visibility = View.VISIBLE
            addFavRecipe()
            Toast.makeText(this@RecipeHome, "Not Selected Clicked", Toast.LENGTH_SHORT).show()
        }
    }

    private fun addFavRecipe() {

        mFavoriteRecipe = FavoriteNameRef!!.child(currentUserId!!).child(mTitle!!)

        val AddMyRecipe = HashMap<String, Any>()
        AddMyRecipe["Cost"] = mCost
        AddMyRecipe["Description"] = mDescription
        AddMyRecipe["Ingredients"] = mIngredients
        AddMyRecipe["Path"] = mImage
        AddMyRecipe["People"] = mPeople
        AddMyRecipe["Time"] = mTime
        AddMyRecipe["Title"] = mTitle
        mFavoriteRecipe!!.updateChildren(AddMyRecipe)
        checkFavorite()
    }

    private fun removeFavRecipe() {
        mFavoriteRecipe = FavoriteNameRef!!.child(currentUserId!!).child(mTitle!!)
        mFavoriteRecipe!!.removeValue()
        checkFavorite()
    }

    private fun init() {
        mRecipeTitle = findViewById(R.id.recipeTitle)
        mRecipeCost = findViewById(R.id.recipeCost)
        mRecipeDescription = findViewById(R.id.recipeDescription)
        mRecipeIngredients = findViewById(R.id.recipeIngredients)
        mRecipeTime = findViewById(R.id.recipeTime)
        mRecipePeople = findViewById(R.id.recipePeople)
        mRecipeImage = findViewById(R.id.recipeImage)
        mBackBtn = findViewById(R.id.btnBack)
        mFavBtnSelected = findViewById(R.id.btnFavSelected)
        mFavBtnNotSelected = findViewById(R.id.btnFavNotSelected)
        mShareBtn = findViewById(R.id.btnShare)
        item = StringBuilder()
    }

    private fun checkFavorite() {
        rootRef!!.child("Favorite").child(currentUserId!!).child(mTitle!!).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    val retriveTitle = dataSnapshot.child("Title").value!!.toString()
                    if (retriveTitle == mTitle) {
                        Toast.makeText(this@RecipeHome, retriveTitle, Toast.LENGTH_SHORT).show()
                        mFavBtnSelected!!.visibility = View.VISIBLE
                    } else {
                        Toast.makeText(this@RecipeHome, retriveTitle, Toast.LENGTH_SHORT).show()
                        mFavBtnNotSelected!!.visibility = View.VISIBLE
                    }

                } else {
                    mFavBtnNotSelected!!.visibility = View.VISIBLE

                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Toast.makeText(this@RecipeHome, "No Data available", Toast.LENGTH_SHORT).show()
            }
        })

    }

    override fun onBackPressed() {

        goBack()

    }

    private fun goBack() {
        val nv = "NV"
        val v = "V"
        val d = "D"

        if (mCat == nv) {
            val intent = Intent(this@RecipeHome, NonVegeHome::class.java)
            intent.putExtra("Child", mChild)
            startActivity(intent)
            finish()
        } else if (mCat == v) {
            val intent = Intent(this@RecipeHome, VegeHome::class.java)
            intent.putExtra("Child", mChild)
            startActivity(intent)
            finish()
        } else if (mCat == d) {
            val intent = Intent(this@RecipeHome, DessertsHome::class.java)
            intent.putExtra("Child", mChild)
            startActivity(intent)
        } else {

            Toast.makeText(this, "Error:" + mCat!!, Toast.LENGTH_SHORT).show()
        }
    }

    private fun toolbarSupport() {
        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        val actionBar = supportActionBar
        actionBar!!.setDisplayShowTitleEnabled(false)
    }

    override fun onStart() {
        super.onStart()

        checkFavorite()
    }
}
