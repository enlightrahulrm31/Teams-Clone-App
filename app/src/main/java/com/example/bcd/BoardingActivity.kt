package com.example.bcd

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.nav_header.*

class BoardingActivity : AppCompatActivity() {
    lateinit var toggle :ActionBarDrawerToggle   // this will open the hammberger toogle bar in the action bar
    lateinit var firebaseauth: FirebaseAuth
    lateinit var database: FirebaseFirestore
     var  username:String = "NOT VALID"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_boarding)
        firebaseauth = FirebaseAuth.getInstance()
        database = FirebaseFirestore.getInstance()
        val drawerLayout : DrawerLayout = findViewById(R.id.drawerlayout)
        val navView:NavigationView =findViewById(R.id.nav_view)

        toggle = ActionBarDrawerToggle(this,drawerLayout,R.string.open,R.string.close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        val  intentjoinmeet : Intent = Intent(this,DashboardActivity::class.java)
        val  intentdasboard: Intent = Intent(this,ReclyclerViewActivity::class.java)
        val  intentMainActivity: Intent = Intent(this,MainActivity::class.java)
        val  intentmanagemeet: Intent = Intent(this,ManageMeetActivity::class.java)
        val u = firebaseauth.currentUser
        val em = u?.email
        database.collection("users").get()    // it is used to retrive all data of user from firestore database
                .addOnSuccessListener { result ->
                    for (document in result) {
                        if (document.data["email"].toString() == em) {   // if the email from document is found equal to email of signed in user then we will replace username to loged in user name
                            username = document.data["name"].toString()
                            break
                        }
                    }
                }
        navView.setNavigationItemSelectedListener {
            when(it.itemId){
                // creating toast so that when we click it we get notified
                R.id.Members->{
                    startActivity(intentdasboard)
                }
                R.id.createMeet ->{
                    startActivity(intentjoinmeet)
                }
                R.id.managemeet ->{
                    startActivity(intentmanagemeet)
                }
                R.id.MyProfile ->Toast.makeText(this,"My Profile", Toast.LENGTH_SHORT).show()
                R.id.Logout ->{
                    Toast.makeText(this, "Logged out ", Toast.LENGTH_SHORT).show()
                    firebaseauth.signOut()
                    finish()
                    startActivity(intentMainActivity)

                }
            }
            true
        }


    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {   // this is to make the hamberger work so that drawer opens

       // NavHeadername.text = incomingname
        NavHeadername.text=username  // setting the nav header name to username
        NavHeaderemail.text =firebaseauth.currentUser?.email   // setting the nav user email to email
      //  Toast.makeText(this,username, Toast.LENGTH_SHORT).show()
        if(toggle.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item)
    }
}