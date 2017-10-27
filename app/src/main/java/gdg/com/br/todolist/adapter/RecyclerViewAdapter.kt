package gdg.com.br.todolist.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import gdg.com.br.todolist.R
import gdg.com.br.todolist.model.ToDo

class RecyclerViewAdapter(
    private val task: List<ToDo>) : RecyclerView.Adapter<RecyclerViewHolder>() {

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder {
    val layoutView = LayoutInflater.from(parent.context).inflate(R.layout.to_do_list_item, parent,
        false)
    return RecyclerViewHolder(layoutView, task)
  }

  override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) {
    holder.categoryTitle.text = task[position].task
  }

  override fun getItemCount(): Int {
    return this.task.size
  }
}