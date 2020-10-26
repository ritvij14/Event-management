package com.inner_wheel.event_management.adapters;

import android.content.Context;
import android.graphics.Bitmap;
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
import com.inner_wheel.event_management.utils.URLtoBitmap;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

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
        Toast.makeText(context, ageLimit, Toast.LENGTH_SHORT).show();
        holder.ages.setText(ageLimit);
        if (group.getWinner() != null) {
            Picasso.get().load(group.getWinner().getFirst()).resize(50, 50).into(holder.firstPlaceWinner);
        }
        // Picasso.get().load(group.getWinner().getFirst()).into(holder.firstPlaceWinner);
    }

    @Override
    public int getItemCount() {
        Log.d("RESULTS ADAPTER", ""+ageGroups.size());
        return ageGroups.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        MaterialTextView ages, secondPlaceName, secondPlacePrize, thirdPlaceName, thirdPlacePrize;
        ImageView firstPlaceWinner;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ages = itemView.findViewById(R.id.age_group);
            firstPlaceWinner = itemView.findViewById(R.id.first_prize_winner);
            secondPlaceName = itemView.findViewById(R.id.second_place_winner);
            secondPlacePrize = itemView.findViewById(R.id.second_place_prize);
            thirdPlaceName = itemView.findViewById(R.id.third_place_winner);
            thirdPlacePrize = itemView.findViewById(R.id.third_place_prize);
        }
    }
}
