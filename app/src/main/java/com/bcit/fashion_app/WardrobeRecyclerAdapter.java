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


public class WardrobeRecyclerAdapter extends RecyclerView.Adapter<WardrobeRecyclerAdapter.ViewHolder> {

    private ArrayList<ArrayList<Clothing>> localDataSet;
    private StorageReference mStorageReferenceLeft;
    private StorageReference mStorageReferenceRight;

    /**
     * Provide a reference to the type of views that you are using
     * This template comes with a TextView
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView textViewLeft;
        private final TextView textViewRight;
        private final ImageView imageViewLeft;
        private final ImageView imageViewRight;


        public ViewHolder(View view) {
            super(view);

            textViewLeft = view.findViewById(R.id.textView_wardrobe_left); //error here should be expected, this is a template
            textViewRight = view.findViewById(R.id.textView_wardrobe_right);
            imageViewLeft = view.findViewById(R.id.imageView_wardrobe_left);
            imageViewRight = view.findViewById(R.id.imageView_wardrobe_right);

        }

        public TextView getTextViewLeft() {
            return textViewLeft;
        }
        public TextView getTextViewRight() {
            return textViewRight;
        }
        public ImageView getImageViewLeft() {
            return imageViewLeft;
        }
        public ImageView getImageViewRight() {
            return imageViewRight;
        }


    }

    /**
     * Initialize the dataset of the Adapter.
     *
     * @param dataSet String[] containing the data to populate views to be used
     *                by RecyclerView.
     */
    public WardrobeRecyclerAdapter(ArrayList<ArrayList<Clothing>> dataSet) {
        localDataSet = dataSet;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.wardrobe_recycler_component, viewGroup, false); //error here should be expected, this is a template

        return new ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {

        viewHolder.getTextViewLeft().setText(localDataSet.get(position).get(0).getName());
        mStorageReferenceLeft = FirebaseStorage.getInstance().getReference().child("clothing/"
                + localDataSet.get(position).get(0).getUserId() + localDataSet.get(position).get(0).getName());
        try {
            final File localFile = File.createTempFile(localDataSet.get(position).get(0).getName(),"jpeg");
            mStorageReferenceLeft.getFile(localFile)
                    .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            Log.d("DEBUG", "image retrieved & loaded");
                            Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                            viewHolder.getImageViewLeft().setImageBitmap(bitmap);
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
        if (localDataSet.get(position).get(1) != null) {
            viewHolder.getTextViewRight().setText(localDataSet.get(position).get(1).getName());
            mStorageReferenceRight = FirebaseStorage.getInstance().getReference().child("clothing/"
                    + localDataSet.get(position).get(1).getUserId() + localDataSet.get(position).get(1).getName());
            try {
                final File localFile = File.createTempFile(localDataSet.get(position).get(1).getName(),"jpeg");
                mStorageReferenceRight.getFile(localFile)
                        .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                Log.d("DEBUG", "image retrieved & loaded");
                                Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                                viewHolder.getImageViewRight().setImageBitmap(bitmap);
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
        } else {
            viewHolder.getTextViewRight().setText("");
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return localDataSet.size();
    }
}