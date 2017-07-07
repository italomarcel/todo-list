package gdg.com.br.todolist.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import gdg.com.br.todolist.R;
import gdg.com.br.todolist.model.ToDo;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewHolder> {
    private List<ToDo> task;

    public RecyclerViewAdapter(List<ToDo> task) {
        this.task = task;
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.to_do_list_item, parent, false);
        return new RecyclerViewHolder(layoutView, task);
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, int position) {
        holder.categoryTitle.setText(task.get(position).getTask());
    }

    @Override
    public int getItemCount() {
        return this.task.size();
    }
}