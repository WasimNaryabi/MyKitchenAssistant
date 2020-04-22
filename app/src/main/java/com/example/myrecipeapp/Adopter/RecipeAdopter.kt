package com.example.myrecipeapp.Adopter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast

import com.bumptech.glide.Glide
import com.example.myrecipeapp.Activities.RecipeHome
import com.example.myrecipeapp.Model.Recipe
import com.example.myrecipeapp.R
import com.squareup.picasso.Picasso

import java.util.ArrayList
import androidx.recyclerview.widget.RecyclerView
import androidx.appcompat.app.AppCompatActivity as AppCompatActivity1

class RecipeAdopter(internal var context: Context, recipes: ArrayList<Recipe>, internal var child: String, internal var cat: String) : RecyclerView.Adapter<RecipeAdopter.myViewHolder>() {

    internal lateinit var activity: androidx.appcompat.app.AppCompatActivity
    internal var recipes = ArrayList<Recipe>()
    internal lateinit var mImage: String
    internal lateinit var mTitle: String
    internal lateinit var mTime: String
    internal lateinit var mCost: String
    internal lateinit var mPeople: String
    internal lateinit var mDescription: String
    internal lateinit var mIngredients: String

    init {
        this.recipes = recipes
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): myViewHolder {

        return myViewHolder(LayoutInflater.from(context).inflate(R.layout.layout_item, parent, false))
    }

    override fun onBindViewHolder(holder: myViewHolder, position: Int) {

        activity = context as AppCompatActivity1
        val mRecipes = recipes[position]

        mImage = mRecipes.Path
        mTitle = mRecipes.Title
        mTime = mRecipes.Time
        mCost = mRecipes.Cost
        mDescription = mRecipes.Description
        mPeople = mRecipes.People
        mIngredients = mRecipes.Ingredients

        holder.itemView.setOnClickListener {
            val intent = Intent(context, RecipeHome::class.java)
            intent.putExtra("eRecipeCat", cat)
            intent.putExtra("eRecipeChild", child)
            intent.putExtra("eRecipeTitle", mRecipes.Title)
            intent.putExtra("eRecipeDescription", mRecipes.Description)
            intent.putExtra("eRecipeIngredients", mRecipes.Ingredients)
            intent.putExtra("eRecipeTime", mRecipes.Time)
            intent.putExtra("eRecipeCost", mRecipes.Cost)
            intent.putExtra("eRecipePeople", mRecipes.People)
            intent.putExtra("eRecipeImage", mRecipes.Path)
            context.startActivity(intent)
            activity.finish()
        }

        holder.mTitleView.text = mTitle
        holder.mTimeView.text = mTime
        holder.mCostView.text = mCost
        Glide.with(context!!).load(mImage).into(holder.mRecipeImage)


    }

    override fun getItemCount(): Int {
        return recipes.size
    }

    inner class myViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        internal var mRecipeImage: ImageView
        internal var mTitleView: TextView
        internal var mTimeView: TextView
        internal var mCostView: TextView


        init {
            mRecipeImage = itemView.findViewById<View>(R.id.dRecipePic) as ImageView
            mTitleView = itemView.findViewById<View>(R.id.dRecipeTitle) as TextView
            mCostView = itemView.findViewById<View>(R.id.dRecipeCost) as TextView
            mTimeView = itemView.findViewById<View>(R.id.dRecipeTime) as TextView
        }
    }
}
