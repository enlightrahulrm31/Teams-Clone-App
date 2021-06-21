package com.example.bcd

import android.app.*
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
    var curday: Int =1
    var curmonthDay: Int =1
    var curyear: Int =1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manage_meet)
        createNotificationchannel()
        selectdate.setOnClickListener {
            showDatePicker()
        }
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
    private fun showDatePicker() {
        Toast.makeText(this,"date picker",Toast.LENGTH_LONG).show()
        val cal  =Calendar.getInstance()
        val year =cal.get(Calendar.YEAR)
        val month =cal.get(Calendar.MONTH)
        val date =cal.get(Calendar.DATE)
        val  datepickerDialog = DatePickerDialog(this, DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
            selectdatetext.text= "Date:" + dayOfMonth + "/" + (month+1) + "/" + year
            curday= dayOfMonth
            curmonthDay = month
            curyear = year

        },year,month,date)
        datepickerDialog.show()
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