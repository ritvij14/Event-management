package com.example.event_management.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.event_management.R;
import com.example.event_management.models.AddChildListItem;
import com.google.android.material.textview.MaterialTextView;

import java.util.ArrayList;

public class AddChildRecyclerAdapter extends RecyclerView.Adapter<AddChildRecyclerAdapter.ViewHolder> {

    ArrayList<AddChildListItem> childList;
    Context context;

    public AddChildRecyclerAdapter(ArrayList<AddChildListItem> childList, Context context) {
        this.childList = childList;
        this.context = context;
    }

    @NonNull
    @Override
    public AddChildRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
            .inflate(R.layout.child_list_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull AddChildRecyclerAdapter.ViewHolder holder, int position) {
        AddChildListItem child = childList.get(position);
        holder.childName.setText(child.getName());
        holder.childAge.setText(child.getAge());
        holder.childSchool.setText(child.getSchool());
    }

    @Override
    public int getItemCount() {
        return childList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        MaterialTextView childName, childAge, childSchool;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            childName = itemView.findViewById(R.id.child_name);
            childAge = itemView.findViewById(R.id.child_age);
            childSchool = itemView.findViewById(R.id.child_school);
        }
    }
}
