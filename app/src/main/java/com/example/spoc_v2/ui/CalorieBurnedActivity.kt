package com.example.spoc_v2.ui

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.CheckBox
import android.widget.TextView
import androidx.core.view.isGone
import com.example.spoc_v2.R
import com.example.spoc_v2.ui.calorie.DietaryActivity
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_b_m_r.*
import kotlinx.android.synthetic.main.activity_calorie_burned.*
import java.math.BigDecimal
import java.math.RoundingMode
import kotlin.math.roundToInt

class CalorieBurnedActivity : AppCompatActivity() {

    lateinit var slowWalk: CheckBox
    lateinit var normalWalk: CheckBox
    lateinit var fastWalk: CheckBox
    lateinit var veryFastWalk: CheckBox

    lateinit var min30: CheckBox
    lateinit var min60: CheckBox
    lateinit var min90: CheckBox
    lateinit var min120: CheckBox
    private lateinit var result: TextView

    private var database = FirebaseFirestore.getInstance()
    private val currentUser = HomeActivity.user

    val TAG = "CalorieBurnedActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calorie_burned)


        calculateWalk()

    }

    @SuppressLint("SetTextI18n")
    private fun calculateWalk() {

//        slowWalk = findViewById(R.id.slow_checkBox)
//        normalWalk = findViewById(R.id.normal_checkBox)
//        fastWalk = findViewById(R.id.fast_checkBox)
//        veryFastWalk = findViewById(R.id.very_fast_checkBox)
//
//        min30 = findViewById(R.id.min30_checkBox)
//        min60 = findViewById(R.id.min60_checkBox)
//        min90 = findViewById(R.id.min90_checkBox)
//        min120 = findViewById(R.id.min120_checkBox)


        val slow = 2.3 * 0.0175
        val normal = 2.9 * 0.0175
        val fast = 3.3 * 0.0175
        val veryFast = 3.6 * 0.0175
        //val multiMet = 0.0175


        if (currentUser != null) {
            val ref = database.collection(currentUser.username).document("bmr")

            ref.get().addOnSuccessListener { document ->
                var weightStr = document["weight"].toString()
                val weight = weightStr.toInt()
                var total: Int = 0
                var walkTotal: Int = 0


                slow_img.setOnClickListener{
                    total = (weight * slow).roundToInt()
                    slow_img.visibility = View.GONE
                    normal_img_view.visibility = View.GONE
                    fast_img_view.visibility = View.GONE
                    veryfast_img_view.visibility = View.GONE
                    walking_intensity_tv.text = " Slow "
                }
                normal_img_view.setOnClickListener{
                    total = (weight * normal).roundToInt()
                    slow_img.visibility = View.GONE
                    normal_img_view.visibility = View.GONE
                    fast_img_view.visibility = View.GONE
                    veryfast_img_view.visibility = View.GONE
                    walking_intensity_tv.text = " Normal "
                }
                fast_img_view.setOnClickListener{
                    total = (weight * fast).roundToInt()
                    slow_img.visibility = View.GONE
                    normal_img_view.visibility = View.GONE
                    fast_img_view.visibility = View.GONE
                    veryfast_img_view.visibility = View.GONE
                    walking_intensity_tv.text = " Fast "
                }
                veryfast_img_view.setOnClickListener{
                    total = (weight * veryFast).roundToInt()
                    slow_img.visibility = View.GONE
                    normal_img_view.visibility = View.GONE
                    fast_img_view.visibility = View.GONE
                    veryfast_img_view.visibility = View.GONE
                    walking_intensity_tv.text = " Very Fast "
                }
                min30_img_tv.setOnClickListener{
                    walkTotal = (total * 30)
                    min30_img_tv.visibility = View.GONE
                    min60_img_tv.visibility = View.GONE
                    min90_img_tv.visibility = View.GONE
                    min120_img_tv.visibility = View.GONE
                    min150_img_tv.visibility = View.GONE
                    min180_img_tv.visibility = View.GONE
                    walking_time_hours_tv.text = " Half Hour "
                    displayCalorieBurned(walkTotal)
                }
                min60_img_tv.setOnClickListener{
                    walkTotal = (total * 60)
                    min30_img_tv.visibility = View.GONE
                    min60_img_tv.visibility = View.GONE
                    min90_img_tv.visibility = View.GONE
                    min120_img_tv.visibility = View.GONE
                    min150_img_tv.visibility = View.GONE
                    min180_img_tv.visibility = View.GONE
                    walking_time_hours_tv.text = " One Hour "
                    displayCalorieBurned(walkTotal)
                }
                min90_img_tv.setOnClickListener{
                    walkTotal = (total * 90)
                    min30_img_tv.visibility = View.GONE
                    min60_img_tv.visibility = View.GONE
                    min90_img_tv.visibility = View.GONE
                    min120_img_tv.visibility = View.GONE
                    min150_img_tv.visibility = View.GONE
                    min180_img_tv.visibility = View.GONE
                    walking_time_hours_tv.text = " One and a Half Hour "
                    displayCalorieBurned(walkTotal)
                }
                min120_img_tv.setOnClickListener{
                    walkTotal = (total * 120)
                    min30_img_tv.visibility = View.GONE
                    min60_img_tv.visibility = View.GONE
                    min90_img_tv.visibility = View.GONE
                    min120_img_tv.visibility = View.GONE
                    min150_img_tv.visibility = View.GONE
                    min180_img_tv.visibility = View.GONE
                    walking_time_hours_tv.text = " Two Hours "
                    displayCalorieBurned(walkTotal)
                }
                min150_img_tv.setOnClickListener{
                    walkTotal = (total * 150)
                    min30_img_tv.visibility = View.GONE
                    min60_img_tv.visibility = View.GONE
                    min90_img_tv.visibility = View.GONE
                    min120_img_tv.visibility = View.GONE
                    min150_img_tv.visibility = View.GONE
                    min180_img_tv.visibility = View.GONE
                    walking_time_hours_tv.text = " Two and a Half Hour "
                    displayCalorieBurned(walkTotal)
                }
                min180_img_tv.setOnClickListener{
                    walkTotal = (total * 180)
                    min30_img_tv.visibility = View.GONE
                    min60_img_tv.visibility = View.GONE
                    min90_img_tv.visibility = View.GONE
                    min120_img_tv.visibility = View.GONE
                    min150_img_tv.visibility = View.GONE
                    min180_img_tv.visibility = View.GONE
                    walking_time_hours_tv.text = " Three hours "
                    displayCalorieBurned(walkTotal)
                }
                walking_save_button.setOnClickListener {
                    saveDetails()
                }

            }

        }


        //var totalAmount: Int = 0
//        slowWalk.setOnClickListener {
//            if(slowWalk.isChecked) {
//                if (weightValue != null) {
//                    totalAmount = (weightValue * slow).roundToInt()
//                }
//                displayCalorie(totalAmount)
//            }
//            else{
//                displayCalorie(totalAmount)
//            }
//        }

    }

    private fun saveDetails() {
        val calorieBurned = result.text.toString()

        if (currentUser != null) {

            database.collection(currentUser.username).document("bmr")
                .update(mapOf(
                    "calorieBurned" to calorieBurned,
                    "calorieBurned2" to true,
                ))
                .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully written!") }
                .addOnFailureListener { e -> Log.w(TAG, "Error writing document", e) }
        }
        Thread.sleep(2000)
        val intent = Intent(this, DietaryActivity::class.java)
        startActivity(intent)
    }


    @SuppressLint("SetTextI18n")
    private fun displayCalorieBurned(calorie: Int) {
        result = findViewById(R.id.physical_activity_result_tv)
        val bmrF = BigDecimal(calorie).setScale(1, RoundingMode.HALF_EVEN)
        val bmrMulti = BigDecimal(calorie * 1000).setScale(0, RoundingMode.HALF_UP)
        //val multi = "Your Daily Caloric Intake: $bmrMulti"
        //val multi = "Your Daily Caloric Intake: $bmrMulti"
        var bmiLabel = ""
        bmiLabel = """
            $bmrF
            
            $bmiLabel
            """.trimIndent()
        result.text = bmiLabel
        //feedback!!.text = multi
    }
}
