package com.example.bcd

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.android.synthetic.main.activity_reclycler_view.*
import kotlinx.android.synthetic.main.activity_sign_in.view.*

class ReclyclerViewActivity : AppCompatActivity() {

    private val db :FirebaseFirestore = FirebaseFirestore.getInstance()
    private val collectionReference:CollectionReference = db.collection("users")
    var userAdapter: UserAdapter?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reclycler_view)
        setupRecyclerview()
    }

    fun setupRecyclerview(){
        val  query : Query = collectionReference

        val firestoreRecyclerOptions : FirestoreRecyclerOptions<UserModel> =FirestoreRecyclerOptions.Builder<UserModel>()
            .setQuery(query,UserModel::class.java).build()
        userAdapter = UserAdapter(firestoreRecyclerOptions)

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = userAdapter
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