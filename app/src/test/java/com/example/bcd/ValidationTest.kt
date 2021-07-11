package com.example.bcd

import com.example.bcd.Validation.Validation
import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Test

class ValidationTest{
    private lateinit var validation : Validation

    @Before
    fun setUp(){
        validation = Validation()
    }

    @Test
    fun CheckEmail(){
        val result = validation.checkemail("123@gmail.com")
        assertThat(result).isTrue()   // it will check if the email provided is true or not
    }
    @Test
    fun CheckPhoneNumber(){
        val result = validation.check_phone_number("abc789")
        assertThat(result).isTrue()   // it will check if the email provided is true or not
    }
}