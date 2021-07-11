package com.example.bcd.Activity



import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import com.example.bcd.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : AppCompatActivity() {

    lateinit var firebaseauth: FirebaseAuth
    lateinit var database: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

         firebaseauth = FirebaseAuth.getInstance()                                                             // creating instance of firebase auth
        database = FirebaseFirestore.getInstance()

        if(firebaseauth.currentUser!=null){                                                                   // checking if the user is already signed in or not  if yes then
            Toast.makeText(this, "ALREADY LOGGED IN ", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, BoardingActivity::class.java)                             // directing the user to boarding activity
            startActivity(intent)
            finish()                                                                                          //  finish is used so clear the back stack
        }

        val sign_in_button = findViewById<Button>(R.id.signup)
        val sign_up_button = findViewById<Button>(R.id.signin)

        sign_in_button.setOnClickListener{
            Toast.makeText(this, "SIGN UP.", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, SignUpActivity::class.java)                                 // directing to sign in activity
            startActivity(intent)
            finish()
        }

        sign_up_button.setOnClickListener{
            Toast.makeText(this, "SIGN IN.", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, SignInActivity::class.java)                                 // directing to sign up  activity
            startActivity(intent)
            finish()
        }

    }
}