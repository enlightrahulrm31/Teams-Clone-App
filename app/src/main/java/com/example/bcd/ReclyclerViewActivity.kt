package com.example.bcd

import android.content.ContentValues.TAG
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
import kotlinx.android.synthetic.main.activity_reclycler_view.*
import kotlinx.android.synthetic.main.activity_sign_in.view.*
import kotlinx.android.synthetic.main.row_users.*

class ReclyclerViewActivity : AppCompatActivity() {
    lateinit var firebaseauth: FirebaseAuth
    lateinit var database: FirebaseFirestore
    var  currentemail:String ? =null

    private val db :FirebaseFirestore = FirebaseFirestore.getInstance()
    private val collectionReference:CollectionReference = db.collection("users")
    var userAdapter: UserAdapter?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        firebaseauth = FirebaseAuth.getInstance()
        database = FirebaseFirestore.getInstance()
        val u = firebaseauth.currentUser
        currentemail = u?.email
        setContentView(R.layout.activity_reclycler_view)
        setupRecyclerview()
        Toast.makeText(this,"1", Toast.LENGTH_SHORT).show()


    }

    fun setupRecyclerview(){
        val  query : Query = collectionReference
        val firestoreRecyclerOptions : FirestoreRecyclerOptions<UserModel> =FirestoreRecyclerOptions.Builder<UserModel>()
            .setQuery(query,UserModel::class.java).build()
        userAdapter = UserAdapter(firestoreRecyclerOptions,this)
        recyclerView.layoutManager = LinearLayoutManager(this)
            recyclerView.adapter = userAdapter

    }

    override fun onStart() {
        super.onStart()
        Toast.makeText(this,"2 ", Toast.LENGTH_SHORT).show()
        userAdapter?.startListening()

    }

    override fun onDestroy() {
        super.onDestroy()
        userAdapter?.startListening()

    }
}