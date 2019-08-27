package com.homebrewforlife.sharkydart.anyonecanfish.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.homebrewforlife.sharkydart.anyonecanfish.FishingTripsActivity;
import com.homebrewforlife.sharkydart.anyonecanfish.R;
import com.homebrewforlife.sharkydart.anyonecanfish.models.Fire_Trip;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import static android.app.Activity.RESULT_OK;

public class FishingTripsRVAdapter extends RecyclerView.Adapter<FishingTripsRVAdapter.ViewHolder> {
    private Context mContext;
    private final ArrayList<Fire_Trip> mTripsArrayList;
    private final View.OnClickListener mPhotoBtnOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Fire_Trip theTrip = (Fire_Trip)view.getTag();
            Log.d("fart", "Clicked photo btn for: " + theTrip.getUid() + " " + theTrip.getDesc() + " " + theTrip.getName());
            //Intent intent = new Intent(mContext, TripDetailActivity.class);
            //intent.putExtra(TackleBoxesActivity.THE_TACKLEBOX, theTrip);
            //mContext.startActivity(intent);

            Intent ppIntent = new Intent(Intent.ACTION_GET_CONTENT);
            ppIntent.setType("image/jpeg");
            ppIntent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
            ((Activity) mContext).startActivityForResult(
                    Intent.createChooser(
                            ppIntent,
                            "Complete action using"),
                    mTripsArrayList.indexOf(theTrip));
        }
    };
    private final View.OnClickListener mEditBtnOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Fire_Trip theTrip = (Fire_Trip)view.getTag();
            Log.d("fart", "Clicked edit btn for: " + theTrip.getUid() + " " + theTrip.getDesc() + " " + theTrip.getName());

        }
    };
    private final View.OnClickListener mTripOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Fire_Trip theTrip = (Fire_Trip)view.getTag();
            Log.d("fart", "Clicked trip: " + theTrip.getUid() + " " + theTrip.getDesc() + " " + theTrip.getName());
            //Intent intent = new Intent(mContext, TripDetailActivity.class);
            //intent.putExtra(FishEventsActivity.THE_TACKLEBOX, theTrip);
            //mContext.startActivity(intent);
        }
    };

    public FishingTripsRVAdapter(AppCompatActivity parent, ArrayList<Fire_Trip> items) {
        mTripsArrayList = items;
        mContext = parent;
    }

    @Override @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.rv_content_trips, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        holder.mTvTripName.setText(mTripsArrayList.get(position).getName());
        holder.mTvTripDesc.setText(mTripsArrayList.get(position).getDesc());

        if(mTripsArrayList.get(position).getImage_url() != null && !mTripsArrayList.get(position).getImage_url().isEmpty())
            Picasso.get().load(mTripsArrayList.get(position).getImage_url()).error(R.drawable.default_trip)
                    .into(holder.mImgTripPic);
        else
            Picasso.get().load(R.drawable.default_trip)  //need default trip Image
                    .into(holder.mImgTripPic);

        holder.mImgPicker.setTag(mTripsArrayList.get(position));
        holder.mImgPicker.setOnClickListener(mPhotoBtnOnClickListener);
        holder.mEditDetails.setTag(mTripsArrayList.get(position));
        holder.mEditDetails.setOnClickListener(mEditBtnOnClickListener);

        holder.mImgTripPic.setTag(mTripsArrayList.get(position));
        holder.mImgTripPic.setOnClickListener(mTripOnClickListener);
    }

    @Override
    public int getItemCount() {
        return mTripsArrayList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView mTvTripDesc;
        TextView mTvTripName;
        ImageView mImgTripPic;
        ImageButton mImgPicker;
        ImageButton mEditDetails;

        ViewHolder(View view) {
            super(view);
            mTvTripDesc = view.findViewById(R.id.tvTripDesc);
            mTvTripName = view.findViewById(R.id.tvTripName);
            mImgTripPic = view.findViewById(R.id.imgTripPic);
            mImgPicker = view.findViewById(R.id.btnImgPicker);
            mEditDetails = view.findViewById(R.id.btnEditDetails);
        }
    }

    private void doCameraStuff_TinyBitmap(){
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(mContext.getPackageManager()) != null) {
            ((Activity) mContext).startActivityForResult(takePictureIntent, FishingTripsActivity.REQUEST_FISH_THUMBNAIL_CAPTURE);
        }
    }

    private File createImageFile(String baseName) throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(new Date());
        String imageFileName = baseName + "_" + timeStamp;
        File storageDir = mContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        FishingTripsActivity.mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private void dispatchTakePictureIntent(String baseName) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(mContext.getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile(baseName);
            } catch (IOException ex) {
                // Error occurred while creating the File
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(mContext,
                        "com.example.android.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                ((Activity) mContext).startActivityForResult(takePictureIntent, FishingTripsActivity.REQUEST_FISH_BIG_CAPTURE);
            }
        }
    }
}
