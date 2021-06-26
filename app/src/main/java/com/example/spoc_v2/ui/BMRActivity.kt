package com.example.spoc_v2.ui

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import com.example.spoc_v2.R
import com.example.spoc_v2.ui.HomeActivity.Companion.user
import com.example.spoc_v2.ui.authorisation.RegisterActivity
import com.example.spoc_v2.ui.chat.LatestMessagesActivity
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_b_m_r.*
import kotlinx.android.synthetic.main.activity_dietary.*
import java.lang.String.format
import java.math.BigDecimal
import java.math.RoundingMode
import java.util.*

class BMRActivity : AppCompatActivity() {
    private var height: EditText? = null
    private var weight: EditText? = null
    private var age: EditText? = null
    private var result: TextView? = null
    private var bmrFeedbackTv: TextView? = null
    private var bmr = 0.0
    private lateinit var surgeryDateTextView: TextView
    private var database = FirebaseFirestore.getInstance()
    private val currentUser = user

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_b_m_r)

        supportActionBar?.hide()

        bmrFeedbackTv = findViewById<View>(R.id.bmr_feedback_tv) as TextView

        if (currentUser != null){
            val docRef = database.collection(currentUser.username).document("bmr")
            docRef.addSnapshotListener { snapshot, e ->
                if (e != null) {
                    Log.w("BMRActivity", "Listen failed.", e)
                    return@addSnapshotListener
                }

                if (snapshot != null && snapshot.exists()) {
                    Log.d("BMRActivity", "Current data: ${snapshot.data}")
                    //                    Log.d("BMRActivity", "DocumentSnapshot data: ${document.data}")
                    bmr_save_button.setOnClickListener {
                        saveDetails()
                    }
                } else {
                    Log.d("BMRActivity", "Current data: null")

                    val docData = hashMapOf(
                        "BMR" to true,
                        "BMResult" to 3.14159265,
                        "dateBMR" to Timestamp(Date()),
                        "surgeryDate" to Timestamp(Date()),
                        "calorieEaten" to 3,
                        "calorieBurned" to 3,
                    )

                    database.collection(currentUser.username).document("bmr")
                        .set(docData)
                        .addOnSuccessListener {
                            Log.d(
                                "BMRActivity",
                                "DocumentSnapshot successfully written!"
                            )
                        }
                        .addOnFailureListener { e ->
                            Log.w(
                                "BMRActivity",
                                "Error writing document",
                                e
                            )
                        }
                }
            }
        }


