package com.homebrewforlife.sharkydart.anyonecanfish.adapters;

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
import com.homebrewforlife.sharkydart.anyonecanfish.models.Fire_BasicInfo;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class BasicEquipmentRVAdapter extends RecyclerView.Adapter<BasicEquipmentRVAdapter.ViewHolder> {
    private final ArrayList<Fire_BasicInfo> mBasicInfoArrayList;
    private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Fire_BasicInfo theItem = (Fire_BasicInfo) view.getTag();
            Log.d("fart", "clicked equip: " + theItem.getTitle());
            //do a search for this + "fishing"
            if(view.findViewById(R.id.tvBody).getVisibility() == VISIBLE)
                view.findViewById(R.id.tvBody).setVisibility(GONE);
            else if(view.findViewById(R.id.tvBody).getVisibility() == GONE)
                view.findViewById(R.id.tvBody).setVisibility(VISIBLE);
        }
    };

    public BasicEquipmentRVAdapter(AppCompatActivity parent, ArrayList<Fire_BasicInfo> items) {
        mBasicInfoArrayList = items;
    }

    @Override @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.rv_content_basic_equipment, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        holder.mTitle.setText(mBasicInfoArrayList.get(position).getTitle());
        holder.mBody.setText(mBasicInfoArrayList.get(position).getBody());

        if(mBasicInfoArrayList.get(position).getImage_url() != null && !mBasicInfoArrayList.get(position).getImage_url().isEmpty())
            Picasso.get().load(mBasicInfoArrayList.get(position).getImage_url())
                    .into(holder.mImg_url);
        else
            holder.mImg_url.setVisibility(GONE);

        holder.itemView.setTag(mBasicInfoArrayList.get(position));
        holder.itemView.setOnClickListener(mOnClickListener);
    }

    @Override
    public int getItemCount() {
        return mBasicInfoArrayList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView mBody;
        TextView mTitle;
        ImageView mImg_url;

        ViewHolder(View view) {
            super(view);
            mTitle = view.findViewById(R.id.tvTitle);
            mBody = view.findViewById(R.id.tvBody);
            mImg_url = view.findViewById(R.id.imgPic);
        }
    }
}
