package com.example.spoc_v2.ui.authorisation

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import com.example.spoc_v2.R
import com.example.spoc_v2.models.User
import com.example.spoc_v2.ui.BMRActivity
import com.example.spoc_v2.ui.HomeActivity
import com.example.spoc_v2.ui.MainActivity
import com.example.spoc_v2.ui.chat.LatestMessagesActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_register.*
import java.util.*
import kotlin.collections.HashMap

class RegisterActivity : AppCompatActivity() {

    companion object {
        const val TAG = "RegisterActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        //supportActionBar?.title = "       Register"
        supportActionBar?.hide()

        val userCurr = FirebaseAuth.getInstance().currentUser
        if (userCurr != null) {
            finish()
            val newAct = Intent(this, HomeActivity::class.java)
            startActivity(newAct)
        }


        registerButon_reg.setOnClickListener {
            performRegister()
        }
        alreadyHaveAcc_TextView_reg.setOnClickListener {
            Log.d(TAG, "Have acc")

            //launch login activity
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
        //Select photo
        slectphoto_button_reg.setOnClickListener {
            Log.d(TAG, "Try to show photo selector")

            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, 0)
        }
    }

    //Choose photo pic
    var selectedPhotoUri: Uri? = null

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 0 && resultCode == Activity.RESULT_OK && data != null) {
            Log.d(TAG, "Photo was selected")

            //Pick up pic from phone folder
            selectedPhotoUri = data.data
            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, selectedPhotoUri)

            selectPhotoCircle_imageview_reg.setImageBitmap(bitmap)
            slectphoto_button_reg.alpha = 0f

        }
    }

    //Registration screen
    private fun performRegister() {

        val email = email_editText_reg.text.toString()
        val password = password_editText_reg.text.toString()
        val user = username_editText_reg.toString()
        val img = selectedPhotoUri

        if (email.isEmpty() || password.isEmpty() || img == null) {
            Toast.makeText(
                this,
                "Please select profile picture and enter user name,email and password",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        Log.d(TAG, "Email is $email")
        Log.d(TAG, "Password is $password")

        //Firebase Authentication to create a user with email and password
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (!it.isSuccessful) return@addOnCompleteListener
                //else is successful
                Log.d(
                    "RegisterActivity",
                    "Successfully created user with uid: ${it.result?.user?.uid}"
                )
                uploadImageToFirebase()
            }
            .addOnFailureListener {
                Log.d(TAG, "Failed to create user: ${it.message}")
                Toast.makeText(this, "Filed to create new user: ${it.message}", Toast.LENGTH_SHORT)
                    .show()
            }
    }
    private fun uploadImageToFirebase() {
        if (selectedPhotoUri == null) return

        val filename = UUID.randomUUID().toString()
        val ref = FirebaseStorage.getInstance().getReference("/images/$filename")
        ref.putFile(selectedPhotoUri!!)
            .addOnSuccessListener { it ->
                Log.d(TAG, "Successfully uploaded image: ${it.metadata?.path}")

                ref.downloadUrl.addOnSuccessListener {
                    // it.toString()
                    Log.d(TAG, "File Location: $it")
                    saveUserToFirebaseDatabase(it.toString())
                }
            }
            .addOnFailureListener {
                Log.d(TAG, "Filed to upload image ${it.message}")
            }
    }
    private fun saveUserToFirebaseDatabase(profileImageUrl: String) {
        val uid = FirebaseAuth.getInstance().uid ?: ""
        val ref = FirebaseDatabase.getInstance().getReference("/users/$uid")
        //var db = FirebaseFirestore.getInstance()
        val user = User(
            uid,
            username_editText_reg.text.toString(),
            profileImageUrl
        )

        ref.setValue(user)
            .addOnSuccessListener {
                val newImg = HashMap<String, Any>()
                newImg.put("profileImageUrl", profileImageUrl)
                ref.updateChildren(newImg)
               // db.collection("users").document(uid)
                Log.d(TAG, "User saved to Firebase Database")

                //Run LatestMessagesActivity
                val intent = Intent(this, HomeActivity::class.java)
                //Allow back to main window
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            }
            .addOnFailureListener {
                Log.d(TAG, "Failed to add user to DB: ${it.message}")
            }
    }
}