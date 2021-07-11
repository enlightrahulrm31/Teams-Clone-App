package com.example.bcd.RecyclerViewActivity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bcd.Adapters.UserAdapter
import com.example.bcd.Activity.BoardingActivity
import com.example.bcd.R
import com.example.bcd.Model.UserModel
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.android.synthetic.main.activity_reclycler_view.*

class ReclyclerViewActivity : AppCompatActivity() {

    private val db :FirebaseFirestore = FirebaseFirestore.getInstance()
    private val collectionReference:CollectionReference = db.collection("users")
    var userAdapter: UserAdapter?= null

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_reclycler_view)

        getSupportActionBar()?.setDisplayHomeAsUpEnabled(true)
        getSupportActionBar()?.setTitle("My Contacts")

        setupRecyclerview()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        val intent = Intent(this, BoardingActivity::class.java)

        startActivity(intent)
        finish()

        return super.onOptionsItemSelected(item)
    }

    fun setupRecyclerview(){
        val  query : Query = collectionReference

        val firestoreRecyclerOptions : FirestoreRecyclerOptions<UserModel> =FirestoreRecyclerOptions.Builder<UserModel>()
            .setQuery(query, UserModel::class.java).build()

        userAdapter = UserAdapter(firestoreRecyclerOptions, this)
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