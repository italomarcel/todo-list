package gdg.com.br.todolist;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import gdg.com.br.todolist.adapter.RecyclerViewAdapter;
import gdg.com.br.todolist.model.ToDo;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerViewAdapter recyclerViewAdapter;
    private EditText addTaskBox;
    private DatabaseReference databaseReference;
    private List<ToDo> allTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        allTask = new ArrayList<>();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        addTaskBox = (EditText) findViewById(R.id.add_task_box);
        recyclerView = (RecyclerView) findViewById(R.id.task_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewAdapter = new RecyclerViewAdapter(allTask);
        recyclerView.setAdapter(recyclerViewAdapter);
        Button addTaskButton = (Button) findViewById(R.id.add_task_button);
        addTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String enteredTask = addTaskBox.getText().toString();
                if (TextUtils.isEmpty(enteredTask)) {
                    Toast.makeText(MainActivity.this, "You must enter a task first", Toast.LENGTH_LONG).show();
                    return;
                }
                if (enteredTask.length() < 6) {
                    Toast.makeText(MainActivity.this, "Task count must be more than 6", Toast.LENGTH_LONG).show();
                    return;
                }
                ToDo taskObject = new ToDo(enteredTask);
                databaseReference.push().setValue(taskObject);
                addTaskBox.setText("");
            }
        });
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                getAllTask(dataSnapshot);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                getAllTask(dataSnapshot);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                taskDeletion(dataSnapshot);
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    private void getAllTask(DataSnapshot dataSnapshot) {
        for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
            String taskTitle = singleSnapshot.getValue(String.class);
            allTask.add(new ToDo(taskTitle));

            recyclerViewAdapter.notifyDataSetChanged();

        }
    }

    private void taskDeletion(DataSnapshot dataSnapshot) {
        for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
            String taskTitle = singleSnapshot.getValue(String.class);
            for (int i = 0; i < allTask.size(); i++) {
                if (allTask.get(i).getTask().equals(taskTitle)) {
                    allTask.remove(i);
                }
            }
            recyclerViewAdapter.notifyDataSetChanged();
        }
    }
}

