package com.example.korg.bakingapp;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by korg on 14/12/2017.
 */

public class RetrofitController implements Callback<BakingModel[]> {

    private static final String BASE_URL = "https://d17h27t6h515a5.cloudfront.net/";


    public void start() {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        RetrofitInterface serviceAPI = retrofit.create(RetrofitInterface.class);

        Call<BakingModel[]> call = serviceAPI.getData();
        call.enqueue(this);

    }


    @Override
    public void onResponse(Call<BakingModel[]> call, Response<BakingModel[]> response) {
        System.out.println("###Response");
        if (response.isSuccessful()) {
            BakingModel[] data = response.body();
            System.out.println("data=" + data[0].getName());
        } else {
            System.out.println(response.errorBody());
        }
    }

    @Override
    public void onFailure(Call<BakingModel[]> call, Throwable t) {
        System.out.println("fail=" + t.getMessage());
    }
}