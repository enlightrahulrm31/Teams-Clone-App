package com.example.bcd

import android.app.PictureInPictureParams
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.bumptech.glide.Glide
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_profile.*
import kotlinx.android.synthetic.main.nav_header.*

class ProfileActivity : AppCompatActivity() {

    lateinit var database: FirebaseFirestore
     var curUserUrl:String ?=null
    lateinit var phno :String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        database = FirebaseFirestore.getInstance()
        getSupportActionBar()?.setDisplayHomeAsUpEnabled(true);
        val name:String = intent.getStringExtra("name")!!
        val email:String = intent.getStringExtra("email")!!
        getSupportActionBar()?.setTitle(name)
        username.text = name
        usermail.text = email

        database.collection("users").get()    // it is used to retrive all data of user from firestore database
                .addOnSuccessListener { result ->
                    for (document in result) {
                        if (document.data["email"].toString() == email) {   // if the email from document is found equal to email of signed in user then we will replace username to loged in user name
                            curUserUrl = document.data["userurl"].toString()
                            phno = document.data["phoneno"].toString()
                            break
                        }
                    }
                }

        /*if(phno !=null){
            Phonetext.visibility.
        }*/
        UpdateButton.setOnClickListener {
            database.collection("users").get()    // it is used to retrive all data of user from firestore database
                    .addOnSuccessListener {result ->
                                for (document in result) {
                                    if (document.data["email"].toString() == email) {
                                        document.data["phoneno"] = Phoneno.text
                                        break;
                                    }
                                }
                    }
        }
          Glide      // user to download url of the image
                .with(this)
                .load(curUserUrl)
                .centerCrop()
                .placeholder(R.drawable.ic_baseline_person_24)
                .into(imagebutton)
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val intent = Intent(this,BoardingActivity::class.java)
        startActivity(intent)
        finish()
        return super.onOptionsItemSelected(item)
    }
}