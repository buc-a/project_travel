package com.example.project_travel_1.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.project_travel_1.R;

import com.example.project_travel_1.objects.Ticket;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class FirstFragment extends Fragment {

    EditText text_from, text_to, depart_date, return_date;
    Button btn_find_ticket;
    String str_from, str_to , str_depart_date , str_return_date;

    String TOKEN = "748b21841bf0db13fdf7fa8e57fb67d8";
    Retrofit retrofit, retrofit2;
    List<Ticket> tickets;
    View view;
    JSONObject resultJSON;
    static ArrayList<String> list;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("message");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_first, container, false);


        retrofit2 = new Retrofit.Builder()
                .baseUrl("https://api.travelpayouts.com/aviasales/v3/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();


        tickets = new ArrayList<>();

        text_from = (EditText) view.findViewById(R.id.tv_from);
        text_to = (EditText) view.findViewById(R.id.tv_to);
        depart_date = (EditText) view.findViewById(R.id.et_depart_date);
        return_date = (EditText) view.findViewById(R.id.et_return_date) ;
        btn_find_ticket = (Button) view.findViewById(R.id.btn_find_ticket);

        btn_find_ticket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();

                String t1=text_from.getText().toString();
                String t2 = text_to.getText().toString();
                String t3 = depart_date.getText().toString();
                String t4 = return_date.getText().toString();
                if (!t1.isEmpty() && !t2.isEmpty() && !t3.isEmpty() && !t4.isEmpty())
                {
                    bundle.putString("text_from",t1);
                    bundle.putString("text_to", t2);
                    bundle.putString("depart_date",t3);
                    bundle.putString("return_date",t4);

                    Navigation.findNavController(view).navigate(R.id.action_firstFragment_to_listTicketsFragment,bundle);
                }
                else
                {
                    Toast.makeText(getActivity(), "Заполните все поля", Toast.LENGTH_SHORT);
                }

            }
        });

        return view;
    }


}