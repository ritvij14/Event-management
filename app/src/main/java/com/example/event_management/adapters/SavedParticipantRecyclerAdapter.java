package com.example.event_management.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.event_management.R;
import com.example.event_management.fragments.competition.CompetitionPaymentFragment;
import com.example.event_management.models.AddChildListItem;
import com.google.android.material.textview.MaterialTextView;

import java.util.ArrayList;

public class SavedParticipantRecyclerAdapter extends RecyclerView.Adapter<SavedParticipantRecyclerAdapter.ViewHolder> {

    ArrayList<AddChildListItem> childList;
    Context context;

    public SavedParticipantRecyclerAdapter(ArrayList<AddChildListItem> childList, Context context) {
        this.childList = childList;
        this.context = context;
    }

    @NonNull
    @Override
    public SavedParticipantRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.saved_participant_list_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull SavedParticipantRecyclerAdapter.ViewHolder holder, int position) {
        AddChildListItem child = childList.get(position);
        holder.name.setText(child.getName());
        holder.itemView.setOnClickListener(v -> {
            ((FragmentActivity)context).getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.main_competitions_frame, new CompetitionPaymentFragment(
                            child.getName(),
                            child.getSchool(),
                            child.getAge()
                    ))
                    .commit();
        });
    }

    @Override
    public int getItemCount() { return childList.size(); }

    class ViewHolder extends RecyclerView.ViewHolder {
        MaterialTextView name;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.participant_name);
        }
    }
}
