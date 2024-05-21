package com.example.project_travel_1.fragments;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.project_travel_1.R;
import com.example.project_travel_1.adapters.MemoryAdapter;
import com.example.project_travel_1.Interfaces.RecyclerViewInterface;
import com.example.project_travel_1.adapters.ObjectDiffutilCallback;
import com.example.project_travel_1.objects.Memory;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class SeeMemoryFragment extends Fragment implements RecyclerViewInterface {


    private static final String ARG_PARAM1 = "text_title";
    private String param1;
    View view;
    MemoryAdapter memoryAdapter;
    private DatabaseReference mDataBase;
    private FirebaseAuth mAuth;
    private RecyclerView recyclerView;
    private ArrayList<Memory> memories = new ArrayList<Memory>();


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            param1 = getArguments().getString(ARG_PARAM1);

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_see_memory, container, false);


        recyclerView = view.findViewById(R.id.memory_rec_view);
        mAuth = FirebaseAuth.getInstance();
        mDataBase = FirebaseDatabase.getInstance().getReference(mAuth.getUid()).child("memory");



        memoryAdapter = new MemoryAdapter(getActivity(), memories, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(memoryAdapter);


        getData();



        return view;
    }


    private void getData()
    {

        ValueEventListener eventListener = new ValueEventListener() {


            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {


                for( DataSnapshot ds: snapshot.getChildren())
                {

                    String description = ds.child("description").getValue().toString();
                    String name = ds.child("name").getValue().toString();
                    String photo_uri = ds.child("photo_uri").getValue().toString();


                    memories.add(new Memory(name, description, photo_uri, ds.getKey()));

                }
                if (memoryAdapter.getItemCount()>0)
                {
                    ObjectDiffutilCallback objectDiffutilCallback = new ObjectDiffutilCallback(memoryAdapter.getData(), memories);
                    DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(objectDiffutilCallback);
                    diffResult.dispatchUpdatesTo(memoryAdapter);
                    Log.d("MyTag", "Вызвали метод удаления 2");
                }
                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                recyclerView.setAdapter(memoryAdapter);


            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity(), "Ошибка", Toast.LENGTH_SHORT).show();
            }
        };
        mDataBase.addValueEventListener(eventListener);
    }


    @Override
    public void onItemClick(int position) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(ARG_PARAM1, memories.get(position));
        //bundle.putString(ARG_PARAM1, memories.get(position).getKey());
        Log.d("MyTag", "Передали "+memories.get(position).getKey());
        memories.clear();
        Navigation.findNavController(view).navigate(R.id.action_seeMemoryFragment_to_itemMemoryFullFragment2, bundle);
    }



}