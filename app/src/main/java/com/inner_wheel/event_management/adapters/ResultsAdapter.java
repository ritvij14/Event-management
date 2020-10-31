package com.inner_wheel.event_management.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textview.MaterialTextView;
import com.inner_wheel.event_management.R;
import com.inner_wheel.event_management.api.models.AgeGroup;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ResultsAdapter extends RecyclerView.Adapter<ResultsAdapter.ViewHolder> {

    ArrayList<AgeGroup> ageGroups;
    Context context;

    public ResultsAdapter(ArrayList<AgeGroup> ageGroups, Context context) {
        this.ageGroups = ageGroups;
        this.context = context;
    }

    @NonNull
    @Override
    public ResultsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.result_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ResultsAdapter.ViewHolder holder, int position) {
        AgeGroup group = ageGroups.get(position);
        String ageLimit = "Ages " + group.getStartAge() + "-" + group.getEndAge() + ":";
        // Toast.makeText(context, ageLimit, Toast.LENGTH_SHORT).show();
        holder.ages.setText(ageLimit);
        if (group.getWinner() != null) {
            Picasso.get().load(group.getWinner().getFirst()).resize(500, 500).into(holder.firstPlaceWinner);
            Picasso.get().load(group.getWinner().getSecond()).resize(500, 500).into(holder.secondPlaceWinner);
            Picasso.get().load(group.getWinner().getThird()).resize(500, 500).into(holder.thirdPlaceWinner);
        }
        // Picasso.get().load(group.getWinner().getFirst()).into(holder.firstPlaceWinner);
    }

    @Override
    public int getItemCount() {
        Log.d("RESULTS ADAPTER", ""+ageGroups.size());
        return ageGroups.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        MaterialTextView ages;
        ImageView firstPlaceWinner, secondPlaceWinner, thirdPlaceWinner;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ages = itemView.findViewById(R.id.age_group);
            firstPlaceWinner = itemView.findViewById(R.id.first_prize_winner);
            secondPlaceWinner = itemView.findViewById(R.id.second_prize_winner);
            thirdPlaceWinner = itemView.findViewById(R.id.third_prize_winner);
        }
    }
}
