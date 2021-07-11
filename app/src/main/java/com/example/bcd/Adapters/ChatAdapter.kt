import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.bcd.*
import com.example.bcd.Model.ChatModel
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import kotlinx.android.synthetic.main.chat_user_left.view.*
class ChatAdapter(options: FirestoreRecyclerOptions<ChatModel>, context: Context):
    FirestoreRecyclerAdapter<ChatModel, ChatAdapter.ChatAdapterVH>(options){
    var k = context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatAdapterVH {
            return ChatAdapterVH(
                    LayoutInflater.from(parent.context).inflate(R.layout.chat_user_left, parent, false)
            )
    }
    override fun onBindViewHolder(holder: ChatAdapterVH, position: Int, model: ChatModel) {
            holder.thismessage.text = model.chattext.toString()

            holder.thisuser.text = model.sendername

            holder.thismail.text = model.senderemail
            Glide      // used to download url of the image
                    .with(k)
                    .load(model.senderurl)
                    .centerCrop()
                    .placeholder(null)
                    .into(holder.thisimage);
    }
    class ChatAdapterVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
            var thismessage = itemView.messagefromuser
            var thisimage = itemView.imagebutton
            var thisuser = itemView.Username
            var thismail = itemView.sendermail
    }

}