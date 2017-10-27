package gdg.com.br.todolist.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import gdg.com.br.todolist.R;
import gdg.com.br.todolist.model.ToDo;

public class RecyclerViewHolder extends RecyclerView.ViewHolder {
    private static final String TAG = RecyclerViewHolder.class.getSimpleName();
    public ImageView markIcon;
    public TextView categoryTitle;
    public ImageView deleteIcon;

    public RecyclerViewHolder(final View itemView, final List<ToDo> taskObject) {
        super(itemView);
        categoryTitle = (TextView) itemView.findViewById(R.id.task_title);
        markIcon = (ImageView) itemView.findViewById(R.id.task_icon);
        deleteIcon = (ImageView) itemView.findViewById(R.id.task_delete);
        deleteIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), "Delete icon has been clicked", Toast.LENGTH_LONG).show();
                String taskTitle = taskObject.get(getAdapterPosition()).getTask();
                Log.d(TAG, "Task Title " + taskTitle);
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference();

                Query query = ref.orderByChild("task").equalTo(taskTitle);
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot appleSnapshot : dataSnapshot.getChildren()) {
                            appleSnapshot.getRef().removeValue();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.e(TAG, "onCancelled", databaseError.toException());
                    }
                });
            }
        });
    }
}