package com.example.bcd.Activity

import android.animation.ObjectAnimator
import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.bcd.R
import com.example.bcd.DataClass.User
import com.example.bcd.Validation.Validation
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_sign_up.*
import java.util.*

class SignUpActivity : AppCompatActivity() {

    lateinit var firebaseauth: FirebaseAuth
    lateinit var database:FirebaseFirestore

    var selecteduri :Uri ?=null
    var imgurl:String ?=null

    var checkIfuserSelectedImage:Boolean =false
    var currentProgress=0

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        firebaseauth = FirebaseAuth.getInstance()
        database = FirebaseFirestore.getInstance()

        val sgnUpButton =findViewById<Button>(R.id.btnsignup)
        ProgressBar.max=10
         currentProgress =10

        imagebutton.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type="image/*"
            startActivityForResult(intent,12)
        }

        sgnUpButton.setOnClickListener{
            signupuser()
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode==12 && data!=null && resultCode==Activity.RESULT_OK){                               // checking if all the required condition are satisfied when the user select image
            checkIfuserSelectedImage=true
            selecteduri =data.data
            val bitmap =MediaStore.Images.Media.getBitmap(contentResolver,selecteduri)
            imagebutton.setImageBitmap(bitmap)                                                             // this is used for round image configuaration
        }
    }

    var u= User()

    private fun signupuser(){

        val nameid = findViewById<EditText>(R.id.singupname)                                                     //  id for name of a person
        val eid = findViewById<EditText>(R.id.singupEmailAddress)                                                //  id for email address

        val pss = findViewById<EditText>(R.id.signupPassword)                                                    //  id for password view
        val cpss = findViewById<EditText>(R.id.signupconfirmPassword)                                            // id for confirm password view

        val name:String = nameid.text.toString()
        val email:String = eid.text.toString()
        val password:String = pss.text.toString()

        val confirmpassword:String = cpss.text.toString()
        val validation = Validation()

        if(validation.checkemail(email) == false){
            Toast.makeText(this, "Enter Valid Mail", Toast.LENGTH_SHORT).show()
            return
        }

        if(email.isBlank() || password.isBlank() || confirmpassword.isBlank()){                                  // Handling the case when email , password is left blank
            Toast.makeText(this, "Email and password cant be blank.", Toast.LENGTH_SHORT).show()
            return
        }

        if(password!=confirmpassword){                                                                           //  checking the case when password is not equal to confirmed password
            Toast.makeText(this, "Password and Confirm Doesnt match .", Toast.LENGTH_SHORT).show()
            return
        }

        firebaseauth.createUserWithEmailAndPassword(email,password)
            .addOnCompleteListener(this){
                if(it.isSuccessful){
                    ObjectAnimator.ofInt(ProgressBar,"progress",currentProgress)
                        .setDuration(4000)
                        .start()

                    val userid: String=firebaseauth.currentUser?.uid.toString()

                    u.NAME=name
                    u.EMAIL=email
                    u.UID=userid

                    if(checkIfuserSelectedImage==true) {
                        uploadimagetofirebase(email)
                    }
                    else {
                        database.collection("users").document(email).set(u)
                            Toast.makeText(this, "Account Created Successfully!.", Toast.LENGTH_SHORT).show()
                            val intent = Intent(this, BoardingActivity::class.java)
                            intent.putExtra("url",it.toString())
                            startActivity(intent)
                            finish()

                    }
                }
                else{
                    Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show()  // showing toast message if anything goes wrong
                }
            }
    }

    private fun uploadimagetofirebase(email:String) {

        val filename =UUID.randomUUID().toString()

        val ref = FirebaseStorage.getInstance().getReference("$filename")
         ref.putFile(selecteduri!!).addOnCompleteListener {
             ref.downloadUrl.addOnSuccessListener{
                 imgurl=it.toString()                                                                         // taking the url of image that we have uploaded in firebase storage
                 u.USERURL=imgurl                                                                             // adding image url to our class user
                 database.collection("users").document(email).set(u)

                 Toast.makeText(this, "Account Created Successfully!.", Toast.LENGTH_SHORT).show()

                 val intent = Intent(this, BoardingActivity::class.java)
                 intent.putExtra("url",it.toString())

                 startActivity(intent)
                 finish()
             }
         }

    }

}