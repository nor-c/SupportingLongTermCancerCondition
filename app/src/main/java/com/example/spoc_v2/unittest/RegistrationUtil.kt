package com.example.spoc_v2.unittest

import com.example.spoc_v2.ui.HomeActivity
import com.example.spoc_v2.ui.chat.LatestMessagesActivity

object RegistrationUtil {

    private val existingUsers = LatestMessagesActivity.currentUser?.username

    /**
     * the input is not valid if...
     * ...the username/password is empty
     * ...the username is already taken
     * ...the email is not the same as the real password
     * ...the password contains less than 2 digits
     */
    fun validateRegistrationInput(
        img: String,
        username: String,
        email: String,
        password: String
    ): Boolean {
        if(username.isEmpty() || email.isEmpty() || password.isEmpty()) {
            return false
        }
        if(username == existingUsers) {
            return false
        }
        if(password != password) {
            return false
        }
        if(password.count { it.isDigit() } < 8) {
            return false
        }
        return true
    }
}