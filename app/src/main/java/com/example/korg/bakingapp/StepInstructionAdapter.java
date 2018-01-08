package com.example.korg.bakingapp;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by korg on 4/1/2018.
 */

public class StepInstructionAdapter extends RecyclerView.Adapter<StepInstructionAdapter.InstructionViewHolder> {

    @Override
    public StepInstructionAdapter.InstructionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(StepInstructionAdapter.InstructionViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    class InstructionViewHolder extends RecyclerView.ViewHolder {
        TextView recipe;

        InstructionViewHolder(View itemView) {
            super(itemView);
            recipe = itemView.findViewById(R.id.recipe);
        }
    }
}
