package com.example.bcd

import ChatAdapter
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.android.synthetic.main.activity_boarding.*
import kotlinx.android.synthetic.main.activity_chat_recycler_view.*
import kotlinx.android.synthetic.main.activity_reclycler_view.*
import java.sql.Time
import java.util.*

class ChatRecyclerViewActivity : AppCompatActivity() {
    lateinit var firebaseauth: FirebaseAuth
    lateinit var database: FirebaseFirestore
    private val db :FirebaseFirestore = FirebaseFirestore.getInstance()
    var collectionReference:CollectionReference ? =null
    var userAdapter: ChatAdapter?= null
    lateinit var ToEmail : String
    lateinit var FromEmail :String
    lateinit var tokenkey :String
    lateinit var userurl :String
    var  time:Long = 0
    var name:String ?=null
    var u= ChatData()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_recycler_view)
        firebaseauth = FirebaseAuth.getInstance()
        database = FirebaseFirestore.getInstance()
         ToEmail  = intent.getStringExtra("senderemail").toString()  // email of user to whom we are sending message
         FromEmail = firebaseauth.currentUser?.email.toString()   // this will give me the email id of current user
        supportActionBar?.title = intent.getStringExtra("sendername").toString()
        database.collection("users").get()    // it is used to retrive all data of user from firestore database
                .addOnSuccessListener { result ->
                    for (document in result) {
                        if (document.data["email"].toString() == FromEmail) {   // if the email from document is found equal to email of signed in user then we will replace username to loged in user name
                            userurl = document.data["userurl"].toString()
                            name = document.data["name"].toString()
                            break
                        }
                    }
                }
        u= ChatData()
        var cnt:Int =0;
           // setting the name to whom sender to send message
         tokenkey = constructkey(FromEmail,ToEmail)  // it is used to retrive all data of user from firestore database
          // when the key matches the required document then adapter will update otherwise  it wont and this is how is we will recieve different chat rooms for particular users
      //  setupRecyclerview(FromEmail,ToEmail)   // setting up recylcer view to initiate CHAT Adapter and displaying item Chat  users
        setupRecyclerview(FromEmail,ToEmail)
        sendmessagebutton.setOnClickListener {
            cnt++;
            time = System.currentTimeMillis()
            createmessagedoumnent(FromEmail,ToEmail)
            SendMessageText.text.clear()
        }

    }
    private fun constructkey(a: String, b: String): String {  // creating the token key on basis of which our documnet in firebase database in formed
          if(a < b){
              return a+b                             // it will be unique for every pair of  users
          }
          else{
              return b+a
          }
    }
    private fun createmessagedoumnent(frommail:String,tomail:String) {
            // creating object of ChatData class
        u.CHATTEXT = SendMessageText.text.toString()
        u.SENDEREMAIL=FromEmail
      //  u.RECIEVEREMAIL=ToEmail
        u.SENDERURL= userurl
        u.CURTIME=time
        u.SENDERNAME=name
        var s:String =frommail+tomail
        database.collection(s).add(u)     // creating database of sender and reciever with unique id as token key
        s = tomail + frommail
        database.collection(s).add(u)     // creating database of sender and reciever with unique id as token key  // Now I have to create a key between sender and reciever such that it will unique for these pairs and both reciever and sneder can acess this key

    }
    fun setupRecyclerview(frommail:String,tomail:String){
        var s:String =frommail+tomail
            collectionReference = db.collection(s)
            val query: Query = collectionReference!!//.orderBy("curtime")
            //val  query2 : Query = collectionReference!!
            val firestoreRecyclerOptions: FirestoreRecyclerOptions<ChatModel> =
                    FirestoreRecyclerOptions.Builder<ChatModel>()
                            .setQuery(query, ChatModel::class.java).build()
            userAdapter = ChatAdapter(firestoreRecyclerOptions, this)       // calling adapter class
            chatrecyclerView.layoutManager = LinearLayoutManager(this)               // team recycler view is the id for for recycler view item which is present in activity boarding
            chatrecyclerView.adapter = userAdapter

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