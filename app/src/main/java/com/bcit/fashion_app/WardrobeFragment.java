package com.bcit.fashion_app;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link WardrobeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WardrobeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";

    // TODO: Rename and change types of parameters
    // this fragment contains a recycler view so need this list of Clothing objects
    // to populate the images and text description
    private ArrayList<Clothing> mParam1;

    public WardrobeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment WardrobeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static WardrobeFragment newInstance(ArrayList<Clothing> param1) {
        WardrobeFragment fragment = new WardrobeFragment();
        Bundle args = new Bundle();
        // serialize an arraylist<Clothing> ?
        args.putSerializable(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            // not sure if we can do this, serialize an arraylist of clothing objects
            mParam1 = (ArrayList<Clothing>) getArguments().getSerializable(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_wardrobe, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // setup the recycler view in the wardrobe fragment
        // this list will have pairs of Clothing
        ArrayList<ArrayList<Clothing>> listOfListClothingForRecycler = new ArrayList<>();
        if (mParam1.size() % 2 == 0) {
            // preparing and setting up new ArrayList<ArrayList<Clothing>> for recycler adapter
            int sizeOfArrayListArrayListClothing = mParam1.size() / 2;
            // loop helper to add content to ArrayList<ArrayList<Clothing>>
            for (int i = 0; i < sizeOfArrayListArrayListClothing; i++) {
                ArrayList<Clothing> temp = new ArrayList<>();
                temp.add(mParam1.get(i + i));
                temp.add(mParam1.get(i + i + 1));
                listOfListClothingForRecycler.add(temp);
            }
        } else {
            mParam1.add(null);
            int sizeOfArrayListArrayListClothing = mParam1.size() / 2;
            for (int i = 0; i < sizeOfArrayListArrayListClothing; i++) {
                ArrayList<Clothing> temp = new ArrayList<>();

                temp.add(mParam1.get(i + i));
                temp.add(mParam1.get(i + i + 1));
                listOfListClothingForRecycler.add(temp);
            }
        }
        for (ArrayList<Clothing> listCloth: listOfListClothingForRecycler) {
            System.out.println(listCloth);
        }


        Button create_item_button = view.findViewById(R.id.button_wardrobe_build_outfit);
        create_item_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction fragmentTransactionTwo = getActivity().getSupportFragmentManager().beginTransaction();
                Fragment newClothingFragment = NewClothingFragment.newInstance("yolo");
                fragmentTransactionTwo.replace(R.id.fragmentContainerView, newClothingFragment);
                fragmentTransactionTwo.commit();
            }
        });

        RecyclerView recyclerView = view.findViewById(R.id.reyclerView_wardrobe);
        WardrobeRecyclerAdapter adapter = new WardrobeRecyclerAdapter(listOfListClothingForRecycler);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
    }
}