//        docRef.get()
//            .addOnSuccessListener { document ->
//                if (document != null) {
//                    Log.d("BMRActivity", "DocumentSnapshot data: ${document.data}")
//                    bmr_save_button.setOnClickListener {
//                        saveDetails()
//                    }
//                } else {
//                    Log.d("BMRActivity", "No such document")
//
//                    if(currentUser != null){
//
//                        val docData = hashMapOf(
//                            "stringExample" to "Hello world!",
//                            "booleanExample" to true,
//                            "numberExample" to 3.14159265,
//                            "dateExample" to Timestamp(Date()),
//                            "listExample" to arrayListOf(1, 2, 3),
//                            "nullExample" to null
//                        )
//
//                        val nestedData = hashMapOf(
//                            "a" to 5,
//                            "b" to true
//                        )
//
//                        docData["objectExample"] = nestedData
//
//
//                        database.collection("users").document(currentUser?.uid.toString())
//                            .set(docData)
//                            .addOnSuccessListener { Log.d("BMRActivity", "DocumentSnapshot successfully written!") }
//                            .addOnFailureListener { e -> Log.w("BMRActivity", "Error writing document", e) }
//                    }
//
//                }
//            }
//            .addOnFailureListener { exception ->
//                Log.d("BMRActivity", "get failed with ", exception)
//            }

        // database.child(currentUser.toString()).setValue(User(result))


        bmr_bottomNavigationView.setOnNavigationItemSelectedListener {
            when (it.itemId){
                R.id.menu_food_search ->{
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                }
                R.id.menu_home ->{
                    val intent = Intent(this, HomeActivity::class.java)
                    startActivity(intent)
                }
                R.id.menu_new_message ->{
                    val intent = Intent(this, LatestMessagesActivity::class.java)
                    startActivity(intent)
                }
                R.id.menu_graph ->{
                    val intent = Intent(this, HistoryActivity::class.java)
                    startActivity(intent)
                }
                R.id.menu_sign_out ->{
                    FirebaseAuth.getInstance().signOut()
                    val intent = Intent(this, RegisterActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                }
            }
            true
        }

        height = findViewById<View>(R.id.bmr_height) as EditText
        weight = findViewById<View>(R.id.bmr_weight) as EditText
        age = findViewById<View>(R.id.bmr_age) as EditText
        result = findViewById<View>(R.id.bmr_result) as TextView

        val mPickTimeBtn = findViewById<ImageView>(R.id.bmr_pickDateBtn)
        surgeryDateTextView = findViewById<TextView>(R.id.bmr_dateTv)

        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        mPickTimeBtn.setOnClickListener {

            val dpd = DatePickerDialog(
                this,
                { view, year, monthOfYear, dayOfMonth ->
                    // Display Selected date in TextView
                    val mm = monthOfYear + 1
                    surgeryDateTextView.text = "$dayOfMonth/$mm/$year"
                },
                year,
                month,
                day
            ).show()
            mPickTimeBtn.visibility = View.GONE
        }

    }


    fun calculateBMR(v: View?) {
        val heightStr = height!!.text.toString()
        val weightStr = weight!!.text.toString()
        val ageStr = age!!.text.toString()

        if ("" != heightStr && "" != weightStr && "" != ageStr)
        {
            val heightValue = heightStr.toInt()
            val weightValue = weightStr.toInt()
            val ageValue = ageStr.toInt()

            male_button.setOnClickListener {
                bmr = ((9.99 * weightValue) + (6.25 * heightValue) - (4.92 * ageValue) + 5) / 1000
                displayBMR(bmr)
                if (currentUser != null) {

                    database.collection(currentUser.username).document("bmr")
                        .update(mapOf(
                            "male" to true,
                            "female" to 1,
                        ))
                }
            }
            female_button.setOnClickListener {
                bmr = ((9.99 * weightValue) + (6.25 * heightValue) - (4.92 * ageValue) - 161) / 1000
                displayBMR(bmr)
                if (currentUser != null) {

                    database.collection(currentUser.username).document("bmr")
                        .update(mapOf(
                            "female" to true,
                            "male" to 1,
                        ))
                }
            }

            no_exercise_btn.setOnClickListener {
                var noExer = 0.0
                noExer = bmr * 1.2
                displayBMR(noExer)
            }
            exercise_btn_one.setOnClickListener {
                var oneExer = 0.0
                oneExer = bmr * 1.375
                displayBMR(oneExer)
            }
            exercise_btn_two.setOnClickListener {
                var twoExrc = 0.0
                twoExrc = bmr * 1.55
                displayBMR(twoExrc)
            }
            exercise_btn_three.setOnClickListener {
                var threeExer = 0.0
                threeExer = bmr * 1.725
                displayBMR(threeExer)
            }
        }
        calc.visibility = View.GONE
    }


    @SuppressLint("SetTextI18n")
    private fun saveDetails() {
        val userBmrCalorieCalculated = bmr_result.text.toString()
        val weightStr = weight!!.text.toString()
        //val weightValue = weightStr.toInt()


        bmrFeedbackTv!!.visibility = View.VISIBLE
        bmr_save_button.visibility = View.GONE

        if (currentUser != null) {

            database.collection(currentUser.username).document("bmr")
                .update(mapOf(
                    "BMR" to true,
                    "BMResult" to userBmrCalorieCalculated,
                    "dateBMR" to FieldValue.serverTimestamp(),
                    "surgeryDate" to surgeryDateTextView.text.toString(),
                    "weight" to weightStr,
                ))
                .addOnSuccessListener { Log.d("success", "DocumentSnapshot successfully written!") }
                .addOnFailureListener { e -> Log.w("fail", "Error writing document", e) }
        }
    }


    private fun displayBMR(bmr: Double) {
        val bmrF = BigDecimal(bmr).setScale(3, RoundingMode.HALF_EVEN)
        val bmrMulti  = BigDecimal(bmr * 1000).setScale(0, RoundingMode.HALF_UP)
        val multi = "Your Daily Caloric Intake: $bmrMulti"
        var bmiLabel = ""
//        bmiLabel = if (java.lang.Float.compare(bmr.toFloat(), 15f) <= 0) {
//            getString(R.string.very_severely_underweight)
//        } else if (java.lang.Float.compare(bmr.toFloat(), 15f) > 0 && java.lang.Float.compare(
//                bmr.toFloat(),
//                16f
//            ) <= 0
//        ) {
//            getString(R.string.severely_underweight)
//        } else if (java.lang.Float.compare(bmr.toFloat(), 16f) > 0 && java.lang.Float.compare(
//                bmr.toFloat(),
//                18.5f
//            ) <= 0
//        ) {
//            getString(R.string.underweight)
//        } else if (java.lang.Float.compare(bmr.toFloat(), 18.5f) > 0 && java.lang.Float.compare(
//                bmr.toFloat(),
//                25f
//            ) <= 0
//        ) {
//            getString(R.string.normal)
//        } else if (java.lang.Float.compare(bmr.toFloat(), 25f) > 0 && java.lang.Float.compare(
//                bmr.toFloat(),
//                30f
//            ) <= 0
//        ) {
//            getString(R.string.overweight)
//        } else if (java.lang.Float.compare(bmr.toFloat(), 30f) > 0 && java.lang.Float.compare(
//                bmr.toFloat(),
//                35f
//            ) <= 0
//        ) {
//            getString(R.string.obese_class_i)
//        } else if (java.lang.Float.compare(bmr.toFloat(), 35f) > 0 && java.lang.Float.compare(
//                bmr.toFloat(),
//                40f
//            ) <= 0
//        ) {
//            getString(R.string.obese_class_ii)
//        } else {
//            getString(R.string.obese_class_iii)
//        }
        bmiLabel = """
            $bmrF
            
            $bmiLabel
            """.trimIndent()
        result!!.text = bmiLabel
        bmrFeedbackTv!!.text = multi
    }


//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        when(item.itemId){
//            R.id.menu_food_search ->{
//                val intent = Intent(this, MainActivity::class.java)
//                startActivity(intent)
//            }
//            R.id.menu_bmr_calculator ->{
//                val intent = Intent(this, BMRActivity::class.java)
//                startActivity(intent)
//            }
//            R.id.menu_new_message ->{
//                val intent = Intent(this, LatestMessagesActivity::class.java)
//                startActivity(intent)
//            }
//            R.id.menu_sign_out ->{
//                FirebaseAuth.getInstance().signOut()
//                val intent = Intent(this, RegisterActivity::class.java)
//                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
//                startActivity(intent)
//            }
//        }
//        return super.onOptionsItemSelected(item)
//    }
//    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
//        menuInflater.inflate(R.menu.top_menu, menu)
//        return super.onCreateOptionsMenu(menu)
//    }
}