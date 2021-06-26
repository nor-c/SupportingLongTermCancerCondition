package com.example.spoc_v2.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.spoc_v2.R
import com.example.spoc_v2.models.User
import com.example.spoc_v2.ui.calorie.DietaryActivity
import com.example.spoc_v2.ui.chat.LatestMessagesActivity


import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_home.*


class HomeActivity : AppCompatActivity() {

    companion object{
        var user: User? = null
    }
    val TAG = "HomeActivity"
    private var database = FirebaseFirestore.getInstance()
    private val currentUser = user
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        supportActionBar?.hide()

        if (currentUser != null) {

            database.collection(currentUser.username).document("bmr")
                .update(mapOf(
                    "calorieEaten2" to 1,
                    "calorieBurned2" to 1,

                    ))
                .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully written!") }
                .addOnFailureListener { e -> Log.w(TAG, "Error writing document", e) }
        }

        home_bmr_img.setOnClickListener{
            val intent = Intent(this, BMRActivity::class.java)
            startActivity(intent)
        }
        home_recipes_img.setOnClickListener{
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
        home_chat_img.setOnClickListener{
            val intent = Intent(this, LatestMessagesActivity::class.java)
            startActivity(intent)
        }

        home_dietary_img.setOnClickListener{
            val intent = Intent(this, DietaryActivity::class.java)
            startActivity(intent)
        }
        home_history_img.setOnClickListener{
            val intent = Intent(this, HistoryActivity::class.java)
            startActivity(intent)
        }
        fetchCurrentUser()
    }

    private fun fetchCurrentUser() {
        val uid = FirebaseAuth.getInstance().uid
        val ref = FirebaseDatabase.getInstance().getReference("/users/$uid")
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
            }
            override fun onDataChange(snapshot: DataSnapshot) {
                user = snapshot.getValue(User::class.java)
                Log.d("HomeUser", "Current user ${user?.username}")
                home_user_name.text = "Welcome ${user?.username}"
            }

        })
//        if (currentUser != null) {
//
//            database.collection(currentUser.username).document("bmr")
//                .update(mapOf(
//                    "calorieEaten2" to 1,
//
//                    ))
//                .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully written!") }
//                .addOnFailureListener { e -> Log.w(TAG, "Error writing document", e) }
//        }
    }
    fun openOtherActivity() {
        val intent = Intent(this, BMRActivity::class.java)
        startActivity(intent)
    }



}