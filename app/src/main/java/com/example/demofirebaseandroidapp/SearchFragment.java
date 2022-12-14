package com.example.demofirebaseandroidapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class SearchFragment extends Fragment {

    private RecyclerView recyclerView;

    private ProfileAdapter profileAdapter;

    private ArrayList<Profile> profiles;

    private FirebaseAuth auth;
    private FirebaseFirestore db;
    private ProgressDialog progress;
    private TextInputEditText searchField;
    private MaterialButton btnSearch;
    private AutoCompleteTextView autoCompleteTextView, autoCompleteTextViewInterests;
    private ArrayAdapter<String> arrayAdapter, interestsAdapter;

    private String[] items = {"Gujranwala", "Lahore", "Pindi", "Islamabad", "Karachi", "Sialkot", "Daska"};

    private String [] interestsItems = { "Outdoor Activities", "Netflix", "Social Media", "Lofi", "Flutter",
            "Web Development", "Ludo", "Library", "Instruments", "Music", "TikTok", "Instagram",
            "Mobile Development", "Fitness", "Bollywood", "Chris Prat", "DC World Action"};;


    private String locationSelected;
    private String interestSelected;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        progress = new ProgressDialog(view.getContext());
        progress.setCancelable(false);
        progress.setMessage("Fetching Posts...");


        searchField = view.findViewById(R.id.search);
        recyclerView = view.findViewById(R.id.profilesRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));

        profiles = new ArrayList<Profile>();
        profileAdapter = new ProfileAdapter(profiles);

        recyclerView.setAdapter(profileAdapter);


        db = FirebaseFirestore.getInstance();

        auth = FirebaseAuth.getInstance();
        //searchField = view.findViewById(R.id.searchField);
        btnSearch = view.findViewById(R.id.btnSearch);

        autoCompleteTextView = view.findViewById(R.id.autoCompleteTextView);
        arrayAdapter = new ArrayAdapter<String>(getContext(), R.layout.dropdown_item, items);
        autoCompleteTextView.setAdapter(arrayAdapter);

        autoCompleteTextViewInterests = view.findViewById(R.id.autoCompleteTextViewInterest);
        interestsAdapter = new ArrayAdapter<String>(getContext(), R.layout.dropdown_item, interestsItems);
        autoCompleteTextViewInterests.setAdapter(interestsAdapter);


        getAllProfiles();

        autoCompleteTextViewInterests.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String item = adapterView.getItemAtPosition(i).toString();
                interestSelected = item.toString();
                Toast.makeText(getContext(), interestSelected, Toast.LENGTH_SHORT).show();
            }
        });


        //Handle drop down click
        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String item = adapterView.getItemAtPosition(i).toString();
                locationSelected = item.toString();
                Toast.makeText(getContext(), locationSelected, Toast.LENGTH_SHORT).show();
            }
        });

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {




                if(interestSelected == null || locationSelected == null){
                    Snackbar.make( getActivity().findViewById(android.R.id.content), "Input all the fields!", Snackbar.LENGTH_LONG).setBackgroundTint(getResources().getColor(R.color.red))
                            .show();

                    return;
                }


                clear();

                interestSelected = interestSelected.substring(0, 1).toUpperCase() + interestSelected.substring(1);

                progress.show();
                ProfileChangeListener(interestSelected, locationSelected);
            }
        });

    }

    private void ProfileChangeListener(String query, String loc) {
        db.collection("profiles").whereEqualTo("location", loc).whereArrayContains("interests", query).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {


                if(!task.isSuccessful()){
                    progress.dismiss();
                    Toast.makeText(getView().getContext(), task.getException().toString(), Toast.LENGTH_SHORT).show();
                }else {

                    for(QueryDocumentSnapshot document: task.getResult()){

                        //Toast.makeText(getView().getContext(), document.toObject(Profile.class).toString(), Toast.LENGTH_SHORT).show();
                        profiles.add(document.toObject(Profile.class));
                        profileAdapter.notifyDataSetChanged();
                        progress.dismiss();
                    }
                }
            }
        });


    }


    private void getAllProfiles(){
        db.collection("profiles").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {


                if(!task.isSuccessful()){
                    progress.dismiss();
                    Toast.makeText(getView().getContext(), task.getException().toString(), Toast.LENGTH_SHORT).show();
                }else {

                    for(QueryDocumentSnapshot document: task.getResult()){

                        //Toast.makeText(getView().getContext(), document.toObject(Profile.class).toString(), Toast.LENGTH_SHORT).show();
                        profiles.add(document.toObject(Profile.class));
                        profileAdapter.notifyDataSetChanged();
                        progress.dismiss();
                    }
                }
            }
        });
    }






    public void clear() {
        int size = profiles.size();
        if (size > 0) {
            for (int i = 0; i < size; i++) {
                profiles.remove(0);
            }

            profileAdapter.notifyItemRangeRemoved(0, size);
        }
    }
}




;