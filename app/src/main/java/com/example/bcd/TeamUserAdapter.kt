package com.example.bcd

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import kotlinx.android.synthetic.main.row_users.view.*
import kotlinx.android.synthetic.main.team_row_user.view.*

class TeamUserAdapter(options: FirestoreRecyclerOptions<TeamMeetingModel>, context: Context):
        FirestoreRecyclerAdapter<TeamMeetingModel, TeamUserAdapter.TeamUserAdapterVH>(options){
     var k = context
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TeamUserAdapterVH {
        return TeamUserAdapterVH(
                LayoutInflater.from(parent.context).inflate(R.layout.team_row_user, parent, false)
        )
    }

    override fun onBindViewHolder(holder: TeamUserAdapterVH, position: Int, model: TeamMeetingModel) {
        holder.thismeetingdescription.text = model.description

    }

    class TeamUserAdapterVH(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var thismeetingdescription = itemView.Tvmeetingdescription

    }
}
/*
class UserAdapter(options: FirestoreRecyclerOptions<UserModel>, context: Context) :
        FirestoreRecyclerAdapter<UserModel, UserAdapter.UserAdapterVH>(options) {
    var k = context
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserAdapterVH {
        return UserAdapterVH(
                LayoutInflater.from(parent.context).inflate(R.layout.row_users, parent, false)
        )
    }

    override fun onBindViewHolder(holder: UserAdapterVH, position: Int, model: UserModel) {
        holder.thisuserName.text = model.name
        holder.thisuseremail.text = model.email
        holder.thisuserbbutton.setOnClickListener {
            Toast.makeText(k, holder.thisuserName.text, Toast.LENGTH_SHORT).show()   // this will make appear the name for which the button you click
        }

    }

    class UserAdapterVH(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var thisuserName = itemView.Tvusername
        var thisuseremail = itemView.Tvuseremail
        var thisuserbbutton = itemView.Tvbutton

    }

}*/
