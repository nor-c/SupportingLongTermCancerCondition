package com.example.spoc_v2.ui.calorie

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.spoc_v2.R
import com.example.spoc_v2.models.DietaryUserData
import com.example.spoc_v2.ui.*
import com.example.spoc_v2.ui.authorisation.RegisterActivity
import com.example.spoc_v2.ui.chat.LatestMessagesActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_dietary.*
import java.lang.StrictMath.abs
import java.text.SimpleDateFormat
import java.util.*


class DietaryActivity : AppCompatActivity() {


    lateinit var generalHealthTextView: TextView
    lateinit var physicalTextView: TextView
    lateinit var emotionTextView: TextView
    private lateinit var dateHeaderTextView: TextView
    lateinit var calorieEatenTextView: TextView

    val TAG = "DietaryActivity"
    private var database = FirebaseFirestore.getInstance()
    private val currentUser = HomeActivity.user
    companion object{
        var userDietId: DietaryUserData? = null
    }


//    // access the spinner
//    private lateinit var generalHealthSpinner: Spinner
//    lateinit var physicalActivitySpinner: Spinner
//    lateinit var emotionSpinner: Spinner


    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("SetTextI18n", "SimpleDateFormat")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dietary)

        supportActionBar?.hide()

        dietary_bottomNavigationView.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.menu_food_search -> {
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                }
                R.id.menu_home -> {
                    val intent = Intent(this, HomeActivity::class.java)
                    startActivity(intent)
                }
                R.id.menu_graph ->{
                    val intent = Intent(this, HistoryActivity::class.java)
                    startActivity(intent)
                }
                R.id.menu_new_message -> {
                    val intent = Intent(this, LatestMessagesActivity::class.java)
                    startActivity(intent)
                }
                R.id.menu_sign_out -> {
                    FirebaseAuth.getInstance().signOut()
                    val intent = Intent(this, RegisterActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                }
            }
            true
        }

//        val mPickTimeBtn = findViewById<ImageView>(R.id.pickDateBtn)
//        dateHeaderTextView = findViewById(R.id.dateTv)
//
//
//        val c = Calendar.getInstance()
//        val year = c.get(Calendar.YEAR)
//        val month = c.get(Calendar.MONTH) +1
//        val day = c.get(Calendar.DAY_OF_MONTH)
//
//
//        mPickTimeBtn.setOnClickListener {
//
//            val dpd = DatePickerDialog(
//                this,
//                { view, year, monthOfYear, dayOfMonth ->
//                    // Display Selected date in TextView
//                    dateHeaderTextView.text = "$dayOfMonth/$month/$year"
//                },
//                year,
//                month,
//                day
//            )
//            dpd.show()
//            mPickTimeBtn.visibility = View.GONE
//            pick_date_tv.visibility = View.GONE
//
//        }
        val calorieSuggested = findViewById<TextView>(R.id.overall_general_health_spinner)
        val afterSurgery = findViewById<TextView>(R.id.after_surgery_time_edit)
        val dietaryResultCalculated = findViewById<TextView>(R.id.dietary_result_calculated)

        //val date = Calendar.getInstance().time
        val dates = SimpleDateFormat("dd/MM/yyyy")

        val currentDate = dates.format(Date()).toString()
        //afterSurgery.text = currentDateTime.toString()
        if (currentUser != null) {
            val ref =   database.collection(currentUser.username).document("bmr")

            ref.get().addOnSuccessListener { document ->
                val value = document["surgeryDate"].toString()
                val bmrCalorie = document["BMResult"].toString()
                val calorieResult = document["calorieEaten"].toString()
                val calorieResult2 = document["calorieEaten2"]
                val calorieBurned = document["calorieBurned"].toString()
                val calorieBurned2 = document["calorieBurned2"]
                val finalDate = value
                var date1: Date? = null
                var date2: Date? = null
                val bmr = bmrCalorie.toDouble() * 1000


                date1 = dates.parse(currentDate)
                date2 = dates.parse(finalDate)
                val difference: Long = abs(date1.time - date2.time)
                val differenceDates = difference / (24 * 60 * 60 * 1000)
                val dayDifference = differenceDates.toString()

                afterSurgery.text = dayDifference
                calorieSuggested.text = bmr.toString()
                if(calorieResult2 == true){
                    result_calorie_eaten_textView.text = calorieResult
                }
                if(calorieBurned2 == true){
                    physical_activity_result_textView.text = calorieBurned
                }

                //val bmrT = bmrCalorie.toDouble() * 1000
                val cr = calorieResult.toDouble()
                val cb = calorieBurned.toDouble()
                val totalCalorie = cr - cb
                if(calorieResult2 == true && calorieBurned2 == true){
                    if(bmr < totalCalorie){
                        sad_img.visibility = View.VISIBLE
                        dietaryResultCalculated.text = "Your result of caloric intake after the " +
                                "calculation is unfortunately too high, and you should limit it."
                    }else{
                        happy_img.visibility = View.VISIBLE
                        dietaryResultCalculated.text = "Your calorie consumption result after the" +
                                " calculation is excellent. Keep it up"
                    }
                }
                database.collection(currentUser.username).document("bmr")
                    .update(mapOf(
                        "fullData" to totalCalorie,
                        "fullDataTrue" to true,
                    ))
                    .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully written!") }
                    .addOnFailureListener { e -> Log.w(TAG, "Error writing document", e) }



            }
        }

        calorie_eat_tv.setOnClickListener{
            val intent = Intent(this, CalorieEatenActivity::class.java)
            startActivity(intent)
        }
        physical_activity_tv.setOnClickListener{
            val intent = Intent(this, CalorieBurnedActivity::class.java)
            startActivity(intent)
        }


