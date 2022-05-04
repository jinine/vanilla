package com.bcit.fashion_app;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link OutfitFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OutfitFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";

    // TODO: Rename and change types of parameters
    private String mParam1;


    public OutfitFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment OutfitFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static OutfitFragment newInstance(String param1) {
        OutfitFragment fragment = new OutfitFragment();
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
        return inflater.inflate(R.layout.fragment_outfit, container, false);
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

        EditText editText_search = view.findViewById(R.id.editText_outfit_A);
        TextView clothing_one_name = view.findViewById(R.id.textView_outfit_one);
        TextView clothing_one_size = view.findViewById(R.id.textView_outfit_size);
        TextView clothing_one_color = view.findViewById(R.id.textView_outfit_color);
        TextView clothing_one_brand = view.findViewById(R.id.textView_outfit_brand);
        TextView clothing_one_item = view.findViewById(R.id.textView_outfit_item);
        TextView clothing_one_image = view.findViewById(R.id.textView_outfit_image);

        TextView clothing_two_name = view.findViewById(R.id.textView_outfit_two);
        TextView clothing_two_size = view.findViewById(R.id.textView_outfit_size_two);
        TextView clothing_two_color = view.findViewById(R.id.textView_outfit_color_two);
        TextView clothing_two_brand = view.findViewById(R.id.textView_outfit_brand_two);
        TextView clothing_two_item = view.findViewById(R.id.textView_outfit_item_two);
        TextView clothing_two_image = view.findViewById(R.id.textView_outfit_image_two);

        TextView clothing_three_name = view.findViewById(R.id.textView_outfit_three);
        TextView clothing_three_size = view.findViewById(R.id.textView_outfit_size_three);
        TextView clothing_three_color = view.findViewById(R.id.textView_outfit_color_three);
        TextView clothing_three_brand = view.findViewById(R.id.textView_outfit_brand_three);
        TextView clothing_three_item = view.findViewById(R.id.textView_outfit_item_three);
        TextView clothing_three_image = view.findViewById(R.id.textView_outfit_image_three);

        Button search_button = view.findViewById(R.id.button_outfit_search);
        search_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db.collection("clothing")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    Clothing clothing = null;
                                    String search_value = editText_search.getText().toString();

                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        //Log.d("Debug", document.getData().toString());
                                        if (document.getData().get("userId").toString().equals(mUser.getUid().toString())) {

                                            if(document.getData().get("name").toString().equals(search_value)) {

                                                clothing = new Clothing(document.getData().get("userId").toString(),
                                                        document.getData().get("name").toString(),
                                                        document.getData().get("size").toString(),
                                                        document.getData().get("colour").toString(),
                                                        document.getData().get("brand").toString(),
                                                        ItemType.valueOf(document.getData().get("item").toString()),
                                                        document.getData().get("clothingImageId").toString());

                                                if (clothing_one_name.getText() == "") {
                                                    clothing_one_name.setText(clothing.getName());
                                                    clothing_one_size.setText(clothing.getSize());
                                                    clothing_one_brand.setText(clothing.getBrand());
                                                    clothing_one_color.setText(clothing.getColour());
                                                    clothing_one_item.setText(clothing.getItem().toString());
                                                    clothing_one_image.setText(clothing.getClothingImageId());
                                                    break;
                                                } else if (clothing_two_name.getText() == "") {
                                                    clothing_two_name.setText(clothing.getName());
                                                    clothing_two_size.setText(clothing.getSize());
                                                    clothing_two_brand.setText(clothing.getBrand());
                                                    clothing_two_color.setText(clothing.getColour());
                                                    clothing_two_item.setText(clothing.getItem().toString());
                                                    clothing_two_image.setText(clothing.getClothingImageId());
                                                    break;
                                                } else {
                                                    clothing_three_name.setText(clothing.getName());
                                                    clothing_three_size.setText(clothing.getSize());
                                                    clothing_three_brand.setText(clothing.getBrand());
                                                    clothing_three_color.setText(clothing.getColour());
                                                    clothing_three_item.setText(clothing.getItem().toString());
                                                    clothing_three_image.setText(clothing.getClothingImageId());
                                                    break;
                                                }
                                            }
                                        }
                                    }
                                } else {
                                    Log.w("Debug", "Error getting documents.", task.getException());
                                }
                            }
                        });
            }
        });

        Button add_outfit_button = view.findViewById(R.id.button_outfit_add);
        add_outfit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              if (clothing_one_name.getText() == "" || clothing_two_name.getText() == ""
                      || clothing_three_name.getText() == "") {
                  System.out.println("Please add a shirt, pants, and shoes..");
              } else {
                  Clothing clothing_one = new Clothing(mUser.getUid(), clothing_one_name.getText().toString(),
                          clothing_one_size.getText().toString(), clothing_one_color.getText().toString(),
                          clothing_one_brand.getText().toString(),
                          ItemType.valueOf(clothing_one_item.getText().toString().toUpperCase()),
                          clothing_one_image.getText().toString());
                  Clothing clothing_two = new Clothing(mUser.getUid(), clothing_two_name.getText().toString(),
                          clothing_two_size.getText().toString(), clothing_two_color.getText().toString(),
                          clothing_two_brand.getText().toString(),
                          ItemType.valueOf(clothing_two_item.getText().toString().toUpperCase()),
                          clothing_two_image.getText().toString());
                  Clothing clothing_three = new Clothing(mUser.getUid(), clothing_three_name.getText().toString(),
                          clothing_three_size.getText().toString(), clothing_three_color.getText().toString(),
                          clothing_three_brand.getText().toString(),
                          ItemType.valueOf(clothing_three_item.getText().toString().toUpperCase()),
                          clothing_three_image.getText().toString());
                  Outfit outfit_data_to_write = new Outfit(mUser.getUid(), clothing_one, clothing_two, clothing_three);
                  db.collection("outfit")
                          .add(outfit_data_to_write)
                          .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                              @Override
                              public void onSuccess(DocumentReference documentReference) {
                                  FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                                  Fragment outfitDisplayFragment = OutfitDisplayFragment.newInstance("", "");
                                  fragmentTransaction.replace(R.id.fragmentContainerView, outfitDisplayFragment);
                                  fragmentTransaction.commit();
                                  Log.d("Debug", "DocumentSnapshort added with ID" + documentReference.getId());
                              }
                          })
                          .addOnFailureListener(new OnFailureListener() {
                              @Override
                              public void onFailure(@NonNull Exception e) {
                                  Log.w("Debug", "Error adding document", e);
                              }
                          });
              }
            }
        });
    }
}