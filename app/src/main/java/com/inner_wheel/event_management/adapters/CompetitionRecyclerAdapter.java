package com.inner_wheel.event_management.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.inner_wheel.event_management.R;
import com.inner_wheel.event_management.activities.CompetitionActivity;
import com.inner_wheel.event_management.models.CompetitionListItem;
import com.google.android.material.textview.MaterialTextView;

import java.util.ArrayList;

public class CompetitionRecyclerAdapter extends RecyclerView.Adapter<CompetitionRecyclerAdapter.ViewHolder> {

    ArrayList<CompetitionListItem> competitionList;
    private String compStatus;
    Context context;

    public CompetitionRecyclerAdapter(ArrayList<CompetitionListItem> competitionList, String compStatus, Context context) {
        this.competitionList = competitionList;
        this.compStatus = compStatus;
        this.context = context;
    }

    @NonNull
    @Override
    public CompetitionRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
            .inflate(R.layout.competition_list_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull CompetitionRecyclerAdapter.ViewHolder holder, int position) {
        CompetitionListItem competition = competitionList.get(position);
        holder.compTitle.setText(competition.getCompetitionName());
        holder.compDate.setText(competition.getDate());
        holder.compCategory.setText(competition.getCategory());
        holder.id = competition.getId();

        holder.itemView.setOnClickListener(v -> {
            Intent compIntent = new Intent(context, CompetitionActivity.class);
            Bundle b = new Bundle();
            b.putString("ID", holder.id);
            b.putString("STATUS", compStatus);
            Log.d("ADAPTER", holder.id);
            compIntent.putExtras(b);
            context.startActivity(compIntent);
        });
    }

    @Override
    public int getItemCount() { return competitionList.size(); }

    class ViewHolder extends RecyclerView.ViewHolder {
        MaterialTextView compTitle, compDate, compCategory;
        String id;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            compTitle = itemView.findViewById(R.id.competition_title);
            compDate = itemView.findViewById(R.id.competition_date);
            compCategory = itemView.findViewById(R.id.competition_category);
        }
    }
}
