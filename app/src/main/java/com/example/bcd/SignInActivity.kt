package com.example.bcd

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore

class SignInActivity : AppCompatActivity() {

    lateinit var firebaseauth: FirebaseAuth
    lateinit var database: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_sign_in)

        firebaseauth = FirebaseAuth.getInstance()
        database = FirebaseFirestore.getInstance()

        val sgnUpButton =findViewById<Button>(R.id.LoginButton)
        sgnUpButton.setOnClickListener{
            SignInUser()
        }
    }

    private fun SignInUser(){

        val eid = findViewById<EditText>(R.id.LoginEmailAddress)                                                        //  id for email address
        val pss = findViewById<EditText>(R.id.LoginPassword)                                                          //  id for password view

        var email:String = eid.text.toString()
        val password:String = pss.text.toString()

        if(email.isBlank() || password.isBlank() ){                                                                         // Handling the case when email , password is left blank
            Toast.makeText(this, "Email and password cant be blank.", Toast.LENGTH_SHORT).show()
            return
        }

        firebaseauth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener(this){
                    if(it.isSuccessful){

                        database.collection("users").get()                                                        // it is used to retrive all data of user from firestore database
                                .addOnSuccessListener { result->
                                    var username :String ="Uploading"
                                    var cnt:Int =0
                                    for (document in result){
                                        if(document.data["email"].toString()==email){
                                            username = document.data["name"].toString()
                                            break
                                        }
                                    }

                                    val intent: Intent = Intent(this,BoardingActivity::class.java)

                                    intent.putExtra("SendingUserName","MAHANT")                                  // passing data to Boarding activity
                                    intent.putExtra("UserEmailid",email)                                               // passing data to Boarding activity

                                    startActivity(intent)
                                    finish()

                                }
                    }
                    else{
                        Toast.makeText(this, "Error.", Toast.LENGTH_SHORT).show()
                    }
                }
    }
}
