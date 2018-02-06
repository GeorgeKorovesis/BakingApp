package com.example.korg.bakingapp;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import static android.view.View.VISIBLE;
import static com.example.korg.bakingapp.RecipesAdapter.bakingTimeFragment;
import static com.example.korg.bakingapp.RecipesAdapter.recipeIngredientsCard;
import static com.example.korg.bakingapp.RecipesAdapter.recipeNameFragment;

public class MainActivity extends AppCompatActivity implements ActivityNotification {

    private boolean isTablet;
    private LinearLayout mainLayout;
    private LinearLayout primaryLayout;
    private LinearLayout secondaryLayout;
    private final String OrientationChanged = "OrientationChanged";
    private final String CurrentFragment = "CurrentFragment";
    private final String ActionBarTitle = "ActionBarTitle";
    private boolean orientationChanged;
    private String currentFragment;
    private String actionBarTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        isTablet = getResources().getBoolean(R.bool.isTablet);
        mainLayout = findViewById(R.id.main_layout);
        primaryLayout = findViewById(R.id.primary_layout);
        secondaryLayout = findViewById(R.id.secondary_layout);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        if (savedInstanceState != null) {
            orientationChanged = savedInstanceState.getBoolean(OrientationChanged);
            currentFragment = savedInstanceState.getString(CurrentFragment);
            actionBarTitle = savedInstanceState.getString(ActionBarTitle);
        }
        /*only for first time*/
        if (currentFragment == null) {
            RecipeNameFragment recFragment = RecipeNameFragment.newInstance();
            transaction.replace(R.id.main_frame, recFragment);
            transaction.commit();
            if (getSupportActionBar() != null) {
                getSupportActionBar().setTitle("Recipes");
                getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            }
            if (isTablet) {
                primaryLayout.setVisibility(View.GONE);
                secondaryLayout.setVisibility(View.GONE);
            }
        } else if (isTablet && (currentFragment.contains("RecipeStepInstructionFragment") || currentFragment.contains("RecipeIngredientsFragment")) && orientationChanged) {
            mainLayout.setVisibility(View.GONE);
            orientationChanged = false;
            if (getSupportActionBar() != null) {
                getSupportActionBar().setTitle(actionBarTitle);
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            }
        }
    }

    @Override
    public void notifyActivity(String tFragment, String tCard, int recipeId, int stepsId) {
        FragmentTransaction transaction;
        /*check which fragment called notifyActivity and perform some actions*/
        switch (tFragment) {
            case (bakingTimeFragment):
                if (getSupportActionBar() != null)
                    getSupportActionBar().setDisplayHomeAsUpEnabled(true);

                transaction = getSupportFragmentManager().beginTransaction();

                if (!isTablet) {
                    RecipeNameDetailsFragment rf = RecipeNameDetailsFragment.newInstance(recipeId);
                    transaction.replace(R.id.main_frame, rf);
                    currentFragment = rf.toString();
                } else {
                    transaction.remove(getSupportFragmentManager().findFragmentById(R.id.main_frame));
                    RecipeNameDetailsFragment rf = RecipeNameDetailsFragment.newInstance(recipeId);
                    RecipeStepInstructionFragment ri = RecipeStepInstructionFragment.newInstance(recipeId, stepsId);
                    secondaryLayout.setVisibility(VISIBLE);
                    primaryLayout.setVisibility(VISIBLE);
                    mainLayout.setVisibility(View.GONE);
                    transaction.replace(R.id.primary_frame, rf);
                    transaction.replace(R.id.secondary_frame, ri);

                    currentFragment = ri.toString();
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
                    currentFragment = fragment.toString();
                } else {
                    RecipeStepInstructionFragment fragment = RecipeStepInstructionFragment.newInstance(recipeId, stepsId);
                    transaction = getSupportFragmentManager().beginTransaction();
                    if (isTablet) {
                        transaction.remove(getSupportFragmentManager().findFragmentById(R.id.secondary_frame));
                        transaction.replace(R.id.secondary_frame, fragment);
                    } else {
                        transaction.replace(R.id.main_frame, fragment);
                        transaction.addToBackStack(null);
                    }
                    transaction.commit();
                    currentFragment = fragment.toString();
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
        Fragment primaryFrame=getSupportFragmentManager().findFragmentById(R.id.primary_frame);
        Fragment mainFrame=getSupportFragmentManager().findFragmentById(R.id.main_frame);
        Fragment secondaryFrame=getSupportFragmentManager().findFragmentById(R.id.secondary_frame);

        if (primaryFrame instanceof RecipeNameDetailsFragment || mainFrame instanceof RecipeNameDetailsFragment) {
            if (isTablet) {
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                mainLayout.setVisibility(VISIBLE);
                primaryLayout.setVisibility(View.GONE);
                secondaryLayout.setVisibility(View.GONE);
                transaction.remove(primaryFrame);
                transaction.remove(secondaryFrame);
                transaction.commit();
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
        outState.putBoolean(OrientationChanged, true);
        outState.putString(CurrentFragment, currentFragment);
        String title = "";
        if (getSupportActionBar() != null)
            title = getSupportActionBar().getTitle().toString();
        outState.putString(ActionBarTitle, title);
        super.onSaveInstanceState(outState);
    }


}