//        dietary_save_button.setOnClickListener {
//            saveDetails()
//        }
//        val docRef = database.collection(currentUser?.uid.toString()).document("dietary")
//        docRef.addSnapshotListener { snapshot, e ->
//            if (e != null) {
//                Log.w(TAG, "Listen failed.", e)
//                return@addSnapshotListener
//            }
//
//            if (snapshot != null && snapshot.exists()) {
//                Log.d(TAG, "Current data: ${snapshot.data}")
//                //                    Log.d("BMRActivity", "DocumentSnapshot data: ${document.data}")
//                dietary_save_button.setOnClickListener {
//                    saveDetails()
//                }
//            } else {
//                Log.d(TAG, "Current data: null")
//
//                if (currentUser != null) {
//
//                    val data = HashMap<String, Any>()
//
//                    //var date = dateHeaderTextView.toString()
//                    val userData2 = DietaryUserData("1",
//                        "date"," d","44 ",
//                        " 44","3 ", " 4")
//                    //val id = database.collection(currentUser.uid).document()
//                    val userData = database.collection(currentUser.uid).document()
//                    userData.set(data)
//                    //database.collection(currentUser.uid).document(id).set(userData)
//                    Log.d(TAG, userData.toString())
//
////                    database.collection(currentUser.uid).document("dietary")
////                        .set(userData)
////                        .addOnSuccessListener {
////                            Log.d(TAG, "DocumentSnapshot successfully written!")
////                        }
////                        .addOnFailureListener { e ->
////                            Log.w(TAG, "Error writing document", e)
////                        }
//                }
//            }
//        }
    }

//    private fun saveDetails() {
//        //val userData: DietaryUserData? = null
//
//        if (currentUser != null) {
//
//            database.collection(currentUser.username).document("bmr")
//                .update(mapOf(
//                    "fullData" to calorieBurned,
//                    "fullDataTrue" to true,
//                ))
//                .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully written!") }
//                .addOnFailureListener { e -> Log.w(TAG, "Error writing document", e) }
//        }

//        if (currentUser != null) {
//
//           // val date = dateHeaderTextView.text.toString()
//           // val fullData = "$date, ${generalHealthTextView.text}, ${calorieEatenTextView.text}" +
//                    ", ${physicalTextView.text}, ${emotionTextView.text}"
//
//            val name = currentUser.username
//            Log.d(TAG, "Current user $name")
//           // val ref = database.collection(currentUser.uid).document()
//            val ref = database.collection(currentUser.username).document()
//
////            val user = hashMapOf(
////                "dietary" to true,
////                "id" to ref.id,   // note the use of ref.id here to get the random id
//////                "generalHealthSpinner" to generalHealthTextView.text.toString(),
////                "dietaryDate" to date,
////                "fullData" to fullData,
//////                "caloriesEaten" to calorieEatenTextView.text.toString(),
//////                "physicalActivity" to physicalTextView.text.toString(),
//////                "emotions" to emotionTextView.text.toString(),
////            )
//
//
////            ref.set(user)
//            Log.d(TAG, "DocumentSnapshot successfully written!")
//            Log.d(TAG, "DocumentSnapshot: ${ref.id}")
//
//
//
//            val intent = Intent(this, HistoryActivity::class.java)
//            Thread.sleep(1500)
//            startActivity(intent)

//            val data = HashMap<String, Any>()
//
//            //var date = dateHeaderTextView.toString()
//            val userData2 = DietaryUserData("1",
//                "date"," d","44 ",
//                " 44","3 ", " 4")
//            //val id = database.collection(currentUser.uid).document()
//            val userData = database.collection(currentUser.uid).document()
//            //userData.set(data)
//            userData.set(data)
//
//            //database.collection(currentUser.uid).document(id).set(userData)
//            Log.d(TAG, userData.toString())
//        val docRef = database.collection(currentUser?.uid.toString()).document()
//        docRef.addSnapshotListener { snapshot, e ->
//            if (e != null) {
//                Log.w(TAG, "Listen failed.", e)
//                return@addSnapshotListener
//            }
//
//            if (snapshot != null && snapshot.exists()) {
//                Log.d(TAG, "Current data: ${snapshot.data}")
//                //                    Log.d("BMRActivity", "DocumentSnapshot data: ${document.data}")
//                dietary_save_button.setOnClickListener {
//                    saveDetails()
//                }
//            } else {
//                Log.d(TAG, "Current data: null")

//                if (currentUser != null) {
//
//                    val data = HashMap<String, Any>()
//
//                    //var date = dateHeaderTextView.toString()
//                    val userData2 = DietaryUserData("1",
//                        "date"," d","44 ",
//                        " 44","3 ", " 4")
//                    //val id = database.collection(currentUser.uid).document()
//                    val userData = database.collection(currentUser.uid).document()
//                    userData.set(data)
//                    //database.collection(currentUser.uid).document(id).set(userData)
//                    Log.d(TAG, userData.toString())

//                    database.collection(currentUser.uid).document("dietary")
//                        .set(userData)
//                        .addOnSuccessListener {
//                            Log.d(TAG, "DocumentSnapshot successfully written!")
//                        }
//                        .addOnFailureListener { e ->
//                            Log.w(TAG, "Error writing document", e)
//                        }
//                }
          }
//        }
//       // var userBmrCalorieCalculated = bmr_result.text.toString()
//
//        // var updates = hashMapOf<String, Any>()
//        if (currentUser != null) {
//
//            database.collection(currentUser.uid).document()
//                .update(mapOf(
//                    "caloriesEaten" to "1222",
//                    "dietaryDate" to "111"
//                ))
//                .addOnSuccessListener { Log.d("success", "DocumentSnapshot successfully written!") }
//                .addOnFailureListener { e -> Log.w("fail", "Error writing document", e) }
//        }


//    }
//}