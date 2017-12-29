package com.example.korg.bakingapp;

import android.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecipesFragment recFragment = new RecipesFragment();

        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.add(R.id.main_frame, recFragment);
        transaction.commit();
    }
}