package com.example.spoc_v2.ui.chat

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.DividerItemDecoration
import com.example.spoc_v2.R
import com.example.spoc_v2.models.User
import com.example.spoc_v2.ui.chat.ChatLogActivity
import com.example.spoc_v2.ui.chat.LatestMessagesActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_new_message.*
import kotlinx.android.synthetic.main.user_row_newmessage.view.*

class NewMessageActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_message)


        supportActionBar?.title = "Select User"

        recycleview_newmessage.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))

        fetchUser()
    }
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    companion object{
        val USER_KEY = "USER_KEY"
    }

    private fun fetchUser() {
        val ref = FirebaseDatabase.getInstance().getReference("/users")
        ref.addListenerForSingleValueEvent(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {

                val adapter = GroupAdapter<ViewHolder>()
                snapshot.children.forEach {
                    Log.d("New Message", it.toString())
                    val user = it.getValue(User::class.java)
                    if(user?.username != LatestMessagesActivity.currentUser?.username ) {
                        adapter.add(
                            UserItem(
                                user!!
                            )
                        )
                    }
//                    if (user != null){
//                        adapter.add(UserItem(user))
//                    }
                }
                adapter.setOnItemClickListener { item, view ->

                    val userItem = item as UserItem
                    val intent = Intent(view.context, ChatLogActivity::class.java)
                    //intent.putExtra(USER_KEY,userItem.user.username)
                    intent.putExtra(USER_KEY,userItem.user)

                    startActivity(intent)

                    finish()
                }
                recycleview_newmessage.adapter = adapter
            }
            override fun onCancelled(error: DatabaseError) {

            }

        })
    }
}

class UserItem(val user: User): Item<ViewHolder>(){
    override fun bind(viewHolder: ViewHolder, position: Int) {

        //will be called in our list for each user object
        viewHolder.itemView.username_textMessage_newmessage.text = user.username

        Picasso.get().load(user.profileImageUrl).into(viewHolder.itemView.imageView_newmesage_row)
    }
    override fun getLayout(): Int {
        return R.layout.user_row_newmessage
    }
}
