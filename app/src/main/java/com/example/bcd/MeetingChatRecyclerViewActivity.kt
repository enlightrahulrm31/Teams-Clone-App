package com.example.bcd

import ChatAdapter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.android.synthetic.main.activity_chat_recycler_view.*
import kotlinx.android.synthetic.main.activity_meeting_chat_recycler_view.*

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
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_meeting_chat_recycler_view)
        firebaseauth = FirebaseAuth.getInstance()
        database = FirebaseFirestore.getInstance()
        FromEmail = firebaseauth.currentUser?.email.toString()   // this will give me the email id of current user
        token = intent.getStringExtra("meettimes").toString()
        database.collection("users").get()    // it is used to retrive all data of user from firestore database
                .addOnSuccessListener { result ->
                    for (document in result) {
                        if (document.data["email"].toString() == FromEmail) {   // if the email from document is found equal to email of signed in user then we will replace username to loged in user name
                            userurl = document.data["userurl"].toString()
                            break
                        }
                    }
                }
        u= ChatData()
       setupRecyclerview()
        sendmessagebuttonmeet.setOnClickListener {
            time = System.currentTimeMillis()
            Toast.makeText(this,token,Toast.LENGTH_LONG).show()
            createmessagedoumnent(FromEmail)
            SendMessageTextmeet.text.clear()
        }
    }
    private fun createmessagedoumnent(frommail:String) {
        u.CHATTEXT = SendMessageTextmeet.text.toString()
        u.SENDEREMAIL=FromEmail
        u.SENDERURL= userurl
        u.CURTIME=time
       database.collection(token).add(u)     // creating database of sender and reciever with unique id as token key  // Now I have to create a key between sender and reciever such that it will unique for these pairs and both reciever and sneder can acess this key
    }
    fun setupRecyclerview(){
        Toast.makeText(this,token,Toast.LENGTH_LONG).show()
        collectionReference = db.collection(token)
        val query: Query = collectionReference!!
        //val  query2 : Query = collectionReference!!
        val firestoreRecyclerOptions: FirestoreRecyclerOptions<ChatModel> =
                FirestoreRecyclerOptions.Builder<ChatModel>()
                        .setQuery(query, ChatModel::class.java).build()
        userAdapter = ChatAdapter(firestoreRecyclerOptions, this)       // calling adapter class
        meetingchatrecyclerView.layoutManager = LinearLayoutManager(this)               // team recycler view is the id for for recycler view item which is present in activity boarding
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
