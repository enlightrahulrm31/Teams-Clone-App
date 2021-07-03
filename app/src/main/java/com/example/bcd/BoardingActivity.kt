package com.example.bcd

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
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.android.synthetic.main.activity_boarding.*
import kotlinx.android.synthetic.main.activity_reclycler_view.*
import kotlinx.android.synthetic.main.activity_team_recycler_view.*
import kotlinx.android.synthetic.main.custom_pop.*
import kotlinx.android.synthetic.main.nav_header.*

class BoardingActivity : AppCompatActivity() {
    lateinit var toggle :ActionBarDrawerToggle   // this will open the hammberger toogle bar in the action bar
    lateinit var firebaseauth: FirebaseAuth
    lateinit var database: FirebaseFirestore
     var  username:String = "NOT VALID"
    var curUserUrl:String ?=null
    // added new
    lateinit var db : FirebaseFirestore
    lateinit var collectionReference: CollectionReference
    var userAdapter: TeamUserAdapter?= null
    // delete it it not works
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
      //  setContentView(R.layout.activity_boarding) // replace row user by activity boarding
        db = FirebaseFirestore.getInstance()
        collectionReference = db.collection("teammeetings")
        setContentView(R.layout.activity_boarding)  // change it to activity_team_recycler_view as activity_reclycler_view
        // setupRecyclerview()
        var dialog :Dialog = Dialog(this)
        firebaseauth = FirebaseAuth.getInstance()
        database = FirebaseFirestore.getInstance()
        val drawerLayout : DrawerLayout = findViewById(R.id.drawerlayout)
        val navView:NavigationView =findViewById(R.id.nav_view)

        toggle = ActionBarDrawerToggle(this,drawerLayout,R.string.open,R.string.close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        val  intentjoinmeet : Intent = Intent(this,TeamRecyclerViewActivity::class.java)
        val  intentdasboard: Intent = Intent(this,ReclyclerViewActivity::class.java)
        val  intentMainActivity: Intent = Intent(this,MainActivity::class.java)
        val  intentmanagemeet: Intent = Intent(this,DashboardActivity::class.java)
        val u = firebaseauth.currentUser
        val em = u?.email
        database.collection("users").get()    // it is used to retrive all data of user from firestore database
                .addOnSuccessListener { result ->
                    for (document in result) {
                        if (document.data["email"].toString() == em) {   // if the email from document is found equal to email of signed in user then we will replace username to loged in user name
                            username = document.data["name"].toString()
                            curUserUrl = document.data["userurl"].toString()
                            break
                        }
                    }
                }
        /*createMeeting.setOnClickListener {
             startActivity(intentjoinmeet)
             finish()
        }*/
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
            dialog.findViewById<Button>(R.id.editprofile).setOnClickListener {
                val intent = Intent(this,ProfileActivity::class.java)
                intent.putExtra("name",username)
                intent.putExtra("email",firebaseauth.currentUser?.email)
                startActivity(intent)
            }
            dialog.findViewById<TextView>(R.id.DialogUsername).text = username
            dialog.findViewById<TextView>(R.id.DialogEmail).text = firebaseauth.currentUser?.email
            dialog.show()

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
                    Toast.makeText(this,"Logged out ", Toast.LENGTH_SHORT).show()
                    firebaseauth.signOut()
                    finish()
                    startActivity(intentMainActivity)

                }
            }
            true
        }


    }
    // newly added
    /*fun setupRecyclerview(){
        val  query : Query = collectionReference
        val firestoreRecyclerOptions : FirestoreRecyclerOptions<TeamMeetingModel> =
                FirestoreRecyclerOptions.Builder<TeamMeetingModel>()
                        .setQuery(query,TeamMeetingModel::class.java).build()
        userAdapter = TeamUserAdapter(firestoreRecyclerOptions,this)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = userAdapter
    }
    override fun onStart() {
        super.onStart()
        userAdapter?.startListening()

    }

    override fun onDestroy() {
        super.onDestroy()
        userAdapter?.startListening()

    }
    // delete it if not works*/
    /*fun setupRecyclerview(){
        val  query : Query = collectionReference.orderBy("day")//.orderBy("hour").orderBy("min") // sorting the query by min,hr,day
        val firestoreRecyclerOptions : FirestoreRecyclerOptions<TeamMeetingModel> =
                FirestoreRecyclerOptions.Builder<TeamMeetingModel>()
                        .setQuery(query,TeamMeetingModel::class.java).build()
        userAdapter = TeamUserAdapter(firestoreRecyclerOptions,this)
        teamrecyclerView.layoutManager = LinearLayoutManager(this)   // team recycler view is the id for for recycler view item which is present in activity boarding
        teamrecyclerView.adapter = userAdapter
    }
    override fun onStart() {
        super.onStart()
        userAdapter?.startListening()

    }

    override fun onDestroy() {
        super.onDestroy()
        userAdapter?.startListening()

    }*/


    override fun onOptionsItemSelected(item: MenuItem): Boolean {   // this is to make the hamberger work so that drawer opens

       // NavHeadername.text = incomingname
        NavHeadername.text=username  // setting the nav header name to username  (IT IS THE NAV HEADER )
        NavHeaderemail.text =firebaseauth.currentUser?.email   // setting the nav user email to email
        Glide      // user to download url of the image
            .with(this)
            .load(curUserUrl)
            .centerCrop()
            .placeholder(R.drawable.ic_baseline_person_24)
            .into(nav_user_image);

      //  Toast.makeText(this,username, Toast.LENGTH_SHORT).show()
        if(toggle.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item)
    }
}