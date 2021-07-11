package com.example.bcd.Validation

class Validation {
    private  val EMAIL_PATTERN = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"

    private val PHONE_NO_PATTERN = "[0-9]+"

    fun checkemail(email:String ):Boolean{

        return email.matches((EMAIL_PATTERN.toRegex()))

    }

    fun check_phone_number(phonenumber:String):Boolean{

        return phonenumber.length==10  &&  phonenumber.matches(PHONE_NO_PATTERN.toRegex())
    }

}