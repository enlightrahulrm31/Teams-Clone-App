package com.example.bcd

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*
import kotlin.collections.HashMap

class SignUpActivity : AppCompatActivity() {
    lateinit var firebaseauth: FirebaseAuth
    lateinit var database:FirebaseFirestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        firebaseauth = FirebaseAuth.getInstance()
        database = FirebaseFirestore.getInstance()
        val sgnUpButton =findViewById<Button>(R.id.btnsignup)
        sgnUpButton.setOnClickListener{
         //   Toast.makeText(this, "WORKING", Toast.LENGTH_SHORT).show()
            signupuser()
        }
    }
    private fun signupuser(){
        val nameid = findViewById<EditText>(R.id.singupname)       //  id for name of a person
        val eid = findViewById<EditText>(R.id.singupEmailAddress)   //  id for email address
        val pss = findViewById<EditText>(R.id.signupPassword)         //  id for password view
        val cpss = findViewById<EditText>(R.id.signupconfirmPassword)   // id for confirm password view
        val name:String = nameid.text.toString()
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
                    val userid: String=firebaseauth.currentUser?.uid.toString()
                    var u= User()
                    u.NAME=name
                    u.EMAIL=email
                  //   val dr:DocumentReference = database.collection("users").document(userid)
                   //  val hashmap:HashMap<String,String> = hashMapOf("Name" to name,"Email" to email) // created a hashmap in which i am storing its data of logged in users
                    /*database.collection("users").add(hashmap)
                            .addOnSuccessListener {
                                Toast.makeText(this, "Account Created Successfully!.", Toast.LENGTH_SHORT).show()
                                val intent:Intent = Intent(this,DashboardActivity::class.java)
                                intent.putExtra("UserName",hashmap["Name"]) // passing data to Dashboard activity
                                intent.putExtra("UserEmailid",hashmap["Email"])  // passing data to Dashboard activity
                                startActivity(intent)

                            }*/
                //    Toast.makeText(this, "Account Created Successfully!.", Toast.LENGTH_SHORT).show()

           //  below is trying code  in case the sytem fails delete this and run above code
                    database.collection("users").add(u)
                        .addOnSuccessListener {
                            Toast.makeText(this, "Account Created Successfully!.", Toast.LENGTH_SHORT).show()
                            val intent = Intent(this, SignInActivity::class.java)
                            startActivity(intent)
                            finish()
                        }

                }
                else{
                    Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show()  // showing toast message if anything goes wrong
                }
            }
    }
}