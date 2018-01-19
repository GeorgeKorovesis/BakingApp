package com.example.korg.bakingapp;

        import retrofit2.Call;
        import retrofit2.http.GET;

/**
 * Created by korg on 14/12/2017.
 */

public interface RetrofitInterface {

    @GET("topher/2017/May/59121517_baking/baking.json")
    Call<RecipeModel[]> getData();
}
