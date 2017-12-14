package com.example.korg.bakingapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {
private static boolean fetched;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(!fetched){
        RetrofitController controller = new RetrofitController();
        controller.start();
        fetched=true;
        }
    }
}