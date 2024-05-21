package com.example.project_travel_1.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.project_travel_1.Interfaces.RecyclerViewInterface;
import com.example.project_travel_1.R;
import com.example.project_travel_1.adapters.NodeAdapter;
import com.example.project_travel_1.objects.Attraction;
import com.example.project_travel_1.objects.Node;
import com.example.project_travel_1.objects.Ticket;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MyNodesFragment extends Fragment implements RecyclerViewInterface {


    View view;
    RecyclerView recyclerView;
    NodeAdapter nodeAdapter;
    private DatabaseReference mDataBase;
    private FirebaseAuth mAuth;
    private List<Node> nodes = new ArrayList<>();
    private List<String> keys = new ArrayList<>();
    public MyNodesFragment() {

    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_my_nodes, container, false);

        mAuth = FirebaseAuth.getInstance();
        mDataBase = FirebaseDatabase.getInstance().getReference(mAuth.getUid()).child("nodes");

        recyclerView = view.findViewById(R.id.rec_view_nodes);
        nodeAdapter = new NodeAdapter(getActivity(), nodes, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(nodeAdapter);

        setData();


        return view;
    }

    //нажатие на элемент
    @Override
    public void onItemClick(int position) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("MyNode", nodes.get(position));
        bundle.putString("node_key", keys.get(position));
        Navigation.findNavController(view).navigate(R.id.action_myNodesFragment_to_nodeFragment, bundle);
        nodeAdapter.notifyItemRangeRemoved(0, nodes.size());
        nodes.clear();
    }

    //добавление данных на экран
    public void setData()
    {

        ValueEventListener eventListener = new ValueEventListener(){

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for ( DataSnapshot ds: snapshot.getChildren())
                {
                    Node node = ds.getValue(Node.class);
                    nodes.add(node);
                    keys.add(ds.getKey());
                }
                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                recyclerView.setAdapter(nodeAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity(), "Ошибка", Toast.LENGTH_SHORT).show();
            }
        };
        mDataBase.addValueEventListener(eventListener);


    }
}