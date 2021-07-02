package com.example.bcd

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import kotlinx.android.synthetic.main.nav_header.*
import kotlinx.android.synthetic.main.row_users.view.*
// added context argument in user adapter as we cant simply use this context here
// I am calling this user adapter class in my recycler view activity
class UserAdapter(options: FirestoreRecyclerOptions<UserModel>,context: Context) :
    FirestoreRecyclerAdapter<UserModel, UserAdapter.UserAdapterVH>(options) {
     var k = context
     var  name:String ?=null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserAdapterVH {
       //   val button:Button = dilog.findViewById(R.id.cancelbutton)
         return UserAdapterVH(
             LayoutInflater.from(parent.context).inflate(R.layout.row_users, parent, false)
         )
     }
     override fun onBindViewHolder(holder: UserAdapterVH, position: Int, model: UserModel) {
         holder.thisuserName.text = model.name
         holder.thisuseremail.text = model.email
         name= model.email
         Glide
                 .with(k)
                 .load(model.userurl)
                 .centerCrop()
                 .placeholder(R.drawable.ic_baseline_person_24)
                 .into(holder.thisuserimage)
         /*holder.thisuserbbutton.setOnClickListener {
             Toast.makeText(k, holder.thisuserName.text, Toast.LENGTH_SHORT).show()   // this will make appear the name for which the button you click
         }*/
        /* holder.itemView.setOnClickListener {
             val intent = Intent(k,ChatRecyclerViewActivity::class.java)  // replace teamrecyclerviewactivity to boarding activity
             intent.putExtra("senderemail",model.email)
             intent.putExtra("sendername",model.name)
             k.startActivity(intent)
         }*/
         holder.thismessage.setOnClickListener {
             val intent = Intent(k,ChatRecyclerViewActivity::class.java)  // replace teamrecyclerviewactivity to boarding activity
             intent.putExtra("senderemail",model.email)
             intent.putExtra("sendername",model.name)
             k.startActivity(intent)
         }
         holder.thiscallbutton.setOnClickListener {
             Toast.makeText(k,"Calling", Toast.LENGTH_SHORT).show()
         }

     }


    class UserAdapterVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
         var thisuserName = itemView.Tvusername
         var thisuseremail = itemView.Tvuseremail
       //  var thisuserbbutton = itemView.Tvbutton
         var thisuserimage = itemView.Tvuserimage
         var thiscallbutton = itemView.callbutton
          var thismessage = itemView.messagebutton
     }

}