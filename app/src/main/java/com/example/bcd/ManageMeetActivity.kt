package com.example.bcd

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.bcd.databinding.ActivityMainBinding
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import kotlinx.android.synthetic.main.activity_manage_meet.*
import java.util.*

class ManageMeetActivity : AppCompatActivity() {
    private  lateinit var picker : MaterialTimePicker
    private  lateinit var calender : Calendar
    private  lateinit var alarmManager: AlarmManager
    private  lateinit var pendingIntent: PendingIntent
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manage_meet)
        createNotificationchannel()
        selecttime.setOnClickListener {
            showTimePicker()

        }
        setalarm.setOnClickListener {

            setAlarm()

        }
        cancealarm.setOnClickListener {

            cancelAlarm()

        }

    }

    private fun cancelAlarm() {

        alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val intent = Intent(this,AlarmReciever::class.java)

        pendingIntent = PendingIntent.getBroadcast(this,0,intent,0)

        alarmManager.cancel(pendingIntent)

        Toast.makeText(this,"alarm cancelled ", Toast.LENGTH_LONG).show()



    }

    private fun setAlarm() {

        alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val intent = Intent(this,AlarmReciever::class.java)

        pendingIntent = PendingIntent.getBroadcast(this,0,intent,0)

        alarmManager.setRepeating(
                AlarmManager.RTC_WAKEUP,calender.timeInMillis,
                AlarmManager.INTERVAL_DAY,pendingIntent

        )
        Toast.makeText(this,"alarm set successfully", Toast.LENGTH_LONG).show()




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
                selecttimetext.text= String.format("%02d",picker.hour- 12)+ " : " + String.format("%02d",picker.minute) + "PM"

            }
            else{
                selecttimetext.text = String.format("%02d",picker.hour)+ " : " + String.format("%02d",picker.minute) + "AM"

            }
            calender = Calendar.getInstance()
            calender[Calendar.HOUR_OF_DAY] = picker.hour
            calender[Calendar.MINUTE] = picker.minute
            calender[Calendar.SECOND] = 0
            calender[Calendar.MILLISECOND] = 0

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