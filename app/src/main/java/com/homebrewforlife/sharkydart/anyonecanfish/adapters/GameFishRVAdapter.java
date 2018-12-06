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
import com.homebrewforlife.sharkydart.anyonecanfish.models.Fire_GameFish;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class GameFishRVAdapter extends RecyclerView.Adapter<GameFishRVAdapter.ViewHolder> {
    private final AppCompatActivity mParentActivity;
    private final ArrayList<Fire_GameFish> mGameFishArrayList;
    private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Fire_GameFish theFish = (Fire_GameFish) view.getTag();
            Log.d("fart", "clicked species: " + theFish.getSpecies());
        }
    };

    public GameFishRVAdapter(AppCompatActivity parent, ArrayList<Fire_GameFish> items) {
        mGameFishArrayList = items;
        mParentActivity = parent;
    }

    @Override @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.rv_content_game_fish, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        holder.mFishSpecies.setText(mGameFishArrayList.get(position).getSpecies());
        holder.mFishInformation.setText(mGameFishArrayList.get(position).getInformation());

        Picasso.get().load(mGameFishArrayList.get(position).getImage_url())
                .into(holder.mFishPic);

        holder.itemView.setTag(mGameFishArrayList.get(position));
        holder.itemView.setOnClickListener(mOnClickListener);
    }

    @Override
    public int getItemCount() {
        return mGameFishArrayList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView mFishSpecies;
        TextView mFishInformation;
        ImageView mFishPic;
//        Button mFishWikiLink;

        ViewHolder(View view) {
            super(view);
            mFishInformation = view.findViewById(R.id.tvFishInformation);
            mFishSpecies = view.findViewById(R.id.tvFishSpecies);
            mFishPic = view.findViewById(R.id.imgFishPic);
//            mFishWikiLink = view.findViewById(R.id.btnFishWikiLink);
        }
        public void bindData(final Fire_GameFish viewModel){
            mFishSpecies.setText(viewModel.getSpecies());
            mFishInformation.setText(viewModel.getInformation());
            Picasso.get().load(viewModel.getImage_url()).into(mFishPic);
        }
    }
}
