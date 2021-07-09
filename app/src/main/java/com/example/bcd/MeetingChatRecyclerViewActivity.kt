package com.example.bcd

import ChatAdapter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.messaging.FirebaseMessaging
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_chat_recycler_view.*
import kotlinx.android.synthetic.main.activity_meeting_chat_recycler_view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
const val TOPIC = "/topics/myTopic"
class MeetingChatRecyclerViewActivity : AppCompatActivity() {

    lateinit var firebaseauth: FirebaseAuth
    lateinit var database: FirebaseFirestore

    private val db : FirebaseFirestore = FirebaseFirestore.getInstance()

    var collectionReference: CollectionReference? =null
    var userAdapter: ChatAdapter?= null

    lateinit var ToEmail : String
    lateinit var FromEmail :String
    lateinit var userurl :String

    var  time:Long = 0
    var name:String ?=null
    var u= ChatData()
    var  token:String = ""
    val TAG = "MainActivity"

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_meeting_chat_recycler_view)

        firebaseauth = FirebaseAuth.getInstance()
        database = FirebaseFirestore.getInstance()

        FromEmail = firebaseauth.currentUser?.email.toString()                                             // this will give me the email id of current user
        token = intent.getStringExtra("meettimes").toString()

        FirebaseMessaging.getInstance().subscribeToTopic(TOPIC)                                            // for notification

        database.collection("users").get()                                                   // it is used to retrive all data of user from firestore database
                .addOnSuccessListener { result ->
                    for (document in result) {
                        if (document.data["email"].toString() == FromEmail) {                                 // if the email from document is found equal to email of signed in user then we will replace username to loged in user name
                            userurl = document.data["userurl"].toString()
                            break
                        }
                    }
                }

        u= ChatData()

       setupRecyclerview()

        sendmessagebuttonmeet.setOnClickListener {

            time = System.currentTimeMillis()
            val title = FromEmail
            val message = SendMessageTextmeet.text.toString()
            if(title.isNotEmpty() && message.isNotEmpty()) {
                PushNotification(
                    NotificationData(title, message),
                    TOPIC
                ).also {
                    sendNotification(it)
                }
            }
            createmessagedoumnent(FromEmail)
            SendMessageTextmeet.text.clear()
        }
    }

    // code for sending notification

    private fun sendNotification(notification: PushNotification) = CoroutineScope(Dispatchers.IO).launch {
        try {

            val response = RetrofitInstance.api.postNotification(notification)

            if(response.isSuccessful) {
                Log.d(TAG, "Response: ${Gson().toJson(response)}")
            } else {
                Log.e(TAG, response.errorBody().toString())
            }

        } catch(e: Exception) {

            Log.e(TAG, e.toString())
        }
    }

    private fun createmessagedoumnent(frommail:String) {

        u.CHATTEXT = SendMessageTextmeet.text.toString()
        u.SENDEREMAIL=FromEmail
        u.SENDERURL= userurl
        u.CURTIME=time
        database.collection(token).add(u)                                                            // creating database of sender and reciever with unique id as token key  // Now I have to create a key between sender and reciever such that it will unique for these pairs and both reciever and sneder can acess this key
    }

    fun setupRecyclerview(){

        collectionReference = db.collection(token)
        val query: Query = collectionReference!!

        val firestoreRecyclerOptions: FirestoreRecyclerOptions<ChatModel> =
                FirestoreRecyclerOptions.Builder<ChatModel>()
                        .setQuery(query, ChatModel::class.java).build()

        userAdapter = ChatAdapter(firestoreRecyclerOptions, this)
        // calling adapter class
        meetingchatrecyclerView.layoutManager = LinearLayoutManager(this)                 // team recycler view is the id for for recycler view item which is present in activity boarding

        meetingchatrecyclerView.adapter = userAdapter
    }

    override fun onStart() {
        super.onStart()
        userAdapter?.startListening()
    }

    override fun onDestroy() {
        super.onDestroy()
        userAdapter?.startListening()
    }
}
