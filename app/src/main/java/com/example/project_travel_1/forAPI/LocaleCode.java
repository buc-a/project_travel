package com.example.project_travel_1.forAPI;

import com.example.project_travel_1.objects.TwoPoints;

import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface LocaleCode {
    @POST("widgets_suggest_params?")

    Call<TwoPoints> getIataCode(@Query("q") String q);
}
