package com.bcit.fashion_app;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;


public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.ViewHolder> {

    private ArrayList<Outfit> localDataSet;

    /**
     * Provide a reference to the type of views that you are using
     * This template comes with a TextView
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView textView;
        private final ImageView shirt;
        private final ImageView pants;
        private final ImageView shoes;

        public ViewHolder(View view) {
            super(view);

            textView = view.findViewById(R.id.home_recycler_textview); //error here should be expected, this is a template
            shirt = view.findViewById(R.id.shirt_imageview);
            pants = view.findViewById(R.id.pants_imageview);
            shoes = view.findViewById(R.id.shoes_imageview);
        }

        public TextView getTextView() {
            return textView;
        }
        public ImageView getShirt(){return shirt;}
        public ImageView getPants(){return pants;}
        public ImageView getShoes(){return shoes;}
    }

    /**
     * Initialize the dataset of the Adapter.
     *
     * @param dataSet String[] containing the data to populate views to be used
     *                by RecyclerView.
     */
    public HomeAdapter(ArrayList<Outfit> dataSet) {
        localDataSet = dataSet;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.home_recycler_component, viewGroup, false); //error here should be expected, this is a template

        return new ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        StorageReference mStorageReference_shirt = FirebaseStorage.getInstance().getReference().child("clothing/"
                + localDataSet.get(position).getUserId() + localDataSet.get(position).getShirt().getName());

        StorageReference mStorageReference_pants = FirebaseStorage.getInstance().getReference().child("clothing/"
                + localDataSet.get(position).getUserId() + localDataSet.get(position).getPants().getName());

        StorageReference mStorageReference_shoes = FirebaseStorage.getInstance().getReference().child("clothing/"
                + localDataSet.get(position).getUserId() + localDataSet.get(position).getShoe().getName());

        try {
            final File localFile = File.createTempFile(localDataSet.get(position).getShirt().getName(),"jpeg");
            mStorageReference_shirt.getFile(localFile)
                    .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            Log.d("DEBUG", "image retrieved & loaded");
                            Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                            viewHolder.getShirt().setImageBitmap(bitmap);
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

        try {
            final File localFile = File.createTempFile(localDataSet.get(position).getPants().getName(),"jpeg");
            mStorageReference_pants.getFile(localFile)
                    .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            Log.d("DEBUG", "image retrieved & loaded");
                            Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                            viewHolder.getPants().setImageBitmap(bitmap);
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


        try {
            final File localFile = File.createTempFile(localDataSet.get(position).getShoe().getName(),"jpeg");
            mStorageReference_shoes.getFile(localFile)
                    .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            Log.d("DEBUG", "image retrieved & loaded");
                            Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                            viewHolder.getShoes().setImageBitmap(bitmap);
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

        if (localDataSet.get(position).getUserId().equalsIgnoreCase("KlysIE4bfnhQ1Nofqz1CE2IGFU63")){
            viewHolder.getTextView().setText("tristan.engen");
        } else if (localDataSet.get(position).getUserId().equalsIgnoreCase("8apPUEpMO0gKHG7VnYKYvMFHAi53")) {
            viewHolder.getTextView().setText("scott.everett");
        } else if (localDataSet.get(position).getUserId().equalsIgnoreCase("w22S5nWqgSV717xcjwLcnrh4oCM2")) {
            viewHolder.getTextView().setText("benjamin.lui");
        } else if (localDataSet.get(position).getUserId().equalsIgnoreCase("nI6kcrmrxtNGAznrW3MFSxWPRkj2")) {
            viewHolder.getTextView().setText("maikol.chow");
        } else {
            viewHolder.getTextView().setText(localDataSet.get(position).getUserId());
        }

        Clothing shirt = localDataSet.get(position).getShirt();
        Clothing pants = localDataSet.get(position).getPants();
        Clothing shoes = localDataSet.get(position).getShoe();
        String uid = localDataSet.get(position).getUserId();





    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return localDataSet.size();
    }
}
