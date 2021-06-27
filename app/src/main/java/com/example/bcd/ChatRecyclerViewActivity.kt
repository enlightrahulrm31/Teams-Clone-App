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
    var CurrentUserId:String ?=null
    var CurrentUserEmail:String ?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_recycler_view)
 //
        firebaseauth = FirebaseAuth.getInstance()
        database = FirebaseFirestore.getInstance()
        CurrentUserId =firebaseauth.currentUser?.uid.toString()
        database.collection("users").get()    // it is used to retrive all data of user from firestore database
            .addOnSuccessListener { result ->
                for (document in result) {
                    if (document.data["uid"].toString() == CurrentUserId) {   // if the email from document is found equal to email of signed in user then we will replace username to loged in user name
                        CurrentUserEmail = document.data["email"].toString()
                        break
                    }
                }
            }
        val sendToEmailUsermail : String =intent.getStringExtra("senderemail").toString()
        val key:String = CurrentUserEmail +  sendToEmailUsermail
        collectionReference = db.collection(key)   // when the key matches the required document then adapter will update otherwise  it wont and this is how is we will recieve different chat rooms for particular users
        setupRecyclerview()
        sendmessagebutton.setOnClickListener {
            createmessagedoumnent(intent.getStringExtra("senderemail").toString())
        }
    }

    private fun createmessagedoumnent(senderemail:String) {
        var u= ChatData()
        u.CHATTEXT = SendMessageText.text.toString()
        u.SENDEREMAIL=CurrentUserEmail
        u.RECIEVEREMAIL= senderemail
        val key:String =CurrentUserEmail+senderemail   // Now I have to create a key between sender and reciever such that it will unique for these pairs and both reciever and sneder can acess this key
        database.collection(key).add(u)
            .addOnSuccessListener {
                Toast.makeText(this,"Chat document created successfully", Toast.LENGTH_SHORT).show()
            }
    }
    fun setupRecyclerview(){
        val  query : Query = collectionReference!!
        val firestoreRecyclerOptions : FirestoreRecyclerOptions<ChatModel> =
            FirestoreRecyclerOptions.Builder<ChatModel>()
                .setQuery(query,ChatModel::class.java).build()
        userAdapter = ChatAdapter(firestoreRecyclerOptions,this)
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