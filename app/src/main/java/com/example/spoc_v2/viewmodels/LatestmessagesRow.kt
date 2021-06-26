package com.example.spoc_v2.viewmodels


import com.example.spoc_v2.R
import com.example.spoc_v2.models.ChatMessages
import com.example.spoc_v2.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.latest_message_row.view.*

class LatestMessageRow(val chatMessages: ChatMessages): Item<ViewHolder>(){
    var chatPartnerUser: User? = null
    override fun getLayout(): Int {
        return R.layout.latest_message_row
    }

    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.message_textview_latestmessage.text = chatMessages.text
        //add name and picture to chat latest screen
        val chatPartnerId: String
        if(chatMessages.fromId == FirebaseAuth.getInstance().uid){
            chatPartnerId = chatMessages.toId
        } else {
            chatPartnerId = chatMessages.fromId
        }
        val ref = FirebaseDatabase.getInstance().getReference("/users/$chatPartnerId")
        ref.addListenerForSingleValueEvent(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                chatPartnerUser = snapshot.getValue(User::class.java)
                viewHolder.itemView.username_textview_latestmessage.text = chatPartnerUser?.username

                val targetImageProfile = viewHolder.itemView.imageView_latestmessage
                Picasso.get().load(chatPartnerUser?.profileImageUrl).into(targetImageProfile)
            }
            override fun onCancelled(error: DatabaseError) {
            }
        })
    }
}