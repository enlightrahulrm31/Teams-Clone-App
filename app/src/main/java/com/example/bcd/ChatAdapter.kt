import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.bcd.*
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.firebase.ui.firestore.ObservableSnapshotArray
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.chat_user_left.view.*
class ChatAdapter(options: FirestoreRecyclerOptions<ChatModel>, context: Context,mail:String):
    FirestoreRecyclerAdapter<ChatModel, ChatAdapter.ChatAdapterVH>(options){
    var k = context
   lateinit var firebaseauth: FirebaseAuth
    var flag:Int = 1
    var CurUserEmail:String =mail
    lateinit var mail:String;
    private val MESSAGE_TYPE_LEFT = 0
    private val MESSAGE_TYPE_RIGHT = 1
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatAdapterVH {
            return ChatAdapterVH(
                    LayoutInflater.from(parent.context).inflate(R.layout.chat_user_right, parent, false)
            )
    }
    override fun onBindViewHolder(holder: ChatAdapterVH, position: Int, model: ChatModel) {
       holder.thismessage.text = model.chattext.toString()
        Glide      // used to download url of the image
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