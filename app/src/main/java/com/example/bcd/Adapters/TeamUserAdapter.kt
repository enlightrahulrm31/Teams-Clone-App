package com.example.bcd.Adapters

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Context.*
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.bcd.Reciever.AlarmReciever
import com.example.bcd.RecyclerViewActivity.MeetingChatRecyclerViewActivity
import com.example.bcd.R
import com.example.bcd.Model.TeamMeetingModel
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.team_row_user.view.*
import org.jitsi.meet.sdk.JitsiMeet
import org.jitsi.meet.sdk.JitsiMeetActivity
import org.jitsi.meet.sdk.JitsiMeetConferenceOptions
import java.net.URL
import java.util.*

var map = mutableMapOf<String, Any?>()
var firebaseauthpublic: FirebaseAuth  =FirebaseAuth.getInstance()
var namemap :String = firebaseauthpublic.currentUser?.email!!

class TeamUserAdapter(options: FirestoreRecyclerOptions<TeamMeetingModel>, context: Context):
        FirestoreRecyclerAdapter<TeamMeetingModel, TeamUserAdapter.TeamUserAdapterVH>(options){

     var k = context

    private  lateinit var calender : Calendar
    private  lateinit var alarmManager: AlarmManager
    private  lateinit var pendingIntent: PendingIntent

    var database: FirebaseFirestore  = FirebaseFirestore.getInstance()
    var curday:Int ?=null

    var curmonthDay:Int ?=null
    var curyear:Int ?=null

    var curhour:Int ?=null
    var curminute:Int ?=null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TeamUserAdapterVH {
            return TeamUserAdapterVH(
                    LayoutInflater.from(parent.context).inflate(R.layout.team_row_user, parent, false)
            )
    }


    override fun onBindViewHolder(holder: TeamUserAdapterVH, position: Int, model: TeamMeetingModel) {
        holder.thismeetingdescription.text = model.description
        holder.thismeetorganiser.text=  model.useremail

        curday = model.day
        curmonthDay =model.month
        curyear = model.year
        curhour =model.hour
        curminute =model.min

        val serverurl = URL("https://meet.jit.si")

        var defaultOption = JitsiMeetConferenceOptions.Builder()
                .setServerURL(serverurl)
                .setWelcomePageEnabled(false).build()
        JitsiMeet.setDefaultConferenceOptions(defaultOption)

        holder.thisdate.text = " " + model.day + "/" + (model.month!!+1) + "/" + model.year!!

        if(model.hour!! > 12) {
            holder.thistime.text = String.format("%02d", model.hour!! - 12) + " : " + String.format("%02d", model.min!!) + "PM"
        }
        else{
            holder.thistime.text = String.format("%02d", model.hour!!) + " : " + String.format("%02d", model.min!!) + "PM"
        }

        holder.thisacceptinvite.setOnClickListener {

            if(map[namemap] != null ){
                Toast.makeText(k,"You cant accept multiple invites at a time ", Toast.LENGTH_LONG).show()
                Toast.makeText(k,"Accept after alarm for first finishes", Toast.LENGTH_SHORT).show()
            }
            else {
                createNotificationchannel()
                setAlarm(model.hour!!, model.min!!, model.day!!, model.month!!, model.year!!)
                holder.thisacceptinvite.setBackgroundColor(Color.GREEN)                                                          // Changing the background color of button of user that user get to know that he has accepted the invite
                holder.thisacceptinvite.setText("INVITE ACCEPTED")                                                              // Changing the text of button so that user get to know that he has accepted the invite
            }
        }

        holder.thisjoinmeet.setOnClickListener {
            val opt = JitsiMeetConferenceOptions.Builder().setRoom(model.day.toString()+model.year.toString()+model.month.toString()+model.hour.toString()+model.min.toString())
                    .setWelcomePageEnabled(false).build()
            JitsiMeetActivity.launch(k,opt)
        }

        holder.thischat.setOnClickListener {
            Toast.makeText(k,"Chat activity", Toast.LENGTH_LONG).show()
           gotomeetchat(model.day.toString()+model.year.toString()+model.month.toString()+model.hour.toString()+model.min.toString())
        }
    }

    private fun gotomeetchat(curtimes:String) {
        val intent = Intent(k, MeetingChatRecyclerViewActivity::class.java)
        intent.putExtra("meettimes",curtimes)
        k.startActivity(intent)
    }

    fun setAlarm(hr:Int, min:Int,day:Int, month:Int ,year:Int) {

        alarmManager = k.getSystemService(ALARM_SERVICE) as AlarmManager
        val intent = Intent(k, AlarmReciever::class.java)

        pendingIntent = PendingIntent.getBroadcast(k,0,intent,0)

        calender = Calendar.getInstance()
        calender[Calendar.MONTH] = month
        calender[Calendar.YEAR] = year
        calender[Calendar.DATE] = day
        calender[Calendar.HOUR_OF_DAY] = hr
        calender[Calendar.MINUTE] = min


        if(hr>12){
            Toast.makeText(k,"alarm set successfully for: " +String.format("%02d",hr- 12)+ " : " + String.format("%02d",min) + " PM",Toast.LENGTH_LONG).show()

        }
        else{
            Toast.makeText(k,"alarm set successfully for: " +String.format("%02d",hr)+ " : " + String.format("%02d",min) + " PM",Toast.LENGTH_LONG).show()

        }

        alarmManager.setRepeating(
                AlarmManager.RTC_WAKEUP,calender.timeInMillis,
                AlarmManager.INTERVAL_DAY,pendingIntent
        )
        map[namemap] = 1
    }
    fun createNotificationchannel(){

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val name:CharSequence = "ALARM CHANNEL"
            val description = "Channel for alarm manager"

            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel ("AlarmManager",name ,importance)

            channel.description= description

            val notificationManager = k.getSystemService(
                    NotificationManager::class.java
            )
            notificationManager.createNotificationChannel(channel)
        }
    }

    class TeamUserAdapterVH(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var thismeetingdescription = itemView.Tvmeetingdescription
        var thismeetorganiser = itemView.Tvoraganiser
        var thisacceptinvite = itemView.acceptinvite
        var thisjoinmeet =  itemView.joinmeeting
        var thischat = itemView.joinchatmeetbutton
        var thisdate = itemView.Tvdate
        var thistime = itemView.Tvtime
    }

}
