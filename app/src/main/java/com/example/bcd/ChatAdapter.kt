import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.bcd.*
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import kotlinx.android.synthetic.main.chat_user.view.*
import kotlinx.android.synthetic.main.row_users.view.*

class ChatAdapter(options: FirestoreRecyclerOptions<ChatModel>, context: Context) :
    FirestoreRecyclerAdapter<ChatModel, ChatAdapter.ChatAdapterVH>(options) {
    var k = context
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatAdapterVH {
        return ChatAdapterVH(
            LayoutInflater.from(parent.context).inflate(R.layout.chat_user, parent, false)
        )
    }
    override fun onBindViewHolder(holder: ChatAdapterVH, position: Int, model: ChatModel) {
       holder.thismessage.text = model.chattext.toString()
    }

    class ChatAdapterVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var thismessage = itemView.messagefromuser

    }

}