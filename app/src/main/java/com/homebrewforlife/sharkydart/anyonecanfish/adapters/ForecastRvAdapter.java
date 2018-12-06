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
import com.homebrewforlife.sharkydart.anyonecanfish.models.ForecastPeriod;
import com.homebrewforlife.sharkydart.anyonecanfish.models.SolunarData;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import static com.homebrewforlife.sharkydart.anyonecanfish.handwavyfishingmagic.OptimusCalculatron.pleaseSendYourVisualGuidanceSoThatIMayGrokTheFullness;

public class ForecastRvAdapter extends RecyclerView.Adapter<ForecastRvAdapter.ViewHolder> {
    private final AppCompatActivity mParentActivity;
    private final ArrayList<ForecastPeriod> mForecastPeriodArrayList;
    private final SolunarData mSolunarData;
    private Context mContext;
    private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            ForecastPeriod thePeriod = (ForecastPeriod) view.getTag();
            Log.d("fart", "clicked day: " + thePeriod.getName());
        }
    };

    public ForecastRvAdapter(AppCompatActivity parent, ArrayList<ForecastPeriod> items, SolunarData theSolunarData) {
        mForecastPeriodArrayList = items;
        mParentActivity = parent;
        mSolunarData = theSolunarData;
    }

    @Override @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.fragment_day, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        ForecastPeriod thePeriod = mForecastPeriodArrayList.get(position);
        String temperature = thePeriod.getTemperature() + " "
                + thePeriod.getTemperatureUnit();
        holder.mTvTemperature.setText(temperature);
        holder.mTvWindSpeed.setText(thePeriod.getWindSpeed());
        holder.mTvWindDirection.setText(thePeriod.getWindDirection());
        holder.mTvShortForecast.setText(thePeriod.getShortForecast());
        holder.mTvPeriodName.setText(thePeriod.getName());
        Picasso.get().load(thePeriod.getIconURL()).into(holder.mImgWeatherIcon);

        //Beg Optimus Calculatron for its astute, otherworldly wisdom
        if(mSolunarData != null)
            Picasso.get().load(pleaseSendYourVisualGuidanceSoThatIMayGrokTheFullness(mContext, thePeriod, mSolunarData)).into(holder.mImgDecreeOfCalculatron);

        holder.itemView.setTag(thePeriod);
        holder.itemView.setOnClickListener(mOnClickListener);
    }

    @Override
    public int getItemCount() {
        return mForecastPeriodArrayList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView mTvTemperature;
        TextView mTvWindSpeed;
        TextView mTvWindDirection;
        TextView mTvPeriodName;
        TextView mTvShortForecast;
        ImageView mImgWeatherIcon;
        ImageView mImgDecreeOfCalculatron;

        ViewHolder(View view) {
            super(view);
            mImgDecreeOfCalculatron = view.findViewById(R.id.imgGuessAtSuccess);
            mTvTemperature = view.findViewById(R.id.tvTemperature);
            mTvWindSpeed = view.findViewById(R.id.tvWindSpeed);
            mTvWindDirection = view.findViewById(R.id.tvWindDirection);
            mImgWeatherIcon = view.findViewById(R.id.imgWeatherIcon);
            mTvPeriodName = view.findViewById(R.id.tvPeriodName);
            mTvShortForecast = view.findViewById(R.id.tvShortForecast);
        }
    }
}
