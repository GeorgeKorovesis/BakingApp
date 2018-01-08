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

    @Override
    public void notifyActivity(String tFragment, String tCard, int recipeId, int stepsId) {
        FragmentTransaction transaction;
        switch (tFragment) {
            case (bakingTimeFragment):
                RecipeNameFragment rfragment = RecipeNameFragment.newInstance(recipeId);
                transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.main_frame, rfragment);
                transaction.addToBackStack(null);
                transaction.commit();
                break;
            case (recipeNameFragment):
                if (tCard.equals(recipeIngredientsCard)) {
                    RecipeIngredientsFragment bFragment = RecipeIngredientsFragment.newInstance(recipeId);
                    transaction = getFragmentManager().beginTransaction();
                    transaction.replace(R.id.main_frame, bFragment);
                    transaction.addToBackStack(null);
                    transaction.commit();
                }
                else {
                    RecipeStepInstructionFragment bFragment = RecipeStepInstructionFragment.newInstance(recipeId, stepsId);
                    transaction = getFragmentManager().beginTransaction();
                    transaction.replace(R.id.main_frame, bFragment);
                    transaction.addToBackStack(null);
                    transaction.commit();
                }

                break;
            default:
                break;
        }
    }

}