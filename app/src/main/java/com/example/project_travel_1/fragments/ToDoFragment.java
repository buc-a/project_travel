package com.example.project_travel_1.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.project_travel_1.Interfaces.RecyclerViewInterface;
import com.example.project_travel_1.R;
import com.example.project_travel_1.adapters.TaskAdapter;
import com.example.project_travel_1.objects.Attraction;
import com.example.project_travel_1.objects.Node;
import com.example.project_travel_1.objects.Task;
import com.example.project_travel_1.objects.Ticket;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;


public class ToDoFragment extends Fragment implements RecyclerViewInterface {


    private static final String ARG_PARAM1 = "selected_ticket";
    //выбранные достопримечательности
    List<Attraction> attractions = new ArrayList<Attraction>();
    //выбранный билет
    Ticket ticket;
    View view;
    Button btn_set_task, btn_save;
    EditText et_task;
    RecyclerView recyclerView;
    TaskAdapter taskAdapter;

    String task;
    Task obj;

    List<Task> tasks = new ArrayList<>();
    public ToDoFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            attractions = getArguments().getParcelableArrayList("list_attractions");
            ticket = (Ticket) getArguments().getSerializable(ARG_PARAM1);
            if ( attractions.size()>0)
                Log.d("MyTag", "Получили "+ attractions.get(0).getName_place());


        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_to_do, container, false);
        btn_set_task = (Button) view.findViewById(R.id.btn_setTask);
        btn_save = (Button)view.findViewById(R.id.save_node);
        et_task = (EditText) view.findViewById(R.id.et_new_task);
        recyclerView= view.findViewById(R.id.rec_view__to_do);
        taskAdapter = new TaskAdapter(getActivity(), this, tasks);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(taskAdapter);


        //добавление новой задачи
        btn_set_task.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                task = et_task.getText().toString();
                obj = new Task(task, false);
                taskAdapter.notifyItemRangeRemoved(0, tasks.size());

                if ( !task.isEmpty())
                {
                    tasks.add(obj);
                }
                et_task.setText("");

                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                recyclerView.setAdapter(taskAdapter);
            }
        });

        //сохранение задачи и переход в начало
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth mAuth = FirebaseAuth.getInstance();
                DatabaseReference mDataBase = FirebaseDatabase.getInstance().getReference(mAuth.getUid()).child("nodes");
                Node node = new Node(tasks, ticket, attractions);
                mDataBase.push().setValue(node);

                Toast.makeText(getActivity(), "Карточка добавлена", Toast.LENGTH_SHORT).show();
                Navigation.findNavController(view).navigate(R.id.action_toDoFragment_to_firstFragment);

            }
        });
        return view;
    }


    @Override
    public void onItemClick(int position) {

    }
}