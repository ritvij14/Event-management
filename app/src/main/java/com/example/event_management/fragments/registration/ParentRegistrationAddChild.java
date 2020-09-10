package com.example.event_management.fragments.registration;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.event_management.R;
import com.example.event_management.adapters.AddChildRecyclerAdapter;
import com.example.event_management.databinding.FragmentParentRegistrationAddChildBinding;
import com.example.event_management.models.AddChildListItem;

import java.util.ArrayList;
import java.util.Objects;

public class ParentRegistrationAddChild extends Fragment {

    FragmentParentRegistrationAddChildBinding childBinding;
    AddChildRecyclerAdapter childRecyclerAdapter;
    ArrayList<AddChildListItem> childListItems;
    public ParentRegistrationAddChild() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        childBinding = FragmentParentRegistrationAddChildBinding
            .inflate(inflater, container, false);
        childListItems = new ArrayList<AddChildListItem>();
        childBinding.childList.setHasFixedSize(true);
        childBinding.childList.setLayoutManager(new LinearLayoutManager(getContext(),
            RecyclerView.VERTICAL, false));
        AddChildListItem child = new AddChildListItem("Nipun Aggarwal", "12", "BITS Public School");
        childListItems.add(child);
        childListItems.add(child);
        childListItems.add(child);
        childRecyclerAdapter = new AddChildRecyclerAdapter(childListItems, getContext());
        childBinding.childList.setAdapter(childRecyclerAdapter);
        childRecyclerAdapter.notifyDataSetChanged();

        childBinding.addChildButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                childBinding.addChildInfoCard.setVisibility(View.VISIBLE);
            }
        });

        childBinding.addChildInfoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Objects.requireNonNull(childBinding.childNameField.getText())
                    .toString().isEmpty()) {
                    Toast.makeText(getContext(), "Please enter child details",
                        Toast.LENGTH_SHORT).show();
                }
                childListItems.add(
                    new AddChildListItem(
                        childBinding.childNameField.getText().toString(),
                        childBinding.childAgeField.getText().toString(),
                        childBinding.childSchoolField.getText().toString()
                    )
                );
                childBinding.addChildInfoCard.setVisibility(View.GONE);
            }
        });
        return childBinding.getRoot();
    }
}