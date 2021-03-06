package com.example.bcd.RecyclerViewActivity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bcd.Adapters.TeamUserAdapter
import com.example.bcd.Activity.BoardingActivity
import com.example.bcd.R
import com.example.bcd.Model.TeamMeetingModel
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.android.synthetic.main.activity_team_recycler_view.*

class TeamRecyclerViewActivity : AppCompatActivity() {

    private val database : FirebaseFirestore = FirebaseFirestore.getInstance()
    private val collectionReference: CollectionReference = database.collection("teammeetings")

    var userAdapter: TeamUserAdapter?= null

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_team_recycler_view)

        getSupportActionBar()?.setDisplayHomeAsUpEnabled(true)
        getSupportActionBar()?.setTitle("Meetings")

        setupRecyclerview()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        val intent = Intent(this, BoardingActivity::class.java)

        startActivity(intent)
        finish()

        return super.onOptionsItemSelected(item)
    }

    fun setupRecyclerview(){
        val  query : Query = collectionReference.orderBy("day")//.orderBy("hour").orderBy("min")                       // sorting the query by min,hr,day

        val firestoreRecyclerOptions : FirestoreRecyclerOptions<TeamMeetingModel> =
                FirestoreRecyclerOptions.Builder<TeamMeetingModel>()
                        .setQuery(query, TeamMeetingModel::class.java).build()

        userAdapter = TeamUserAdapter(firestoreRecyclerOptions, this)

        teamrecyclerView.layoutManager = LinearLayoutManager(this)                                                   // team recycler view is the id for for recycler view item which is present in activity boarding
        teamrecyclerView.adapter = userAdapter
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