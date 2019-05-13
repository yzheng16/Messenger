package ca.nait.yzheng16.messenger.message

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import ca.nait.yzheng16.messenger.R
import ca.nait.yzheng16.messenger.models.User
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_new_message.*
import kotlinx.android.synthetic.main.user_row_new_message.view.*

class NewMessageActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_message)

        supportActionBar?.title = "Select User"

//        val adapter = GroupAdapter<ViewHolder>()
//        adapter.add(UserItem(0))
//        recyclerview_new_message.adapter = adapter

        fetchUsers()
    }

    private fun fetchUsers() {
        val ref = FirebaseDatabase.getInstance().getReference("/users")
        ref.addListenerForSingleValueEvent(object : ValueEventListener {

            override fun onDataChange(p0: DataSnapshot) {
                val adapter = GroupAdapter<ViewHolder>()

                p0.children.forEach{
                    val user = it.getValue(User::class.java)
                    if (user != null) {
                        adapter.add(UserItem(user))
                    }
                }
                adapter.setOnItemClickListener { item, view ->
                    val userItem = item as UserItem
                    val intent = Intent(view.context, ChatLogActivity::class.java)

                    intent.putExtra("USER_KEY", userItem.user)
                    startActivity(intent)
                    finish()
                }
                recyclerview_new_message.adapter = adapter
            }

            override fun onCancelled(p0: DatabaseError) {

            }

        })
    }
}

class UserItem(val user: User) : Item<ViewHolder>() {
    override fun getLayout(): Int {
        return R.layout.user_row_new_message
    }

    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.username_text_view_new_message.text = user.username
        Picasso.get().load(user.profileImageUrl).into(viewHolder.itemView.image_view_new_message)
    }

}
