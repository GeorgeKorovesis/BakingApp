package com.example.korg.bakingapp;

import android.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import static com.example.korg.bakingapp.RecipesAdapter.bakingTimeFragment;
import static com.example.korg.bakingapp.RecipesAdapter.recipeIngredientsCard;
import static com.example.korg.bakingapp.RecipesAdapter.recipeNameFragment;
import static com.example.korg.bakingapp.RecipesAdapter.recipeStepDescriptionCard;


public class MainActivity extends AppCompatActivity implements ActivityNotification {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BakingTimeFragment recFragment = new BakingTimeFragment();

        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.add(R.id.main_frame, recFragment);
        transaction.commit();
    }
//    void notifyActivity(String recipeNameFragment, String recipeIngredientsCard, int id);

    @Override
    public void notifyActivity(String tFragment, String tCard, int id) {
        FragmentTransaction transaction;
        switch (tFragment) {
            case (bakingTimeFragment):
                System.out.println("@@@@go to create recipe fragment");
                RecipeNameFragment rfragment = RecipeNameFragment.newInstance(id);
                transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.main_frame, rfragment);
                transaction.addToBackStack(null);
                transaction.commit();
                break;
            case (recipeNameFragment):
                SampleFragment bFragment;
                if (tCard.equals(recipeIngredientsCard))
                    bFragment = SampleFragment.newInstance(recipeNameFragment, recipeIngredientsCard);
                else {
                    bFragment = SampleFragment.newInstance(recipeNameFragment, recipeStepDescriptionCard);
                }
                transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.main_frame, bFragment);
                transaction.addToBackStack(null);
                transaction.commit();
                break;
            default:
                break;
        }
    }

}