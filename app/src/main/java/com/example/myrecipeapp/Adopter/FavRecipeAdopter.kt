package com.example.myrecipeapp.Adopter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView

import com.bumptech.glide.Glide
import com.example.myrecipeapp.Activities.RecipeHome
import com.example.myrecipeapp.Model.Recipe
import com.example.myrecipeapp.R

import java.util.ArrayList
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView

class FavRecipeAdopter(internal var context: Context, recipes: ArrayList<Recipe>) : RecyclerView.Adapter<FavRecipeAdopter.myViewHolder>() {

    internal lateinit var activity: Activity
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

        activity = context as AppCompatActivity
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
