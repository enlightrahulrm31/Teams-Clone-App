package com.example.bcd.Activity

import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.drawerlayout.widget.DrawerLayout
import com.bumptech.glide.Glide
import com.example.bcd.Adapters.namemap
import com.example.bcd.R
import com.example.bcd.RecyclerViewActivity.ReclyclerViewActivity
import com.example.bcd.RecyclerViewActivity.TeamRecyclerViewActivity
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_boarding.*
import kotlinx.android.synthetic.main.nav_header.*
class BoardingActivity : AppCompatActivity() {
    lateinit var toggle :ActionBarDrawerToggle    // this will open the hammberger toogle bar in the action bar

    lateinit var firebaseauth: FirebaseAuth

    lateinit var database: FirebaseFirestore

     var  username:String = "Loading"

    var curUserUrl:String ?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_boarding)

        var dialog :Dialog = Dialog(this)

        firebaseauth = FirebaseAuth.getInstance()

        database = FirebaseFirestore.getInstance()

        val drawerLayout : DrawerLayout = findViewById(R.id.drawerlayout)

        val navView:NavigationView =findViewById(R.id.nav_view)

        toggle = ActionBarDrawerToggle(this,drawerLayout, R.string.open, R.string.close)

        drawerLayout.addDrawerListener(toggle)

        toggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val  intentjoinmeet : Intent = Intent(this, TeamRecyclerViewActivity::class.java)

        val  intentdasboard: Intent = Intent(this, ReclyclerViewActivity::class.java)

        val  intentMainActivity: Intent = Intent(this, MainActivity::class.java)

        val  intentmanagemeet: Intent = Intent(this, CreateMeetActivity::class.java)

        val  intentmyprofile: Intent = Intent(this, ProfileActivity::class.java)

        intentmyprofile.putExtra("name",username)

        intentmyprofile.putExtra("email",firebaseauth.currentUser?.email)

        val u = firebaseauth.currentUser

        val em = u?.email
        namemap =em!!

        var phno:String = ""
        database.collection("users").get()    // it is used to retrive all data of user from firestore database
                .addOnSuccessListener { result ->
                    for (document in result) {
                        if (document.data["email"].toString() == em) {   // if the email from document is found equal to email of signed in user then we will replace username to loged in user name

                            username = document.data["name"].toString()

                            curUserUrl = document.data["userurl"].toString()

                            phno = document.data["phoneno"].toString()

                            break
                        }
                    }
                }

        meetings.setOnClickListener {
            startActivity(intentjoinmeet)

        }

        createMeeting.setOnClickListener {
            startActivity(intentmanagemeet)

        }

        Mycontact.setOnClickListener {
            startActivity(intentdasboard)
        }

        myprofile.setOnClickListener {
            dialog.setContentView(R.layout.custom_pop)

            Glide      // user to download url of the image
                    .with(this)
                    .load(curUserUrl)
                    .centerCrop()
                    .placeholder(R.drawable.ic_baseline_person_24)
                    .into(dialog.findViewById(R.id.userimage))

            dialog.findViewById<ImageView>(R.id.cancelbutton).setOnClickListener {
                dialog.dismiss()
            }

            dialog.findViewById<TextView>(R.id.phonenumbertext).text = phno

            dialog.findViewById<Button>(R.id.editprofile).setOnClickListener {
                val intent = Intent(this, ProfileActivity::class.java)
                intent.putExtra("name",username)
                intent.putExtra("email",firebaseauth.currentUser?.email)
                startActivity(intent)
                dialog.dismiss()
            }

            dialog.findViewById<TextView>(R.id.DialogUsername).text = username

            dialog.findViewById<TextView>(R.id.DialogEmail).text = firebaseauth.currentUser?.email

            dialog.show()

        }
        navView.setNavigationItemSelectedListener {
            when(it.itemId){
                R.id.Members ->{
                    startActivity(intentdasboard)
                }
                R.id.createMeet ->{
                    startActivity(intentjoinmeet)
                }
                R.id.managemeet ->{
                    startActivity(intentmanagemeet)
                }
                R.id.MyProfile -> {
                    startActivity(intentmyprofile)
                }
                R.id.Logout ->{
                    Toast.makeText(this,"Logged out ", Toast.LENGTH_SHORT).show()
                    firebaseauth.signOut()
                    finish()
                    startActivity(intentMainActivity)

                }
            }
            true
        }


    }





    override fun onOptionsItemSelected(item: MenuItem): Boolean {   // this is to make the hamberger work so that drawer opens

        NavHeadername.text=username                            // setting the nav header name to username  (IT IS THE NAV HEADER )
        NavHeaderemail.text =firebaseauth.currentUser?.email   // setting the nav user email to email
        Glide                                                 // user to download url of the image
            .with(this)
            .load(curUserUrl)
            .centerCrop()
            .placeholder(R.drawable.ic_baseline_person_24)
            .into(nav_user_image);

        if(toggle.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item)
    }
}