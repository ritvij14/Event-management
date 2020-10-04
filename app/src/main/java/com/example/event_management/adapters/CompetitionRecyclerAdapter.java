package com.example.event_management.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.event_management.R;
import com.example.event_management.models.CompetitionListItem;
import com.google.android.material.textview.MaterialTextView;

import java.util.ArrayList;

public class CompetitionRecyclerAdapter extends RecyclerView.Adapter<CompetitionRecyclerAdapter.ViewHolder> {

    ArrayList<CompetitionListItem> competitionList;
    Context context;

    public CompetitionRecyclerAdapter(ArrayList<CompetitionListItem> competitionList, Context context) {
        this.competitionList = competitionList;
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
    }

    @Override
    public int getItemCount() { return competitionList.size(); }

    class ViewHolder extends RecyclerView.ViewHolder {
        MaterialTextView compTitle, compDate, compCategory;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            compTitle = itemView.findViewById(R.id.competition_title);
            compDate = itemView.findViewById(R.id.competition_date);
            compCategory = itemView.findViewById(R.id.competition_category);
        }
    }
}
