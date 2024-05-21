package com.example.project_travel_1.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Parcelable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.project_travel_1.R;
import com.example.project_travel_1.adapters.AttractionsAdapter;
import com.example.project_travel_1.Interfaces.AttractionsInterface;
import com.example.project_travel_1.objects.Attraction;
import com.example.project_travel_1.objects.Ticket;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class AttractionsFragment extends Fragment implements AttractionsInterface {
    private static final String ARG_PARAM1 = "selected_ticket";

    View view;

    //радиус (в метрах) вокруг которого искать достопримечательности
    private static int RADIUS = 14000;
    private Ticket ticket;
    Button btn_next;
    RecyclerView recyclerView;
    AttractionsAdapter adapter;
    List<Attraction> attractions = new ArrayList<>();

    List<Attraction> likeAttractions = new ArrayList<>();



    public AttractionsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

            ticket = (Ticket) getArguments().getSerializable(ARG_PARAM1);
            Log.d("MyTag", "Получили билет "+ ticket.toString());


        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view =  inflater.inflate(R.layout.fragment_attractions, container, false);
        btn_next = view.findViewById(R.id.btn_toToDo);
        recyclerView = view.findViewById(R.id.attractions_rec_view);
        adapter = new AttractionsAdapter( getActivity(), attractions,this);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);

        getCoord();

        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList("list_attractions", (ArrayList<? extends Parcelable>) likeAttractions);
                bundle.putSerializable(ARG_PARAM1,ticket);

                Navigation.findNavController(view).navigate(R.id.action_attractionsFragment_to_toDoFragment, bundle);
            }
        });
        return  view;
    }


    public void getCoord()
    {
        String URL1 = "http://api.opentripmap.com/0.1/ru/places/geoname?name="+ticket.getDestination()+"&apikey=5ae2e3f221c38a28845f05b69e88ce800e00a6ce845d3a17b6c51ffa";
        Log.d("MyTag", URL1);
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(URL1)
                .build();

        client.newCall(request).enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(@NonNull okhttp3.Call call, @NonNull IOException e) {
                Log.d("MyTag", "Ошибка "+e.getMessage());
            }

            @Override
            public void onResponse(@NonNull okhttp3.Call call, @NonNull okhttp3.Response response) throws IOException {
                try (ResponseBody responseBody = response.body()) {
                    if (!response.isSuccessful()) {
                        throw new IOException("Запрос к серверу не был успешен: " +
                                response.code() + " " + response.message());
                    }

                    JSONObject jsonObject = new JSONObject(responseBody.string());

                    //координаты выбранного города

                    double lat = jsonObject.getDouble("lat");
                    double lon = jsonObject.getDouble("lon");

                    Log.d("MyTag", "Широта: "+String.valueOf(lat)+" Долгота: "+String.valueOf(lon));

                    getAttractions(lat, lon, RADIUS);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    private void getAttractions( double lat, double lon, int radius)
    {
        String myURL = "http://api.opentripmap.com/0.1/ru/places/radius?radius="+String.valueOf(radius)+
                "&lon="+String.valueOf(lon)+"&lat="+String.valueOf(lat)+
                "&kinds=interesting_places&apikey=5ae2e3f221c38a28845f05b69e88ce800e00a6ce845d3a17b6c51ffa";
        Log.d("MyTag", myURL);
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(myURL)
                .build();

        client.newCall(request).enqueue(new Callback() {
        @Override
        public void onFailure(@NonNull Call call, @NonNull IOException e) {
            Log.d("MyTag", "Ошибка " + e.getMessage());
        }

        @Override
        public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
            try (ResponseBody responseBody = response.body()) {
                if (!response.isSuccessful()) {
                    throw new IOException("Запрос к серверу не был успешен: " +
                            response.code() + " " + response.message());
                }

                JSONObject jsonObject = new JSONObject(responseBody.string());
                JSONArray data = jsonObject.getJSONArray("features");


                setAttractions(data);



            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }
    }
            );

    }

    void setAttractions(JSONArray data) throws JSONException {


        if(data != null && data.length()>0) {
            int i = 0, j = 0;
            while(i < 10) {
                j++;
                String id = data.getJSONObject(j).getJSONObject("properties").getString("xid");
                String name = data.getJSONObject(j).getJSONObject("properties").getString("name");
                if (!name.equals("")) {
                    i++;
                    String URL = "http://api.opentripmap.com/0.1/ru/places/xid/" + id + "?apikey=5ae2e3f221c38a28845f05b69e88ce800e00a6ce845d3a17b6c51ffa";

                    if (!TextUtils.isEmpty(id)) {

                        Log.d("MyTag", "id: " + id);
                        OkHttpClient client = new OkHttpClient();
                        Request request = new Request.Builder()
                                .url(URL)
                                .build();

                        client.newCall(request).enqueue(new Callback() {
                            @Override
                            public void onFailure(@NonNull Call call, @NonNull IOException e) {

                            }

                            @Override
                            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                                try (ResponseBody responseBody = response.body()) {

                                    if (!response.isSuccessful())
                                    {
                                        throw new IOException("Запрос к серверу не был успешен: " +
                                                response.code() + " " + response.message());
                                    }

                                    JSONObject resultJSON = new JSONObject(responseBody.string());
                                    Attraction attraction;
                                    if (resultJSON.has("preview")) {
                                        attraction = new Attraction(resultJSON.getJSONObject("preview").getString("source"), resultJSON.getString("name"));
                                    } else {
                                        attraction = new Attraction("null", resultJSON.getString("name"));
                                    }
                                    if (!attraction.getName_place().equals("")) {
                                        attractions.add(attraction);
                                        Log.d("MyTag", "name: " + attraction.getName_place() + "\nimage: " + attraction.getImageURL());
                                    }

                                } catch (JSONException e) {
                                    throw new RuntimeException(e);
                                }
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                                        recyclerView.setAdapter(adapter);
                                    }
                                });

                            }
                        });
                    }
                }

            }

        }

    }

    //заполняем понравившиеся
    @Override
    public void likeAttraction(int pos) {

        likeAttractions.add(attractions.get(pos));
        Log.d("MyTag", "добавлено "+attractions.get(pos).getName_place());
    }
}