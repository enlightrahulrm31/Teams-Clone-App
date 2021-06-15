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
            //   Toast.makeText(this, "WORKING", Toast.LENGTH_SHORT).show()
            SignInUser()
        }
    }
    private fun SignInUser(){
        val eid = findViewById<EditText>(R.id.LoginEmailAddress)   //  id for email address
        val pss = findViewById<EditText>(R.id.LoginPassword)         //  id for password view
        val email:String = eid.text.toString()
        val password:String = pss.text.toString()

        if(email.isBlank() || password.isBlank() ){                                    // Handling the case when email , password is left blank
            Toast.makeText(this, "Email and password cant be blank.", Toast.LENGTH_SHORT).show()
            return
        }
        firebaseauth.signInWithEmailAndPassword(email,password)
            .addOnCompleteListener(this){
                if(it.isSuccessful){
                    // code
                 //   val userid =firebaseauth.currentUser
                  database.collection("users").get()    // it is used to retrive all data of user from firestore database
                            .addOnSuccessListener { result->
                                var username :String ="NOT VALID"
                                for (document in result){
                                  //  document.data["Name"] // this is token  like map
                                    if(document.data["Email"]==email){
                                         username=document.data["Name"].toString()
                                        break
                                    }
                                    //
                                }
                                Toast.makeText(this,"Logged in successfully ", Toast.LENGTH_SHORT).show()
                                val intent: Intent = Intent(this,DashboardActivity::class.java)
                                intent.putExtra("UserName",username) // passing data to Dashboard activity
                                intent.putExtra("UserEmailid",email)  // passing data to Dashboard activity
                                startActivity(intent)
                            }

                    //Toast.makeText(this, "Logged In Successfully!.", Toast.LENGTH_SHORT).show()
                }
                else{
                    Toast.makeText(this, "Error.", Toast.LENGTH_SHORT).show()
                }
            }
    }
}