package com.example.project_travel_1.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.project_travel_1.R;
import com.example.project_travel_1.objects.Memory;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ItemMemoryFullFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ItemMemoryFullFragment extends Fragment {

    TextView text_title, text_description;
    ImageView image_photo;

    private static final String ARG_PARAM1 = "text_title";


    Memory memory;

    public ItemMemoryFullFragment() {
        // Required empty public constructor
    }


    public static ItemMemoryFullFragment newInstance(String param1, String param2) {
        ItemMemoryFullFragment fragment = new ItemMemoryFullFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);

        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            memory = (Memory) getArguments().getSerializable(ARG_PARAM1);
            Log.d("MyTag", "Получили "+memory.getKey());

        }
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_item_memory_full, container, false);

        text_title = (TextView) view.findViewById(R.id.text_title);
        image_photo = (ImageView) view.findViewById(R.id.image_photo);
        text_description = (TextView) view.findViewById(R.id.text_description);

        text_title.setText(memory.getName());
        text_description.setText(memory.getDescription());
        Picasso.get().load(memory.getPhoto_uri()).into(image_photo);

        return view;

    }
}