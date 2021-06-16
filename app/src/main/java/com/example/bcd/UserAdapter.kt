package com.example.bcd

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import kotlinx.android.synthetic.main.row_users.view.*

class UserAdapter(options: FirestoreRecyclerOptions<UserModel>) :
    FirestoreRecyclerAdapter<UserModel, UserAdapter.UserAdapterVH>(options) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserAdapterVH {
        return UserAdapterVH(LayoutInflater.from(parent.context).inflate(R.layout.row_users,parent,false))
    }

    override fun onBindViewHolder(holder: UserAdapterVH, position: Int, model: UserModel) {
        holder.thisuserName.text=model.name
        holder.thisuseremail.text=model.email

    }

    class UserAdapterVH(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var thisuserName = itemView.Tvusername
        var thisuseremail = itemView.Tvuseremail


    }

}