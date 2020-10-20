package com.inner_wheel.event_management.fragments.competition;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.inner_wheel.event_management.R;
import com.inner_wheel.event_management.databinding.FragmentSubmitEntryBinding;

import org.jetbrains.annotations.NotNull;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Objects;

public class SubmitEntryFragment extends Fragment {

    FragmentSubmitEntryBinding entryBinding;
    String name, school, age, topic;
    private final int GET_IMAGE_FOR_SUBMISSION = 100;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReferenceFromUrl("gs://event-management-6acc9.appspot.com");
    InputStream stream;
    public SubmitEntryFragment(String name, String school, String age, String topic) {
        this.name = name;
        this.age = age;
        this.school = school;
        this.topic = topic;
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        entryBinding = FragmentSubmitEntryBinding.inflate(inflater, container, false);

        // setting data
        entryBinding.registeredParticipantName.setText(name);
        entryBinding.registeredParticipantAge.setText(age);
        entryBinding.registeredParticipantSchool.setText(school);
        entryBinding.registeredParticipantTopic.setText(topic);

        // getting the image
        entryBinding.submitEntryButton.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            startActivityForResult(intent, GET_IMAGE_FOR_SUBMISSION);
        });

        // uploading image
        entryBinding.uploadSubmissionButton.setOnClickListener(v -> {
            StorageReference submissionRef = storageRef.child(topic + ".jpg");
            StorageMetadata metadata = new StorageMetadata.Builder()
                    .setContentType("image/jpg")
                    .build();
            UploadTask task = submissionRef.putStream(stream, metadata);

            task.addOnFailureListener(e -> {
                // unsuccessful upload
                Toast.makeText(getContext(), "Unable to upload, please try again.", Toast.LENGTH_SHORT).show();
            }).addOnSuccessListener(taskSnapshot -> Toast.makeText(
                    getContext(),
                    "Upload successful",
                    Toast.LENGTH_SHORT).show());
        });

        return entryBinding.getRoot();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GET_IMAGE_FOR_SUBMISSION && resultCode == Activity.RESULT_OK) {
            try {
                assert data != null;
                stream = Objects.requireNonNull(getContext())
                        .getContentResolver()
                        .openInputStream(Objects.requireNonNull(data.getData()));
                Toast.makeText(getContext(), "Entry received, please upload.", Toast.LENGTH_SHORT).show();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}