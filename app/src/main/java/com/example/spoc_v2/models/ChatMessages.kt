package com.example.spoc_v2.models



class ChatMessages(val id: String, val text: String, val fromId: String, val toId: String, val timestamp: Long){
        constructor(): this("","","","", -1)
}

