package gdg.com.br.todolist

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

import java.util.ArrayList

import gdg.com.br.todolist.adapter.RecyclerViewAdapter
import gdg.com.br.todolist.model.ToDo

class MainActivity : AppCompatActivity() {
  private var recyclerView: RecyclerView? = null
  private var recyclerViewAdapter: RecyclerViewAdapter? = null
  private var addTaskBox: EditText? = null
  private var databaseReference: DatabaseReference? = null
  private var allTask: MutableList<ToDo>? = null

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
    allTask = ArrayList()
    databaseReference = FirebaseDatabase.getInstance().reference
    addTaskBox = findViewById(R.id.add_task_box) as EditText
    recyclerView = findViewById(R.id.task_list) as RecyclerView
    recyclerView!!.layoutManager = LinearLayoutManager(this)
    recyclerViewAdapter = RecyclerViewAdapter(allTask)
    recyclerView!!.adapter = recyclerViewAdapter
    val addTaskButton = findViewById(R.id.add_task_button) as Button
    addTaskButton.setOnClickListener(View.OnClickListener {
      val enteredTask = addTaskBox!!.text.toString()
      if (TextUtils.isEmpty(enteredTask)) {
        Toast.makeText(this@MainActivity, "You must enter a task first", Toast.LENGTH_LONG).show()
        return@OnClickListener
      }
      if (enteredTask.length < 6) {
        Toast.makeText(this@MainActivity, "Task count must be more than 6",
            Toast.LENGTH_LONG).show()
        return@OnClickListener
      }
      val taskObject = ToDo(enteredTask)
      databaseReference!!.push().setValue(taskObject)
      addTaskBox!!.setText("")
    })
    databaseReference!!.addChildEventListener(object : ChildEventListener {
      override fun onChildAdded(dataSnapshot: DataSnapshot, s: String) {
        getAllTask(dataSnapshot)
      }

      override fun onChildChanged(dataSnapshot: DataSnapshot, s: String) {
        getAllTask(dataSnapshot)
      }

      override fun onChildRemoved(dataSnapshot: DataSnapshot) {
        taskDeletion(dataSnapshot)
      }

      override fun onChildMoved(dataSnapshot: DataSnapshot, s: String) {}

      override fun onCancelled(databaseError: DatabaseError) {}
    })
  }

  private fun getAllTask(dataSnapshot: DataSnapshot) {
    for (singleSnapshot in dataSnapshot.children) {
      val taskTitle = singleSnapshot.getValue<String>(String::class.java)
      allTask!!.add(ToDo(taskTitle))

      recyclerViewAdapter!!.notifyDataSetChanged()

    }
  }

  private fun taskDeletion(dataSnapshot: DataSnapshot) {
    for (singleSnapshot in dataSnapshot.children) {
      val taskTitle = singleSnapshot.getValue<String>(String::class.java)
      for (i in allTask!!.indices) {
        if (allTask!![i].task == taskTitle) {
          allTask!!.removeAt(i)
        }
      }
      recyclerViewAdapter!!.notifyDataSetChanged()
    }
  }
}

