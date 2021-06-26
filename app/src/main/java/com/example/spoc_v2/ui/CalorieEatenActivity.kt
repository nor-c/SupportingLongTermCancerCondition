package com.example.spoc_v2.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.CheckBox
import android.widget.TextView
import com.example.spoc_v2.R
import com.example.spoc_v2.ui.calorie.DietaryActivity
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_daily_calorie.*
import java.lang.Thread.sleep
import java.math.BigDecimal
import java.math.RoundingMode

class CalorieEatenActivity : AppCompatActivity() {

    lateinit var fullBreakfast: CheckBox
    lateinit var halfBreakfast: CheckBox
    lateinit var twotoast: CheckBox
    lateinit var cereals: CheckBox

    lateinit var soup: CheckBox
    lateinit var fishchips: CheckBox
    lateinit var sandwich: CheckBox
    lateinit var rollsausages: CheckBox

    lateinit var meatvegetables: CheckBox
    lateinit var chickendish: CheckBox
    lateinit var steakchips: CheckBox
    lateinit var pastasauce: CheckBox

    lateinit var granolaBar: CheckBox
    lateinit var marsBar: CheckBox
    lateinit var crips: CheckBox
    lateinit var iceCream: CheckBox

    private var database = FirebaseFirestore.getInstance()
    private val currentUser = HomeActivity.user

    private var result: TextView? = null
    val TAG = "CalorieEatenActivity"
    //private var feedback: TextView? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_daily_calorie)

        supportActionBar?.hide()

