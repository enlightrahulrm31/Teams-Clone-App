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

class ChatRecyclerViewActivity : AppCompatActivity() {
    lateinit var firebaseauth: FirebaseAuth
    lateinit var database: FirebaseFirestore
    private val db :FirebaseFirestore = FirebaseFirestore.getInstance()
    var collectionReference:CollectionReference ? =null
    var userAdapter: ChatAdapter?= null
    lateinit var ToEmail : String
    lateinit var FromEmail :String
    lateinit var tokenkey :String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_recycler_view)
        firebaseauth = FirebaseAuth.getInstance()
        database = FirebaseFirestore.getInstance()
         ToEmail  = intent.getStringExtra("senderemail").toString()  // email of user to whom we are sending message
         FromEmail = firebaseauth.currentUser?.email.toString()   // this will give me the email id of current user
         tokenkey = constructkey(FromEmail,ToEmail)  // it is used to retrive all data of user from firestore database
          // when the key matches the required document then adapter will update otherwise  it wont and this is how is we will recieve different chat rooms for particular users
        setupRecyclerview()   // setting up recylcer view to initiate CHAT Adapter and displaying item Chat  users
        sendmessagebutton.setOnClickListener {
            createmessagedoumnent()
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
    private fun createmessagedoumnent() {
        var u= ChatData()     // creating object of ChatData class
        u.CHATTEXT = SendMessageText.text.toString()
        u.SENDEREMAIL=FromEmail
        u.RECIEVEREMAIL=ToEmail
          // Now I have to create a key between sender and reciever such that it will unique for these pairs and both reciever and sneder can acess this key
        database.collection(tokenkey).add(u)     // creating database of sender and reciever with unique id as token key
            .addOnSuccessListener {
                Toast.makeText(this,"Chat document created successfully", Toast.LENGTH_SHORT).show()
            }
    }
    fun setupRecyclerview(){
        collectionReference = db.collection(tokenkey)
        val  query : Query = collectionReference!!
        val firestoreRecyclerOptions : FirestoreRecyclerOptions<ChatModel> =
            FirestoreRecyclerOptions.Builder<ChatModel>()
                .setQuery(query,ChatModel::class.java).build()
        userAdapter = ChatAdapter(firestoreRecyclerOptions,this)   // calling adapter class
        chatrecyclerView.layoutManager = LinearLayoutManager(this)   // team recycler view is the id for for recycler view item which is present in activity boarding
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