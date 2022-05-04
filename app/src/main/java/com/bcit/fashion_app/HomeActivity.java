package com.bcit.fashion_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class HomeActivity extends AppCompatActivity {

    FirebaseFirestore db;
    FirebaseAuth mAuth;
    FirebaseUser mUser;
    ArrayList<Clothing> clothing_object_list;
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        db = FirebaseFirestore.getInstance();

        mAuth=FirebaseAuth.getInstance();
        mUser=mAuth.getCurrentUser();

        System.out.println(mUser.getEmail());
        System.out.println(mUser.getUid());

        clothing_object_list = new ArrayList<>();

        db.collection("clothing")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            ArrayList<Clothing> clothing2 = new ArrayList<Clothing>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                //Log.d("Debug", document.getData().toString());
                                if (document.getData().get("userId").toString().equals(mUser.getUid().toString())) {
                                    clothing2.add(new Clothing(document.getData().get("userId").toString(),
                                            document.getData().get("name").toString(),
                                            document.getData().get("size").toString(),
                                            document.getData().get("colour").toString(),
                                            document.getData().get("brand").toString(),
                                            ItemType.valueOf(document.getData().get("item").toString()),
                                            document.getData().get("clothingImageId").toString()
                                    ));
                                }
                            }
                            Fragment homeFragment = HomeFragment.newInstance("yolo", "yolo2");
//                            Fragment homeFragment = HomeFragment.newInstance("yolo", "yolo2");
                            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                            fragmentTransaction.replace(R.id.fragmentContainerView, homeFragment);
                            fragmentTransaction.commit();

                            BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
                            bottomNavigationView.setOnNavigationItemSelectedListener(new
                                 BottomNavigationView.OnNavigationItemSelectedListener() {
                                     @Override
                                     public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                                         Fragment selectedFragment = null;

                                         switch (item.getItemId()){
                                             case R.id.nav_wardrobe:
                                                 db.collection("clothing")
                                                         .get()
                                                         .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                             @Override
                                                             public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                                 if (task.isSuccessful()) {
                                                                     ArrayList<Clothing> clothing2 = new ArrayList<Clothing>();
                                                                     for (QueryDocumentSnapshot document : task.getResult()) {
                                                                         //Log.d("Debug", document.getData().toString());
                                                                         if (document.getData().get("userId").toString().equals(mUser.getUid().toString())) {
                                                                             clothing2.add(new Clothing(document.getData().get("userId").toString(),
                                                                                     document.getData().get("name").toString(),
                                                                                     document.getData().get("size").toString(),
                                                                                     document.getData().get("colour").toString(),
                                                                                     document.getData().get("brand").toString(),
                                                                                     ItemType.valueOf(document.getData().get("item").toString()),
                                                                                     document.getData().get("clothingImageId").toString()
                                                                             ));
                                                                         }
                                                                     }
                                                                     Fragment wardrobeFragment = WardrobeFragment.newInstance(clothing2);
                                                                     FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                                                                     fragmentTransaction.replace(R.id.fragmentContainerView, wardrobeFragment);
                                                                     fragmentTransaction.commit();
                                                                 } else {
                                                                     Log.w("Debug", "Error getting documents.", task.getException());
                                                                 }
                                                             }
                                                         });
                                                 return true;
                                             case R.id.nav_home:
                                                 selectedFragment = HomeFragment.newInstance("yolo", "yolo2");
                                                 FragmentTransaction transaction2 = getSupportFragmentManager().beginTransaction();
                                                 transaction2.replace(R.id.fragmentContainerView,selectedFragment);
                                                 transaction2.addToBackStack(null);
                                                 transaction2.commit();
                                                 return true;
                                             case R.id.nav_outfits:
                                                 selectedFragment = OutfitDisplayFragment.newInstance("yolo", "yolo2");
                                                 FragmentTransaction transaction3 = getSupportFragmentManager().beginTransaction();
                                                 transaction3.replace(R.id.fragmentContainerView,selectedFragment);
                                                 transaction3.addToBackStack(null);
                                                 transaction3.commit();
                                                 return true;
                                             case R.id.nav_profile:
                                                 selectedFragment = ProfileFragment.newInstance("yolo");
                                                 FragmentTransaction transactionProfile = getSupportFragmentManager().beginTransaction();
                                                 transactionProfile.replace(R.id.fragmentContainerView,selectedFragment);
                                                 transactionProfile.addToBackStack(null);
                                                 transactionProfile.commit();
                                                 return true;

                                         }
                                         FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                                         transaction.replace(R.id.fragmentContainerView, selectedFragment);
                                         transaction.commit();
                                         return true;
                                     }
                                 });

                        } else {
                            Log.w("Debug", "Error getting documents.", task.getException());
                        }


                    }
                });
    }

    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }
}