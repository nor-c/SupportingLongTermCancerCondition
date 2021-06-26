package com.example.spoc_v2.ui.chat


import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.recyclerview.widget.DividerItemDecoration
import com.example.spoc_v2.models.ChatMessages
import com.example.spoc_v2.models.User
import com.example.spoc_v2.viewmodels.LatestMessageRow
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_latest_messages.*
import com.example.spoc_v2.R
import com.example.spoc_v2.ui.HistoryActivity
import com.example.spoc_v2.ui.HomeActivity
import com.example.spoc_v2.ui.MainActivity
import com.example.spoc_v2.ui.authorisation.RegisterActivity
import kotlinx.android.synthetic.main.activity_b_m_r.*


class LatestMessagesActivity : AppCompatActivity() {
    private lateinit var menuItem: MenuItem
    companion object{
        var currentUser: User? = null
        val TAG = "LatestMessages"
    }
    @SuppressLint("ResourceType")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_latest_messages)

        supportActionBar?.title = "Latest messages"

        latest_messages_bottomNavigationView.setOnNavigationItemSelectedListener {
            when (it.itemId){
                R.id.menu_food_search ->{
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                }
                R.id.menu_home ->{
                    val intent = Intent(this, HomeActivity::class.java)
                    startActivity(intent)
                }
                R.id.menu_graph ->{
                    val intent = Intent(this, HistoryActivity::class.java)
                    startActivity(intent)
                }
                R.id.menu_new_message ->{
                    val intent = Intent(this, NewMessageActivity::class.java)
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

        recyclerview_latest_messages.adapter= adapter
        recyclerview_latest_messages.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))

        //set item click listener on your adapter to show megs on chat between two users
        adapter.setOnItemClickListener { item, view ->
            Log.d(TAG, "Success")
            val intent = Intent(this, ChatLogActivity::class.java)

            val row = item as LatestMessageRow

            intent.putExtra(NewMessageActivity.USER_KEY, row.chatPartnerUser)
            startActivity(intent)  //comment if we don't want start chat from this activity
        }

        //   setupDummyRows()
        listenForLatestMessages()
        fetchCurrentUser()
        //If user is logged in already
        verifyUserIsLoggedIn()
    }
    //Display latest messages from another user
    val latestMessagesMap = HashMap<String, ChatMessages>()
    private fun refreshRecyclerVieMessages(){
        adapter.clear()
        latestMessagesMap.values.forEach{
            adapter.add(LatestMessageRow(it))
        }
    }

    private fun listenForLatestMessages() {
        val fromId = FirebaseAuth.getInstance().uid
        val ref = FirebaseDatabase.getInstance().getReference("/latest-messages/$fromId")
        ref.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val chatMessage = snapshot.getValue(ChatMessages::class.java) ?: return //elvis operator or !! below
                latestMessagesMap[snapshot.key!!] = chatMessage
                refreshRecyclerVieMessages()
            }
            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                val chatMessage = snapshot.getValue(ChatMessages::class.java) ?: return //elvis operator or !! below
                latestMessagesMap[snapshot.key!!] = chatMessage
                refreshRecyclerVieMessages()
            }
            override fun onCancelled(error: DatabaseError) {
            }
            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {

            }
            override fun onChildRemoved(snapshot: DataSnapshot) {
            }

        })
    }


    val adapter = GroupAdapter<ViewHolder>()


    private fun fetchCurrentUser() {
        val uid = FirebaseAuth.getInstance().uid
        val ref = FirebaseDatabase.getInstance().getReference("/users/$uid")
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {

            }

            override fun onDataChange(snapshot: DataSnapshot) {
                currentUser = snapshot.getValue(User::class.java)
                Log.d("LatestMessages", "Current user ${currentUser?.username}")
            }

        })
    }

    private fun verifyUserIsLoggedIn() {
        val uid = FirebaseAuth.getInstance()
        if (uid == null){
            val intent = Intent(this, RegisterActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }
    }
//    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
//        menuInflater.inflate(R.menu.new_message_nav, menu)
////        menuItem = menu!!.findItem(R.id.new_message)
////    //        checkSavedRecipes(menuItem)
////
////
////        val intent = Intent(this, MainActivity::class.java)
////        startActivity(intent)
//
//        return true
//    }

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
//                val intent = Intent(this, NewMessageActivity::class.java)
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