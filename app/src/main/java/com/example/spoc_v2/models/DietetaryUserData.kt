package com.example.spoc_v2.models

data class DietaryUserData (
    val id: String,
    val generalHealth: String? = null,
    val dietaryDate: String? = null,
    val surgeryDate: String? = null,
    val caloriesEaten: String? = null,
    val physicalActivity: String? = null,
    val emotions: String
    ){
    constructor() : this("", "","","","","","")
}

