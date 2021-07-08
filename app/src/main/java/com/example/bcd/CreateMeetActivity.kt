package com.example.bcd

import android.app.*
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_create_meet.*
import java.net.URL
import java.util.*

class CreateMeetActivity : AppCompatActivity() {
   lateinit var  serverurl:URL
    lateinit var firebaseauth: FirebaseAuth
    lateinit var database: FirebaseFirestore
    private  lateinit var picker : MaterialTimePicker
    private  lateinit var calender : Calendar
    private  lateinit var alarmManager: AlarmManager
    private  lateinit var pendingIntent: PendingIntent
    private  lateinit var randomString: String
    var useremailid:String?=null
    var curday: Int =1
    var curmonthDay: Int =1
    var curyear: Int =1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_meet)
        getSupportActionBar()?.setDisplayHomeAsUpEnabled(true)
        getSupportActionBar()?.setTitle("Create Meet")
        firebaseauth = FirebaseAuth.getInstance()
        database = FirebaseFirestore.getInstance()
        val CurrentUserId: String=firebaseauth.currentUser?.uid.toString()
        val alphabet: List<Char> = ('a'..'z') + ('A'..'Z') + ('0'..'9')
        randomString = List(20) { alphabet.random() }.joinToString("")
        randomString+=CurrentUserId
        database.collection("users").get()    // it is used to retrive all data of user from firestore database
                .addOnSuccessListener { result ->
                    for (document in result) {
                        if (document.data["uid"].toString() == CurrentUserId) {   // if the email from document is found equal to email of signed in user then we will replace username to loged in user name
                            useremailid = document.data["email"].toString()
                            break
                        }
                    }
                }
        createNotificationchannel()
        selectdatemeet.setOnClickListener {
            showDatePicker()
        }
        selecttimemeet.setOnClickListener {
            showTimePicker()
        }
        buttonmmeet.setOnClickListener {
            addmeeting()
        }


    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val intent = Intent(this,BoardingActivity::class.java)
        startActivity(intent)
        finish()
        return super.onOptionsItemSelected(item)
    }
    private fun showDatePicker() {
        //Toast.makeText(this,"date picker",Toast.LENGTH_LONG).show()
        val cal  =Calendar.getInstance()
        val year =cal.get(Calendar.YEAR)
        val month =cal.get(Calendar.MONTH)
        val date =cal.get(Calendar.DATE)
        val  datepickerDialog = DatePickerDialog(this, DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
            selectdatetextmeet.text= "Date:" + dayOfMonth + "/" + (month+1) + "/" + year
            curday= dayOfMonth
            curmonthDay = month
            curyear = year

        },year,month,date)
        datepickerDialog.show()
    }
    private fun showTimePicker() {
        picker = MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_12H)
                .setHour(12)
                .setMinute(0)
                .setTitleText("Select Alarm Time")
                .build()

        picker.show(supportFragmentManager,"AlarmManager")

        picker.addOnPositiveButtonClickListener {
            if(picker.hour>12){
                selecttimetextmeet.text= String.format("%02d",picker.hour- 12)+ " : " + String.format("%02d",picker.minute) + "PM"

            }
            else{
                selecttimetextmeet.text = String.format("%02d",picker.hour)+ " : " + String.format("%02d",picker.minute) + "AM"

            }
            calender = Calendar.getInstance()
            calender[Calendar.DATE] = curday
            calender[Calendar.MONTH] = curmonthDay
            calender[Calendar.YEAR] = curyear
            calender[Calendar.DATE] = curday
            calender[Calendar.HOUR_OF_DAY] = picker.hour
            calender[Calendar.MINUTE] = picker.minute
            calender[Calendar.SECOND] = 0
            calender[Calendar.MILLISECOND] = 0

        }
    }
    private fun addmeeting() {
        var u= TeamsMeetingData()
        u.DESCRIPTION=tvdescription.text.toString()
        u.USEREMAIL=useremailid
        u.DAY = curday
        u.HOUR = picker.hour
        u.MIN = picker.minute
        u.MONTH = curmonthDay
        u.YEAR = curyear
        u.METTINGCODE=randomString
        database.collection( "teammeetings").add(u)
                .addOnSuccessListener {
                    Toast.makeText(this, "Meeting Created Successfully!.", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this,BoardingActivity::class.java)  // change it to teamRecyclerview
                    startActivity(intent)
                    finish()
                }
    }
    private  fun createNotificationchannel(){

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val name:CharSequence = "ALARM CHANNEL"
            val description = "Channel for alarm manager"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel ("AlarmManager",name ,importance)
            channel.description= description
            val notificationManager = getSystemService(
                    NotificationManager::class.java
            )
            notificationManager.createNotificationChannel(channel)
        }
    }
}