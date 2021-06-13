package com.example.bcd

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth

class SignUpActivity : AppCompatActivity() {
    lateinit var firebaseauth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        firebaseauth = FirebaseAuth.getInstance()
        val sgnUpButton =findViewById<Button>(R.id.btnsignup)
        sgnUpButton.setOnClickListener{
         //   Toast.makeText(this, "WORKING", Toast.LENGTH_SHORT).show()
            signupuser()
        }
    }

    private fun signupuser(){
    val eid = findViewById<EditText>(R.id.singupEmailAddress)   //  id for email address
    val pss = findViewById<EditText>(R.id.signupPassword)         //  id for password view
    val cpss = findViewById<EditText>(R.id.signupconfirmPassword)   // id for confirm password view
        val email:String = eid.text.toString()
        val password:String = pss.text.toString()
        val confirmpassword:String = cpss.text.toString()

        if(email.isBlank() || password.isBlank() || confirmpassword.isBlank()){                                // Handling the case when email , password is left blank
            Toast.makeText(this, "Email and password cant be blank.", Toast.LENGTH_SHORT).show()
            return
        }

        if(password!=confirmpassword){        //  checking the case when password is not equal to confirmed password
            Toast.makeText(this, "Password and Confirm Doesnt match .", Toast.LENGTH_SHORT).show()
            return
        }
        firebaseauth.createUserWithEmailAndPassword(email,password)
            .addOnCompleteListener(this){
                if(it.isSuccessful){
                    // code
                    Toast.makeText(this, "Account Created Successfully!.", Toast.LENGTH_SHORT).show()
                }
                else{
                    Toast.makeText(this, email, Toast.LENGTH_SHORT).show()
                }
            }
    }
}