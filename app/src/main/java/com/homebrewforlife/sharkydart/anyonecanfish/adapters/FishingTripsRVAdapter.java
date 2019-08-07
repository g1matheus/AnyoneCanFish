package com.homebrewforlife.sharkydart.anyonecanfish.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.homebrewforlife.sharkydart.anyonecanfish.R;
import com.homebrewforlife.sharkydart.anyonecanfish.models.Fire_Trip;

import java.util.ArrayList;

public class FishingTripsRVAdapter extends RecyclerView.Adapter<FishingTripsRVAdapter.ViewHolder> {
    private Context mContext;
    private final ArrayList<Fire_Trip> mTripsArrayList;
    private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Fire_Trip theTrip = (Fire_Trip)view.getTag();
            Log.d("fart", "Clicked trip: " + theTrip.getUid() + " " + theTrip.getDesc() + " " + theTrip.getName());
            //Intent intent = new Intent(mContext, TripDetailActivity.class);
            //intent.putExtra(TackleBoxesActivity.THE_TACKLEBOX, theBox);
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

/*
        if(mTripsArrayList.get(position).getImage_url() != null && !mTripsArrayList.get(position).getImage_url().isEmpty())
            Picasso.get().load(mTripsArrayList.get(position).getImage_url())
                    .into(holder.mImgTripPic);
        else
            Picasso.get().load(R.drawable.t_b_open_smallbrown)  //need default trip Image
                    .into(holder.mImgTripPic);
*/

        holder.itemView.setTag(mTripsArrayList.get(position));

        //TODO - The following line is commented out because of another adapter
        //holder.itemView.setOnClickListener(mOnClickListener);
    }

    @Override
    public int getItemCount() {
        return mTripsArrayList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView mTvTripDesc;
        TextView mTvTripName;
        ImageView mImgTripPic;

        ViewHolder(View view) {
            super(view);
            mTvTripDesc = view.findViewById(R.id.tvTripDesc);
            mTvTripName = view.findViewById(R.id.tvTripName);
            mImgTripPic = view.findViewById(R.id.imgTripPic);
        }
    }
}
