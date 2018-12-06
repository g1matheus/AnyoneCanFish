package com.homebrewforlife.sharkydart.anyonecanfish.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.homebrewforlife.sharkydart.anyonecanfish.BasicEquipment_Activity;
import com.homebrewforlife.sharkydart.anyonecanfish.LuresActivity;
import com.homebrewforlife.sharkydart.anyonecanfish.R;
import com.homebrewforlife.sharkydart.anyonecanfish.TackleBoxesActivity;
import com.homebrewforlife.sharkydart.anyonecanfish.fireX.FirestoreStuff;
import com.homebrewforlife.sharkydart.anyonecanfish.models.Fire_Lure;
import com.homebrewforlife.sharkydart.anyonecanfish.models.Fire_TackleBox;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class TackleBoxesRVAdapter extends RecyclerView.Adapter<TackleBoxesRVAdapter.ViewHolder> {
    private Context mContext;
    private final ArrayList<Fire_TackleBox> mTackleBoxArrayList;
    private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Fire_TackleBox theBox = (Fire_TackleBox)view.getTag();

            Intent intent = new Intent(mContext, LuresActivity.class);
            intent.putExtra(TackleBoxesActivity.THE_TACKLEBOX, theBox);
            Log.d("fart", "Clicked tacklebox: " + theBox.getUid() + " " + theBox.getDesc() + " " + theBox.getName());

            mContext.startActivity(intent);
        }
    };

    public TackleBoxesRVAdapter(AppCompatActivity parent, ArrayList<Fire_TackleBox> items) {
        mTackleBoxArrayList = items;
        mContext = parent;
    }

    @Override @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.rv_content_tackle_boxes, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        holder.mName.setText(mTackleBoxArrayList.get(position).getName());
        holder.mDescription.setText(mTackleBoxArrayList.get(position).getDesc());

        if(mTackleBoxArrayList.get(position).getImage_url() != null && !mTackleBoxArrayList.get(position).getImage_url().isEmpty())
            Picasso.get().load(mTackleBoxArrayList.get(position).getImage_url())
                    .into(holder.mTackleBoxPic);
        else
            Picasso.get().load(R.drawable.t_b_open_smallbrown)
                    .into(holder.mTackleBoxPic);

        holder.itemView.setTag(mTackleBoxArrayList.get(position));

        //TODO - The following line is commented out because I couldn't solve an issue with loading Lures
        //holder.itemView.setOnClickListener(mOnClickListener);
    }

    @Override
    public int getItemCount() {
        return mTackleBoxArrayList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView mDescription;
        TextView mName;
        TextView mLureCount;
        ImageView mTackleBoxPic;

        ViewHolder(View view) {
            super(view);
            mDescription = view.findViewById(R.id.tvDescription);
            mName = view.findViewById(R.id.tvName);
            mLureCount = view.findViewById(R.id.tvLureCount);
            mTackleBoxPic = view.findViewById(R.id.imgTackleBoxPic);
        }
    }
}
