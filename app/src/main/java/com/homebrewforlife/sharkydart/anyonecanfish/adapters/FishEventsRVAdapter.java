package com.homebrewforlife.sharkydart.anyonecanfish.adapters;

import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.homebrewforlife.sharkydart.anyonecanfish.R;
import com.homebrewforlife.sharkydart.anyonecanfish.fireX.FirestoreDeletes;
import com.homebrewforlife.sharkydart.anyonecanfish.models.Fire_FishEvent;
import com.homebrewforlife.sharkydart.anyonecanfish.models.Fire_Lure;
import com.homebrewforlife.sharkydart.anyonecanfish.models.Fire_TackleBox;
import com.homebrewforlife.sharkydart.anyonecanfish.models.Fire_Trip;
import com.homebrewforlife.sharkydart.anyonecanfish.models.Fire_User;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class FishEventsRVAdapter extends RecyclerView.Adapter<FishEventsRVAdapter.ViewHolder> {
    private final ArrayList<Fire_FishEvent> mFishEventArrayList;
    private Fire_Trip mTrip;
    private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            final Fire_FishEvent theBox = (Fire_FishEvent) view.getTag();
            Log.d("fart", "clicked FishEvent for deletion: " + theBox.getUid());
            Snackbar.make(view, "Delete FishEvent!", Snackbar.LENGTH_LONG)
                    .setAction("Delete FishEvent", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            FirebaseFirestore mFS_Store = FirebaseFirestore.getInstance();
                            FirebaseUser mCurUser = FirebaseAuth.getInstance().getCurrentUser();
                            if(mCurUser != null){
                                FirestoreDeletes.deleteFS_fishevent(view.getContext(), new Fire_User(mCurUser), mTrip, theBox, mFS_Store);
                                Toast.makeText(view.getContext(), "Deleting a Lure...", Toast.LENGTH_SHORT).show();
                            }
                            else
                                Toast.makeText(view.getContext(), "mCurUser is null", Toast.LENGTH_LONG).show();
                        }
                    }).show();
            Log.i("fart", "clicked FAB");
        }
    };

    public FishEventsRVAdapter(AppCompatActivity parent, ArrayList<Fire_FishEvent> items, Fire_Trip trip) {
        mFishEventArrayList = items;
        mTrip = trip;
    }

    @Override @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.rv_content_fish_events, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        holder.mSpecies.setText(mFishEventArrayList.get(position).getSpecies());
        holder.mDescription.setText(mFishEventArrayList.get(position).getDesc());

        if(mFishEventArrayList.get(position).getImage_url() != null && !mFishEventArrayList.get(position).getImage_url().isEmpty())
            Picasso.get().load(mFishEventArrayList.get(position).getImage_url())
                    .into(holder.mFishPic);
        else
            Picasso.get().load(R.drawable.hula_popper_frog)
                    .into(holder.mFishPic);

        holder.itemView.setTag(mFishEventArrayList.get(position));
        holder.itemView.setOnClickListener(mOnClickListener);
    }

    @Override
    public int getItemCount() {
        Log.d("fart", "FishEventsRVAdapter itemcount: " + mFishEventArrayList.size());
        return mFishEventArrayList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView mDescription;
        TextView mSpecies;
        ImageView mFishPic;

        ViewHolder(View view) {
            super(view);
            mDescription = view.findViewById(R.id.tvDescription);
            mSpecies = view.findViewById(R.id.tvFishSpecies);
            mFishPic = view.findViewById(R.id.imgFishPic);
        }
    }
}
