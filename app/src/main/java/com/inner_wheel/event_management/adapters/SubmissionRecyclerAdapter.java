package com.inner_wheel.event_management.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textview.MaterialTextView;
import com.inner_wheel.event_management.R;
import com.inner_wheel.event_management.api.models.AgeGroup;
import com.inner_wheel.event_management.fragments.competition.CompetitionPaymentFragment;
import com.inner_wheel.event_management.models.AddChildListItem;
import com.inner_wheel.event_management.models.RegisteredListItem;

import java.util.ArrayList;

public class SubmissionRecyclerAdapter extends RecyclerView.Adapter<SubmissionRecyclerAdapter.ViewHolder> {

    ArrayList<RegisteredListItem> list;
    Context context;

    public SubmissionRecyclerAdapter(ArrayList<RegisteredListItem> list, Context context) {
        this.list = list;
        this.context = context;
    }


    @NonNull
    @Override
    public SubmissionRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.saved_participant_list_item, parent, false);
        return new SubmissionRecyclerAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull SubmissionRecyclerAdapter.ViewHolder holder, int position) {
        RegisteredListItem item = list.get(position);
        holder.name.setText(item.getName());
        holder.itemView.setOnClickListener(v -> {
            if (!item.isSubmitted()) {
                /*((FragmentActivity)context)
                        .getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.main_competitions_frame, new SubmissionFragment(
                                child.getName(),
                                child.getSchool(),
                                child.getAge(),
                                ageGroup
                        ))
                        .commit();*/
            } else {
                Toast.makeText(context, "Already submitted! You cannot make more than one submission.", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public int getItemCount() { return list.size(); }

    class ViewHolder extends RecyclerView.ViewHolder {
        MaterialTextView name;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.participant_name);
        }
    }
}
