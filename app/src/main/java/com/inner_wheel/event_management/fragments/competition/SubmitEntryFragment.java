package com.inner_wheel.event_management.fragments.competition;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.inner_wheel.event_management.R;
import com.inner_wheel.event_management.api.RetrofitClient;
import com.inner_wheel.event_management.api.models.RegistrationResponse;
import com.inner_wheel.event_management.api.models.SubmissionResponse;
import com.inner_wheel.event_management.databinding.FragmentSubmitEntryBinding;
import com.inner_wheel.event_management.utils.SharedPrefs;

import org.jetbrains.annotations.NotNull;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SubmitEntryFragment extends Fragment {

    FragmentSubmitEntryBinding entryBinding;
    String id, name, school, age, topic, groupID;
    private final int GET_IMAGE_FOR_SUBMISSION = 100;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReferenceFromUrl("gs://event-management-6acc9.appspot.com");
    InputStream stream;
    SharedPrefs sharedPrefs;
    public SubmitEntryFragment(String id, String name, String school, String age, String topic, String groupID) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.school = school;
        this.topic = topic;
        this.groupID = groupID;
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        entryBinding = FragmentSubmitEntryBinding.inflate(inflater, container, false);
        sharedPrefs = new SharedPrefs(Objects.requireNonNull(getContext()));

        entryBinding.backButton.setOnClickListener(v -> Objects.requireNonNull(getActivity())
                .getSupportFragmentManager().beginTransaction()
                .replace(R.id.main_competitions_frame, new CompetitionSubmissionFragment())
                .commit());


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
            StorageReference submissionRef = storageRef.child(topic + "_" + name + ".jpg");
            StorageMetadata metadata = new StorageMetadata.Builder()
                    .setContentType("image/jpg")
                    .build();
            UploadTask uploadTask = submissionRef.putStream(stream, metadata);

            uploadTask.addOnFailureListener(e -> {
                // unsuccessful upload
                Toast.makeText(getContext(), "Unable to upload, please try again.", Toast.LENGTH_SHORT).show();
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    if (taskSnapshot.getMetadata() != null) {
                        if (taskSnapshot.getMetadata().getReference() != null) {
                            Task<Uri> result = taskSnapshot.getStorage().getDownloadUrl();
                            result.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String imageUrl = uri.toString();
                                    Log.d("SUBMISSION_URL", imageUrl);
                                    addEntry(imageUrl);
                                    //createNewPost(imageUrl);
                                }
                            });
                        }
                    }
                }
            });
        });

        return entryBinding.getRoot();
    }

    private void addEntry(String imageUrl) {
        Call<SubmissionResponse> call = RetrofitClient.getClient().addSubmission(sharedPrefs.getToken(), groupID, id, imageUrl);
        call.enqueue(new Callback<SubmissionResponse>() {
            @Override
            public void onResponse(Call<SubmissionResponse> call, Response<SubmissionResponse> response) {
                SubmissionResponse res = response.body();
                assert res != null;
                if (res.getSuccess()) {
                    Toast.makeText(getContext(), "Upload successful", Toast.LENGTH_SHORT).show();
                    Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction()
                            .replace(R.id.main_competitions_frame, new CompetitionSubmissionFragment())
                            .commit();
                }
            }

            @Override
            public void onFailure(Call<SubmissionResponse> call, Throwable t) {

            }
        });
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