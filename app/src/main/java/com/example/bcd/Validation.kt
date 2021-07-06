package com.example.bcd

class Validation {
    private  val EMAIL_PATTERN = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"

    private val PHONE_NO_PATTERN = "[0-9]+"

    fun checkemail(email:String ):Boolean{

        if(email.matches((EMAIL_PATTERN.toRegex()))){
            return true
        }
        else{
            return false
        }

    }

    fun check_phone_number(phonenumber:String):Boolean{
         if(phonenumber.length==10  &&  phonenumber.matches(PHONE_NO_PATTERN.toRegex())){
             return true
         }
        else{
             return false
         }
    }





}