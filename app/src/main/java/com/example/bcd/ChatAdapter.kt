import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.bcd.*
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import kotlinx.android.synthetic.main.chat_user.view.*
import kotlinx.android.synthetic.main.nav_header.*
import kotlinx.android.synthetic.main.row_users.view.*

class ChatAdapter(options: FirestoreRecyclerOptions<ChatModel>, context: Context,frommail:String):
    FirestoreRecyclerAdapter<ChatModel, ChatAdapter.ChatAdapterVH>(options) {
    var k = context
   // var url :String =Cururl
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatAdapterVH {
        return ChatAdapterVH(
            LayoutInflater.from(parent.context).inflate(R.layout.chat_user, parent, false)
        )
    }
    override fun onBindViewHolder(holder: ChatAdapterVH, position: Int, model: ChatModel) {
       holder.thismessage.text = model.chattext.toString()
        Glide      // user to download url of the image
                .with(k)
                .load(model.senderurl)
                .centerCrop()
                .placeholder(R.drawable.ic_baseline_person_24)
                .into(holder.thisimage);
    }

    class ChatAdapterVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var thismessage = itemView.messagefromuser
        var thisimage = itemView.imagebutton

    }

}