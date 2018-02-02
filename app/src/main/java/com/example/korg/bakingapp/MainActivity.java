package com.example.korg.bakingapp;

import android.content.res.Configuration;
import android.os.PersistableBundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import static com.example.korg.bakingapp.RecipesAdapter.bakingTimeFragment;
import static com.example.korg.bakingapp.RecipesAdapter.recipeIngredientsCard;
import static com.example.korg.bakingapp.RecipesAdapter.recipeNameFragment;

public class MainActivity extends AppCompatActivity implements ActivityNotification {

    private boolean isTablet;
    private final String mLayoutVisibility = "MainLayoutVisibility";
    private final String fragmentTag = "RecipeStepInstructionFragment";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        isTablet = getResources().getBoolean(R.bool.isTablet);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        if (getSupportFragmentManager().findFragmentById(R.id.main_frame) == null && savedInstanceState == null) {
            RecipeNameFragment recFragment = RecipeNameFragment.newInstance();
            transaction.replace(R.id.main_frame, recFragment);
            transaction.commit();
            if (getSupportActionBar() != null) {
                getSupportActionBar().setTitle("Recipes");
                getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            }
            if (isTablet) {
                findViewById(R.id.main_layout).setVisibility(View.VISIBLE);
                findViewById(R.id.primary_layout).setVisibility(View.GONE);
                findViewById(R.id.secondary_layout).setVisibility(View.GONE);
            }
        }

        if (isTablet && savedInstanceState != null) {
            if (savedInstanceState.getInt(mLayoutVisibility) == View.GONE) {
                findViewById(R.id.main_layout).setVisibility(View.GONE);
                findViewById(R.id.primary_layout).setVisibility(View.VISIBLE);
                findViewById(R.id.secondary_layout).setVisibility(View.VISIBLE);
                Fragment f = getSupportFragmentManager().findFragmentByTag(fragmentTag);
                if (f != null) {
                    transaction.replace(R.id.secondary_layout, f);
                    transaction.commit();
                }
            } else {
                findViewById(R.id.main_layout).setVisibility(View.VISIBLE);
                findViewById(R.id.primary_layout).setVisibility(View.GONE);
                findViewById(R.id.secondary_layout).setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void notifyActivity(String tFragment, String tCard, int recipeId, int stepsId) {
        FragmentTransaction transaction;
        switch (tFragment) {
            case (bakingTimeFragment):
                if (getSupportActionBar() != null)
                    getSupportActionBar().setDisplayHomeAsUpEnabled(true);

                transaction = getSupportFragmentManager().beginTransaction();

                if (!isTablet) {
                    RecipeNameDetailsFragment rf = RecipeNameDetailsFragment.newInstance(recipeId);
                    transaction.replace(R.id.main_frame, rf);
                } else {
                    LinearLayout mainLayout = findViewById(R.id.main_layout);
                    LinearLayout primaryLayout = findViewById(R.id.primary_layout);
                    LinearLayout secondaryLayout = findViewById(R.id.secondary_layout);
                    mainLayout.setVisibility(View.GONE);
                    primaryLayout.setVisibility(View.VISIBLE);
                    secondaryLayout.setVisibility(View.VISIBLE);

                    transaction.remove(getSupportFragmentManager().findFragmentById(R.id.main_frame));
                    RecipeNameDetailsFragment rf = RecipeNameDetailsFragment.newInstance(recipeId);
                    transaction.replace(R.id.primary_frame, rf);
                    RecipeStepInstructionFragment ri = RecipeStepInstructionFragment.newInstance(recipeId, stepsId);
                    transaction.replace(R.id.secondary_frame, ri);
                }
                transaction.addToBackStack(null);
                transaction.commit();
                break;

            case (recipeNameFragment):
                if (getSupportActionBar() != null)
                    getSupportActionBar().setDisplayHomeAsUpEnabled(true);

                if (tCard.equals(recipeIngredientsCard)) {
                    RecipeIngredientsFragment fragment = RecipeIngredientsFragment.newInstance(recipeId);
                    transaction = getSupportFragmentManager().beginTransaction();
                    if (isTablet)
                        transaction.replace(R.id.secondary_frame, fragment);
                    else {
                        transaction.replace(R.id.main_frame, fragment);
                        transaction.addToBackStack(null);
                    }
                    transaction.commit();
                } else {
                    RecipeStepInstructionFragment fragment = RecipeStepInstructionFragment.newInstance(recipeId, stepsId);
                    transaction = getSupportFragmentManager().beginTransaction();
                    if (isTablet)
                        transaction.replace(R.id.secondary_frame, fragment, fragmentTag);
                    else {
                        transaction.replace(R.id.main_frame, fragment);
                        transaction.addToBackStack(null);
                    }
                    transaction.commit();
                }
                break;
            default:
                break;
        }
    }

    public void nextClicked(View v) {
        Fragment f = getSupportFragmentManager().findFragmentById(R.id.main_frame);
        if (f instanceof RecipeStepInstructionFragment)
            ((RecipeStepInstructionFragment) f).nextClicked();
    }

    public void previousClicked(View v) {
        Fragment f = getSupportFragmentManager().findFragmentById(R.id.main_frame);
        if (f instanceof RecipeStepInstructionFragment)
            ((RecipeStepInstructionFragment) f).previousClicked();
    }

    @Override
    public void updateActionBar(String title) {
        if (getSupportActionBar() != null)
            getSupportActionBar().setTitle(title);
    }


    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().findFragmentById(R.id.primary_frame) instanceof RecipeNameDetailsFragment || getSupportFragmentManager().findFragmentById(R.id.main_frame) instanceof RecipeNameDetailsFragment) {
            if (isTablet) {
                LinearLayout mainLayout = findViewById(R.id.main_layout);
                LinearLayout primaryLayout = findViewById(R.id.primary_layout);
                LinearLayout secondaryLayout = findViewById(R.id.secondary_layout);
                mainLayout.setVisibility(View.VISIBLE);
                primaryLayout.setVisibility(View.GONE);
                secondaryLayout.setVisibility(View.GONE);

            }
            if (getSupportActionBar() != null)
                getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        }
        if (getSupportFragmentManager().getBackStackEntryCount() > 0)
            getSupportFragmentManager().popBackStackImmediate();
        else
            super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        int lin = findViewById(R.id.main_layout).getVisibility();
        outState.putInt(mLayoutVisibility, lin);
        super.onSaveInstanceState(outState);
    }
}
