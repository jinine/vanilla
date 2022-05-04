package com.bcit.fashion_app;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        FirebaseFirestore db;
        FirebaseAuth mAuth;
        FirebaseUser mUser;
        db = FirebaseFirestore.getInstance();
        mAuth= FirebaseAuth.getInstance();
        mUser=mAuth.getCurrentUser();

        RecyclerView recyclerView = view.findViewById(R.id.home_recycler);
        db.collection("outfit")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            ArrayList<Outfit> outfits = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                document.getData().get("userId").toString();

                                HashMap<String, String> shirt_hash = (HashMap<String, String>) document.getData().get("shirt");
                                HashMap<String, String> pants_hash =  (HashMap<String, String>) document.getData().get("pants");
                                HashMap<String, String> shoes_hash =  (HashMap<String, String>) document.getData().get("shoe");

                                Clothing shirt = new Clothing(shirt_hash.get("userId"), shirt_hash.get("name"),
                                        shirt_hash.get("size"), shirt_hash.get("colour"), shirt_hash.get("brand"),
                                        ItemType.SHIRT, (String) shirt_hash.get("clothingImageId"));
                                Clothing pants = new Clothing(pants_hash.get("userId"), pants_hash.get("name"),
                                        pants_hash.get("size"), pants_hash.get("colour"), pants_hash.get("brand"),
                                        ItemType.PANTS, (String)pants_hash.get("clothingImageId"));
                                Clothing shoes = new Clothing(shoes_hash.get("userId"), shoes_hash.get("name"),
                                        shoes_hash.get("size"), shoes_hash.get("colour"), shoes_hash.get("brand"),
                                        ItemType.SHOES, (String) shoes_hash.get("clothingImageId"));

                                outfits.add(new Outfit(document.getData().get("userId").toString(),
                                        shirt,
                                        pants,
                                        shoes
                                ));
                            }
                            HomeAdapter adapter = new HomeAdapter(outfits);
                            recyclerView.setAdapter(adapter);
                        } else {
                            Log.w("Debug", "Error getting documents.", task.getException());
                        }
                    }
                });
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));

    }

}