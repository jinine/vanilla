package com.bcit.fashion_app;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class ClothingInfoFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";

    private Clothing mParam1;

    public ClothingInfoFragment() {
        // Required empty public constructor
    }
    public static ClothingInfoFragment newInstance(Clothing param1) {
        ClothingInfoFragment fragment = new ClothingInfoFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = (Clothing) getArguments().getSerializable(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_clothing_info, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseFirestore db;
        FirebaseAuth mAuth;
        FirebaseUser mUser;
        TextView name = view.findViewById(R.id.name_textview_clothinginfo);
        TextView size = view.findViewById(R.id.size_textview_clothinginfo);
        TextView colour = view.findViewById(R.id.colour_textview_clothinginfo);
        TextView brand = view.findViewById(R.id.brand_textview_clothinginfo);
        TextView item_type = view.findViewById(R.id.itemtype_textview_clothinginfo);
        ImageView imageView = view.findViewById(R.id.imageView_info_confirmation);
        StorageReference mStorageReference;
        db = FirebaseFirestore.getInstance();
        mAuth= FirebaseAuth.getInstance();
        mUser=mAuth.getCurrentUser();
        System.out.println("name of the value: "+ mParam1.getName());
        try{
            if(mParam1 != null){
                name.setText(mParam1.getName());
                size.setText(mParam1.getSize());
                colour.setText(mParam1.getColour());
                brand.setText(mParam1.getBrand());
                item_type.setText(mParam1.getItem().toString());
                mStorageReference = FirebaseStorage.getInstance().getReference().child("clothing/"
                        + mParam1.getClothingImageId());
                System.out.println("the full string is " + mParam1.getClothingImageId());
                try {
                    final File localFile = File.createTempFile(mParam1.getClothingImageId(),"jpeg");
                    mStorageReference.getFile(localFile)
                            .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                    Log.d("DEBUG", "image retrieved & loaded");
                                    Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                                    imageView.setImageBitmap(bitmap);
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d("DEBUG", "image not loading");
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch(NullPointerException e){
            System.out.println("Null Pointer Exception");
        }
    }
}