package com.bcit.fashion_app;

import static com.bcit.fashion_app.HomeActivity.verifyStoragePermissions;

import android.Manifest;
import android.app.ProgressDialog;
import android.app.usage.NetworkStats;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NewClothingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NewClothingFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";

    private String mParam1;
    Uri imageUri;
    StorageReference storageReference;
    ProgressDialog progressDialog;
    FirebaseFirestore db;
    FirebaseAuth mAuth;
    FirebaseUser mUser;
    EditText name;
    ItemType item;
    ImageView imageViewSelectedImage;

    public NewClothingFragment() {
    }

    public static NewClothingFragment newInstance(String param1) {
        NewClothingFragment fragment = new NewClothingFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_new_clothing, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.fragment_new_clothing);

        name = view.findViewById(R.id.name_input);
        EditText size = view.findViewById(R.id.size_input);
        EditText colour = view.findViewById(R.id.colour_input);
        EditText brand = view.findViewById(R.id.brand_input);
        Button button = view.findViewById(R.id.add_new_button);
        Button buttonSelectImage = view.findViewById(R.id.select_image_button);
        RadioGroup radioGroup = (RadioGroup) view.findViewById(R.id.radioGroup_new_clothing);
        imageViewSelectedImage = getActivity().findViewById(R.id.imageView_upload_image);
        TextView clothing_requirements = view.findViewById(R.id.textView_new_clothing_requirements);

        

        db = FirebaseFirestore.getInstance();

        mAuth= FirebaseAuth.getInstance();
        mUser=mAuth.getCurrentUser();

        buttonSelectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                verifyStoragePermissions(getActivity());
                selectImage(view);
            }
        });


        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if(i == R.id.radioButton_new_clothing_shirt) {
                    item = ItemType.SHIRT;
                } else if(i == R.id.radioButton_new_clothing_pants) {
                    item = ItemType.PANTS;
                } else if(i == R.id.radioButton_new_clothing_shoes){
                    item = ItemType.SHOES;
                }
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (name.getText().toString().trim().equals("")) {
                    clothing_requirements.setText(R.string.nameRequirement);
                } else if (size.getText().toString().trim().equals("")) {
                    clothing_requirements.setText("Please input size.");
                } else if (colour.getText().toString().trim().equals("")) {
                    clothing_requirements.setText("Please input colour.");
                } else if (brand.getText().toString().trim().equals("")) {
                    clothing_requirements.setText("Please input brand.");
                } else if (item == null) {
                    clothing_requirements.setText("Please select item type.");
                } else if (imageViewSelectedImage.getDrawable() == null) {
                    clothing_requirements.setText("Please select an image.");
                } else {
                    Clothing clothing = new Clothing(mUser.getUid(),
                            name.getText().toString().trim(),
                            size.getText().toString().trim(),
                            colour.getText().toString().trim(),
                            brand.getText().toString().trim(),
                            item,
                            mUser.getUid() + name.getText().toString().trim());

                    db.collection("clothing")
                            .add(clothing)
                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {
                                    Log.d("Debug", "DocumentSnapshort added with ID" + documentReference.getId());
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w("Debug", "Error adding document", e);
                                }
                            });
                    uploadImage(clothing);

                }

            }
        });

    }

    private void uploadImage(Clothing clothing) {

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setTitle("Uploading Image...");
        progressDialog.show();
        String fileName = mUser.getUid() + name.getText();
        storageReference = FirebaseStorage.getInstance().getReference("clothing/" + fileName);
        storageReference.putFile(imageUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                        ImageView imageViewSelectedImage = getActivity().findViewById(R.id.imageView_upload_image);
                        imageViewSelectedImage.setImageURI(null);
                        Log.d("DEBUG", "Image Successfully uploaded");
                        if(progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                        FragmentTransaction fragmentTransactionTwo = getActivity().getSupportFragmentManager().beginTransaction();
                        Fragment newClothingFragment = ClothingInfoFragment.newInstance(clothing);
                        fragmentTransactionTwo.replace(R.id.fragmentContainerView, newClothingFragment);
                        fragmentTransactionTwo.commit();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if(progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
                Log.d("DEBUG", "Image uploaded FAILED!!");
            }
        });
    }

    private void selectImage(View view) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,1);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && data != null && data.getData() != null) {
            imageUri = data.getData();
            ImageView imageViewSelectedImage = getActivity().findViewById(R.id.imageView_upload_image);
            imageViewSelectedImage.setImageURI(imageUri);
        }
    }
}