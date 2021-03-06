package com.example.bcd.RecyclerViewActivity

import ChatAdapter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bcd.DataClass.ChatData
import com.example.bcd.Model.ChatModel
import com.example.bcd.R
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.android.synthetic.main.activity_chat_recycler_view.*

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
         ToEmail  = intent.getStringExtra("senderemail").toString()                              // email of user to whom we are sending message

         FromEmail = firebaseauth.currentUser?.email.toString()                                        // this will give me the email id of current user

        supportActionBar?.title = intent.getStringExtra("sendername").toString()

        database.collection("users").get()                                      // it is used to retrive all data of user from firestore database
                .addOnSuccessListener { result ->
                    for (document in result) {
                        if (document.data["email"].toString() == FromEmail) {              // if the email from document is found equal to email of signed in user then we will replace username to loged in user name
                            userurl = document.data["userurl"].toString()
                            name = document.data["name"].toString()
                            break
                        }
                    }
                }

        u= ChatData()
        var cnt:Int =0;
         tokenkey = constructkey(FromEmail,ToEmail)                              // it is used to retrive all data of user from firestore database
                                                                                 // when the key matches the required document then adapter will update otherwise  it wont and this is how is we will recieve different chat rooms for particular users

        setupRecyclerview(FromEmail,ToEmail)

        sendmessagebutton.setOnClickListener {
            cnt++;
            time = System.currentTimeMillis()
            createmessagedoumnent(FromEmail,ToEmail)
            SendMessageText.text.clear()
        }

    }

    private fun constructkey(a: String, b: String): String {                     // creating the token key on basis of which our document in firebase database in formed
          if(a < b){
              return a+b                                                        // it will be unique for every pair of  users
          }
          else{
              return b+a
          }
    }

    private fun createmessagedoumnent(frommail:String,tomail:String) {
                                                                               // creating object of ChatData class
        u.CHATTEXT = SendMessageText.text.toString()
        u.SENDEREMAIL=FromEmail
        u.SENDERURL= userurl
        u.CURTIME=time
        u.SENDERNAME=name
        var s:String =frommail+tomail
        database.collection(s).add(u)                                         // creating database of sender and reciever with unique id as token key
        s = tomail + frommail
        database.collection(s).add(u)                                         // creating database of sender and reciever with unique id as token key  // Now I have to create a key between sender and reciever such that it will unique for these pairs and both reciever and sneder can acess this key

    }

    fun setupRecyclerview(frommail:String,tomail:String){
        var s:String =frommail+tomail
            collectionReference = db.collection(s)
            val query: Query = collectionReference!!.orderBy("curtime")

            val firestoreRecyclerOptions: FirestoreRecyclerOptions<ChatModel> =
                    FirestoreRecyclerOptions.Builder<ChatModel>()
                            .setQuery(query, ChatModel::class.java).build()

            userAdapter = ChatAdapter(firestoreRecyclerOptions, this)

            chatrecyclerView.layoutManager = LinearLayoutManager(this)          // calling adapter class

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