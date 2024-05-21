package com.example.project_travel_1.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.project_travel_1.R;
import com.example.project_travel_1.adapters.TicketsAdapter;
import com.example.project_travel_1.Interfaces.TicketsInterface;
import com.example.project_travel_1.forAPI.LocaleCode;
import com.example.project_travel_1.objects.Ticket;
import com.example.project_travel_1.objects.TwoPoints;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class ListTicketsFragment extends Fragment implements TicketsInterface {

    private static final String ARG_PARAM5 = "selected_ticket";
    private static final String ARG_PARAM1 = "text_from";
    private static final String ARG_PARAM2 = "text_to";
    private static final String ARG_PARAM3 = "depart_date";
    private static final String ARG_PARAM4 = "return_date";

    ArrayList<String> params = new ArrayList<>();
    View view;
    Retrofit retrofit;
    RecyclerView recyclerView;
    TicketsAdapter ticketsAdapter;
    List<Ticket> ticketList = new ArrayList<>();
    String date_from, date_to;

    public ListTicketsFragment() {

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            params.add(0,getArguments().getString(ARG_PARAM1));
            params.add(1,getArguments().getString(ARG_PARAM2));
            params.add(2,getArguments().getString(ARG_PARAM3));
            params.add(3,getArguments().getString(ARG_PARAM4));

            Log.d("MyTag", "Получили "+params.toString());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_list_tickets, container, false);
        recyclerView = view.findViewById(R.id.tickets_rec_view);
        ticketsAdapter = new TicketsAdapter(this, getActivity(), ticketList);

        retrofit = new Retrofit.Builder()
                .baseUrl("https://www.travelpayouts.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(ticketsAdapter);

        toIata();


        return view;
    }

    //получаем код города
    public void toIata()
    {
        LocaleCode service = retrofit.create(LocaleCode.class);
        String q = params.get(0)+" "+params.get(1);
        date_from = params.get(3);
        date_to = params.get(2);


        Call<TwoPoints> codes = service.getIataCode(q);
        codes.enqueue(new Callback<TwoPoints>() {
            @Override
            public void onResponse(Call<TwoPoints> call, Response<TwoPoints> response) {
                String origin_iata = response.body().getOrigin().getIata();
                String destination_iata = response.body().getDestination().getIata();

                Log.d("MyTag", "origin_iata = "+origin_iata);
                Log.d("MyTag", "destination_iata = "+destination_iata);

                getTickets(origin_iata, destination_iata, date_from, date_to);
            }

            @Override
            public void onFailure(Call<TwoPoints> call, Throwable t) {
                Log.d("MyTag", "error"+t);
            }
        });
    }

    public void getTickets(String from, String to, String date_from, String date_to)
    {
        String myURL = "https://api.travelpayouts.com/aviasales/v3/prices_for_dates?origin="+from+"&destination="+to+"&departure_at="+date_to+"&return_at="+date_from+"&unique=false&sorting=price&direct=false&currency=rub&limit=30&page=1&one_way=true&token=748b21841bf0db13fdf7fa8e57fb67d8";
        Log.d("MyTag", myURL);
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(myURL)
                .build();


        client.newCall(request).enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(@NonNull okhttp3.Call call, @NonNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NonNull okhttp3.Call call, @NonNull okhttp3.Response response) throws IOException {

                try (ResponseBody responseBody = response.body()) {
                    if (!response.isSuccessful()) {
                        throw new IOException("Запрос к серверу не был успешен: " +
                                response.code() + " " + response.message());
                    }


                    JSONObject resultJSON = new JSONObject(responseBody.string());
                    JSONArray data = resultJSON.getJSONArray("data");

                    Log.d("MyTag","response body: "+ resultJSON);

                    //заполняем массив билетов
                    setTickets(data);


                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        });



    }

    private void setTickets(JSONArray data) throws JSONException {
        JSONObject ticketJSON;
        Ticket ticket;

        if(data != null && data.length()>0)
        {
            for ( int i = 0; i < data.length(); i++)
            {
                ticketJSON = data.getJSONObject(i);
                String time_to = normalTime(ticketJSON.getString("departure_at"));
                String time_at = normalTime(ticketJSON.getString("return_at"));

                ticket = new Ticket(params.get(0), params.get(1),
                        ticketJSON.getString("origin_airport"), ticketJSON.getString("destination_airport"),
                        ticketJSON.getString("airline"), time_to,
                        time_at, ticketJSON.getInt("price"),
                        ticketJSON.getInt("duration_to"), ticketJSON.getInt("duration_back"),
                        date_to, date_from);
                ticketList.add(ticket);
            }

            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                    recyclerView.setAdapter(ticketsAdapter);
                }
            });

        }
    }

    private String normalTime(String str)
    {
        String time = "";
        boolean flag = false;
        for (char el: str.toCharArray())
        {
            if ( el == 'T')
                flag = true;
            else if ( el == '+')
                break;
            else if ( flag == true )
                time += el;
        }
        return  time;
    }


    @Override
    public void choose(int position) {

        Bundle bundle = new Bundle();
        bundle.putSerializable(ARG_PARAM5,ticketList.get(position));

        Log.d("MyTag", "Отправили "+ ticketList.get(position).toString());
        Navigation.findNavController(view).navigate(R.id.action_listTicketsFragment_to_attractionsFragment, bundle);


    }
}