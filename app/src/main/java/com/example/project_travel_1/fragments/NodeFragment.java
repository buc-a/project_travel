package com.example.project_travel_1.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.project_travel_1.Interfaces.RecyclerViewInterface;
import com.example.project_travel_1.R;
import com.example.project_travel_1.adapters.TaskAdapter;
import com.example.project_travel_1.objects.Attraction;
import com.example.project_travel_1.objects.Node;
import com.example.project_travel_1.objects.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class NodeFragment extends Fragment implements RecyclerViewInterface {


    TextView tv1, tv2, tv3, tv4, tv5, tv6, tv7, tv8;
    RecyclerView recyclerView;
    private FirebaseAuth mAuth;
    private DatabaseReference mDB;
    TaskAdapter taskAdapter;
    String task;

    List<Task> tasks = new ArrayList<>();
    private Node node;
    private String key;

    public NodeFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            node = (Node) getArguments().getSerializable("MyNode");
            key = getArguments().getString("node_key");

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view =  inflater.inflate(R.layout.fragment_node, container, false);

        mAuth = FirebaseAuth.getInstance();
        mDB = FirebaseDatabase.getInstance().getReference(mAuth.getUid()).child("nodes").child(key).child("toDo");

        tasks = node.getToDo();
        tv1 = (TextView) view.findViewById(R.id.city_to_city);
        tv2 = (TextView) view.findViewById(R.id.data_to_data);
        tv3 = (TextView) view.findViewById(R.id.my_attractions);
        tv4 = (TextView) view.findViewById(R.id.tv_price_1);
        tv5 = (TextView) view.findViewById(R.id.tv_duration_to_1);
        tv6 = (TextView) view.findViewById(R.id.tv_duration_back_1);
        tv7 = (TextView) view.findViewById(R.id.tv_departure_at_1);
        tv8 = (TextView) view.findViewById(R.id.tv_return_at_1);

        tv1.setText(node.getTicket().getOrigin() + " - "+ node.getTicket().getDestination());
        tv2.setText(node.getTicket().getData_to() + " - " + node.getTicket().getData_from());
        String attractions = "";
        for ( Attraction attraction: node.getAttractions())
        {
            attractions+= attraction.getName_place() + "\n";

        }
        tv3.setText(attractions);
        tv4.setText(node.getTicket().getPrice().toString() + " руб");
        tv5.setText("Время в пути: "+node.getTicket().getDuration_to()+"мин");
        tv6.setText("Время в пути: "+node.getTicket().getDuration_back()+"мин");
        tv7.setText(node.getTicket().getDeparture_at());
        tv8.setText(node.getTicket().getReturn_at());

        recyclerView= view.findViewById(R.id.rec_view_to_do);
        taskAdapter = new TaskAdapter(getActivity(), this, tasks);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(taskAdapter);

        return view;
    }

    @Override
    public void onItemClick(int position) {

        mDB.child(((Integer)position).toString()).child("done").setValue(true);

        Log.d("MyTag", "Установлено 1");
    }
}