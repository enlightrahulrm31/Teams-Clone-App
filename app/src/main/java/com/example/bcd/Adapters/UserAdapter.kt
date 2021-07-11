package com.example.bcd.Adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.bcd.*
import com.example.bcd.Activity.CallingActivity
import com.example.bcd.Model.UserModel
import com.example.bcd.RecyclerViewActivity.ChatRecyclerViewActivity
import com.example.bcd.Validation.Validation
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import kotlinx.android.synthetic.main.row_users.view.*

class UserAdapter(options: FirestoreRecyclerOptions<UserModel>, context: Context) :                                     // added context argument in user adapter as we cant simply use this context here
    FirestoreRecyclerAdapter<UserModel, UserAdapter.UserAdapterVH>(options) {                                          // I am calling this user adapter class in my recycler view activity

     var k = context
     var  name:String ?=null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserAdapterVH {
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

         var phonenumber :String = model.phoneno.toString()

         holder.thismessage.setOnClickListener {

             if(model.email == namemap){
                 Toast.makeText(k,"Its you", Toast.LENGTH_SHORT).show()
             }
             else {
                 val intent = Intent(k, ChatRecyclerViewActivity::class.java)

                 intent.putExtra("senderemail", model.email)
                 intent.putExtra("sendername", model.name)

                 Toast.makeText(k, model.name, Toast.LENGTH_SHORT).show()

                 k.startActivity(intent)
             }
         }

         holder.thiscallbutton.setOnClickListener {
             if(model.email == namemap){
                 Toast.makeText(k,"Its you", Toast.LENGTH_SHORT).show()
             }
             else {
                 val validation = Validation()

                 if (model.phoneno != null && validation.check_phone_number(model.phoneno!!)) {

                     Toast.makeText(k, "Calling", Toast.LENGTH_SHORT).show()

                     val intent = Intent(k, CallingActivity::class.java)
                     intent.putExtra("sendername", model.name)
                     intent.putExtra("photourl", model.userurl)
                     intent.putExtra("phonennumber", model.phoneno)

                     k.startActivity(intent)
                 }
                 else {
                     Toast.makeText(k, "User Phone number is not Valid", Toast.LENGTH_SHORT).show()
                 }
             }
         }

     }

    class UserAdapterVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
         var thisuserName = itemView.Tvusername
         var thisuseremail = itemView.Tvuseremail
         var thisuserimage = itemView.Tvuserimage
         var thiscallbutton = itemView.callbutton
         var thismessage = itemView.messagebutton
     }

}