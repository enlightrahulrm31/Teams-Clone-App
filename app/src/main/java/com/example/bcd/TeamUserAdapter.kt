package com.example.bcd

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Context.*
import android.content.Intent
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.content.ContextCompat.getSystemServiceName
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import kotlinx.android.synthetic.main.activity_dashboard.view.*
import kotlinx.android.synthetic.main.row_users.view.*
import kotlinx.android.synthetic.main.team_row_user.view.*
import java.util.*
import kotlin.coroutines.coroutineContext

class TeamUserAdapter(options: FirestoreRecyclerOptions<TeamMeetingModel>, context: Context):
        FirestoreRecyclerAdapter<TeamMeetingModel, TeamUserAdapter.TeamUserAdapterVH>(options){
     var k = context
    private  lateinit var calender : Calendar
    private  lateinit var alarmManager: AlarmManager
    private  lateinit var pendingIntent: PendingIntent
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
        holder.thisacceptinvite.setOnClickListener {
            createNotificationchannel()
            setAlarm()
        }

    }
    fun setAlarm() {
        alarmManager = k.getSystemService(ALARM_SERVICE) as AlarmManager
        val intent = Intent(k,AlarmReciever::class.java)
        pendingIntent = PendingIntent.getBroadcast(k,0,intent,0)
        calender = Calendar.getInstance()
        calender[Calendar.MONTH] = curmonthDay!!
        calender[Calendar.YEAR] = curyear!!
        calender[Calendar.DATE] = curday!!
        calender[Calendar.HOUR_OF_DAY] = curhour!!
        calender[Calendar.MINUTE] = curminute!!
        alarmManager.setRepeating(
                AlarmManager.RTC_WAKEUP,calender.timeInMillis,
                AlarmManager.INTERVAL_DAY,pendingIntent

        )
        Toast.makeText(k,"alarm set successfully", Toast.LENGTH_LONG).show()

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

    }

}



/*
class UserAdapter(options: FirestoreRecyclerOptions<UserModel>, context: Context) :
        FirestoreRecyclerAdapter<UserModel, UserAdapter.UserAdapterVH>(options) {
    var k = context
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserAdapterVH {
        return UserAdapterVH(
                LayoutInflater.from(parent.context).inflate(R.layout.row_users, parent, false)
        )
    }

    override fun onBindViewHolder(holder: UserAdapterVH, position: Int, model: UserModel) {
        holder.thisuserName.text = model.name
        holder.thisuseremail.text = model.email
        holder.thisuserbbutton.setOnClickListener {
            Toast.makeText(k, holder.thisuserName.text, Toast.LENGTH_SHORT).show()   // this will make appear the name for which the button you click
        }

    }

    class UserAdapterVH(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var thisuserName = itemView.Tvusername
        var thisuseremail = itemView.Tvuseremail
        var thisuserbbutton = itemView.Tvbutton

    }

}*/
