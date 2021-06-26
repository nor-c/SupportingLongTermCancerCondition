package com.example.spoc_v2.unittest

import com.google.common.truth.Truth.assertThat
import org.junit.Test

class RegistrationUtilTest {

    @Test
    fun `empty username returns false`() {
        val result = RegistrationUtil.validateRegistrationInput(
            "",
            "",
            "123@rt.net",
            "123"
        )
        assertThat(result).isFalse()
    }

    @Test
    fun `valid username and email and password returns true`() {
        val result = RegistrationUtil.validateRegistrationInput(
            "",
            "Norb",
            "norbrt@gm.com",
            "1234678"
        )
        assertThat(result).isTrue()
    }

    @Test
    fun `username already exists returns false`() {
        val result = RegistrationUtil.validateRegistrationInput(
            "",
            "Carl",
            "123@th.net",
            "123"
        )
        assertThat(result).isFalse()
    }

    @Test
    fun `incorrct password returns false`() {
        val result = RegistrationUtil.validateRegistrationInput(
            "",
            "Philipp",
            "123456@n.com",
            "abcdefg"
        )
        assertThat(result).isFalse()
    }

    @Test
    fun `empty password returns false`() {
        val result = RegistrationUtil.validateRegistrationInput(
            "",
            "Philipp",
            "",
            ""
        )
        assertThat(result).isFalse()
    }

    @Test
    fun `less than 2 digit password returns false`() {
        val result = RegistrationUtil.validateRegistrationInput(
            "",
            "Philipp",
            "abcdefg5",
            "abcdefg5"
        )
        assertThat(result).isFalse()
    }
}