package gdg.com.br.todolist.adapter

import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.Query
import com.google.firebase.database.ValueEventListener

import gdg.com.br.todolist.R
import gdg.com.br.todolist.model.ToDo

class RecyclerViewHolder(itemView: View, taskObject: List<ToDo>) : RecyclerView.ViewHolder(
    itemView) {
  var markIcon: ImageView
  var categoryTitle: TextView
  var deleteIcon: ImageView

  init {
    categoryTitle = itemView.findViewById(R.id.task_title) as TextView
    markIcon = itemView.findViewById(R.id.task_icon) as ImageView
    deleteIcon = itemView.findViewById(R.id.task_delete) as ImageView
    deleteIcon.setOnClickListener { v ->
      Toast.makeText(v.context, "Delete icon has been clicked", Toast.LENGTH_LONG).show()
      val taskTitle = taskObject[adapterPosition].task
      Log.d(TAG, "Task Title " + taskTitle)
      val ref = FirebaseDatabase.getInstance().reference

      val query = ref.orderByChild("task").equalTo(taskTitle)
      query.addListenerForSingleValueEvent(object : ValueEventListener {
        override fun onDataChange(dataSnapshot: DataSnapshot) {
          for (appleSnapshot in dataSnapshot.children) {
            appleSnapshot.ref.removeValue()
          }
        }

        override fun onCancelled(databaseError: DatabaseError) {
          Log.e(TAG, "onCancelled", databaseError.toException())
        }
      })
    }
  }

  companion object {
    private val TAG = RecyclerViewHolder::class.java!!.getSimpleName()
  }
}