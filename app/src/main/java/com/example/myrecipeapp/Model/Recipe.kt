package com.example.myrecipeapp.Model

data class Recipe(
        var Path: String,
        var Description: String,
        var Ingredients: String,
        var People: String,
        var Title: String,
        var Time: String,
        var Cost: String
) {

    constructor() : this("", "", "", "","","","")
}

/*class Recipe {

    lateinit var cost: String
    lateinit var description: String
    lateinit var ingredients: String
    lateinit var path: String
    lateinit var people: String
    lateinit var time: String
    lateinit var title: String

    constructor() {

    }

    constructor() {
        this.cost = cost
        this.description = description
        this.ingredients = ingredients
        this.path = path
        this.people = people
        this.time = time
        this.title = title
    }
}*/