        calculateCalorie()



    }

    fun calculateCalorie() {

        fullBreakfast = findViewById(R.id.full_breakfast_checkBox)
        halfBreakfast = findViewById(R.id.half_breakfast_checkBox)
        twotoast = findViewById(R.id.twotoast_checkBox)
        cereals = findViewById(R.id.cereals_checkBox)

        soup = findViewById(R.id.soup_checkBox)
        fishchips = findViewById(R.id.fishchips_checkBox)
        sandwich = findViewById(R.id.min120_checkBox)
        rollsausages = findViewById(R.id.rollsausages_checkBox)

        meatvegetables = findViewById(R.id.meatvegetables_checkBox)
        chickendish = findViewById(R.id.chickendish_checkBox)
        steakchips = findViewById(R.id.steakchips_checkBox)
        pastasauce = findViewById(R.id.pastasauce_checkBox)

        granolaBar = findViewById(R.id.snack1_checkBox)
        marsBar = findViewById(R.id.marsBar_checkBox)
        crips = findViewById(R.id.crips_checkBox)
        iceCream = findViewById(R.id.iceCream_checkBox)

        var totalAmount: Int = 0
        fullBreakfast.setOnClickListener {
            if(fullBreakfast.isChecked) {
                totalAmount += 1500
                displayCalorie(totalAmount)
            }
            else{
                totalAmount -= 1500
                displayCalorie(totalAmount)
            }
        }
        halfBreakfast.setOnClickListener {
            if(halfBreakfast.isChecked) {
                totalAmount += 750
                displayCalorie(totalAmount)
            }
            else{
                totalAmount -= 750
                displayCalorie(totalAmount)
            }
        }
        if(fullBreakfast.isChecked) {
            totalAmount += 1500
            displayCalorie(totalAmount)
        }
        if(halfBreakfast.isChecked) {
            totalAmount += 1500
            displayCalorie(totalAmount)
        }
        twotoast.setOnClickListener {
            if(twotoast.isChecked){
                totalAmount += 250
                displayCalorie(totalAmount)
            }
            else{
                totalAmount -= 250
                displayCalorie(totalAmount)
                }
        }
        cereals.setOnClickListener {
            if(cereals.isChecked) {
                totalAmount += 380
                displayCalorie(totalAmount)
            }
            else{
                totalAmount -= 380
                displayCalorie(totalAmount)
            }
        }
        soup.setOnClickListener {
            if(soup.isChecked) {
                totalAmount += 190
                displayCalorie(totalAmount)
            }
            else{
                totalAmount -= 190
                displayCalorie(totalAmount)
            }
        }
        fishchips.setOnClickListener {
            if(fishchips.isChecked) {
                totalAmount += 840
                displayCalorie(totalAmount)
            }
            else{
                totalAmount -= 840
                displayCalorie(totalAmount)
            }
        }
        rollsausages.setOnClickListener {
            if(rollsausages.isChecked) {
                totalAmount += 150
                displayCalorie(totalAmount)
            }
            else{
                totalAmount -= 150
                displayCalorie(totalAmount)
            }
        }
        sandwich.setOnClickListener {
            if(sandwich.isChecked) {
                totalAmount += 304
                displayCalorie(totalAmount)
            }
            else{
                totalAmount -= 304
                displayCalorie(totalAmount)
            }
        }
        meatvegetables.setOnClickListener {
            if(meatvegetables.isChecked) {
                totalAmount += 658
                displayCalorie(totalAmount)
            }
            else{
                totalAmount -= 658
                displayCalorie(totalAmount)
            }
        }
        chickendish.setOnClickListener {
            if(chickendish.isChecked) {
                totalAmount += 440
                displayCalorie(totalAmount)
            }
            else{
                totalAmount -= 440
                displayCalorie(totalAmount)
            }
        }
        steakchips.setOnClickListener {
            if(steakchips.isChecked) {
                totalAmount += 785
                displayCalorie(totalAmount)
            }
            else{
                totalAmount -= 785
                displayCalorie(totalAmount)
            }
        }
        pastasauce.setOnClickListener {
            if(pastasauce.isChecked) {
                totalAmount += 470
                displayCalorie(totalAmount)
            }
            else{
                totalAmount -= 470
                displayCalorie(totalAmount)
            }
        }
        granolaBar.setOnClickListener {
            if(granolaBar.isChecked) {
                totalAmount += 250
                displayCalorie(totalAmount)
            }
            else{
                totalAmount -= 250
                displayCalorie(totalAmount)
            }
        }
        marsBar.setOnClickListener {
            if(marsBar.isChecked) {
                totalAmount += 230
                displayCalorie(totalAmount)
            }
            else{
                totalAmount -= 230
                displayCalorie(totalAmount)
            }
        }
        crips.setOnClickListener {
            if(crips.isChecked) {
                totalAmount += 234
                displayCalorie(totalAmount)
            }
            else{
                totalAmount -= 234
                displayCalorie(totalAmount)
            }
        }
        iceCream.setOnClickListener {
            if(iceCream.isChecked) {
                totalAmount += 207
                displayCalorie(totalAmount)
            }
            else{
                totalAmount -= 207
                displayCalorie(totalAmount)
            }
        }
        calories_save_button.setOnClickListener {
            saveDetails()
        }

    }

    private fun saveDetails() {
        val calorieCalculated = calorie_result_tv.text.toString()


        if (currentUser != null) {

            database.collection(currentUser.username).document("bmr")
                .update(mapOf(
                    "calorieEaten" to calorieCalculated,
                    "calorieEaten2" to true,

                ))
                .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully written!") }
                .addOnFailureListener { e -> Log.w(TAG, "Error writing document", e) }
        }

        sleep(2000)
        val intent = Intent(this, DietaryActivity::class.java)
        startActivity(intent)
    }

    private fun displayCalorie(calorie: Int) {
        result = findViewById<View>(R.id.calorie_result_tv) as TextView
       // feedback = findViewById<View>(R.id.feedbck_calorie_result_tv) as TextView
        val bmrF = BigDecimal(calorie).setScale(1, RoundingMode.HALF_EVEN)
        val bmrMulti  = BigDecimal(calorie * 1000).setScale(0, RoundingMode.HALF_UP)
        //val multi = "Your Daily Caloric Intake: $bmrMulti"
        var bmiLabel = ""
        bmiLabel = """
            $bmrF
            
            $bmiLabel
            """.trimIndent()
        result!!.text = bmiLabel
        //feedback!!.text = multi
    }

}