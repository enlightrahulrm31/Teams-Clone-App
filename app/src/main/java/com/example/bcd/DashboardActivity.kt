package com.example.bcd

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_dashboard.*
import org.jitsi.meet.sdk.JitsiMeet
import org.jitsi.meet.sdk.JitsiMeetActivity
import org.jitsi.meet.sdk.JitsiMeetConferenceOptions
import java.net.URI
import java.net.URL

class DashboardActivity : AppCompatActivity() {
   lateinit var  serverurl:URL
    lateinit var firebaseauth: FirebaseAuth
    lateinit var database: FirebaseFirestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)
        firebaseauth = FirebaseAuth.getInstance()
        database = FirebaseFirestore.getInstance()
        val username:String? =getIntent().getStringExtra("UserName")
        val useremailid:String? =getIntent().getStringExtra("UserEmailid")
        val n = findViewById<TextView>(R.id.Uname)
         n.setText(username)
       //  e.setText(useremailid)
        var codebox :EditText = scode
        var Btn:Button = Buttonjoin
            serverurl = URL("https://meet.jit.si")
            var defaultOption =JitsiMeetConferenceOptions.Builder()
                .setServerURL(serverurl)
                .setWelcomePageEnabled(false).build()
            JitsiMeet.setDefaultConferenceOptions(defaultOption)

        Buttonjoin.setOnClickListener{
            val opt = JitsiMeetConferenceOptions.Builder().setRoom("dcsdfe121321awd")
                .setWelcomePageEnabled(false).build()
            JitsiMeetActivity.launch(this,opt)

        }
        buttonaddmeet.setOnClickListener {
            addmeeting()
        }


    }
    private fun addmeeting() {
        var u= TeamsMeetingData()
        u.DESCRIPTION=tvdescription.text.toString()
        database.collection("teammeetings").add(u)
            .addOnSuccessListener {
                Toast.makeText(this, "Meeting Created Successfully!.", Toast.LENGTH_SHORT).show()
                val intent = Intent(this,TeamRecyclerViewActivity::class.java)
                startActivity(intent)
                finish()
            }
    }
}