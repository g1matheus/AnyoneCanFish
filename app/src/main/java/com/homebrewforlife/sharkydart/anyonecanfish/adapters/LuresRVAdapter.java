package com.homebrewforlife.sharkydart.anyonecanfish.adapters;

import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.homebrewforlife.sharkydart.anyonecanfish.R;
import com.homebrewforlife.sharkydart.anyonecanfish.fireX.FirestoreAdds;
import com.homebrewforlife.sharkydart.anyonecanfish.fireX.FirestoreDeletes;
import com.homebrewforlife.sharkydart.anyonecanfish.models.Fire_Lure;
import com.homebrewforlife.sharkydart.anyonecanfish.models.Fire_TackleBox;
import com.homebrewforlife.sharkydart.anyonecanfish.models.Fire_User;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class LuresRVAdapter extends RecyclerView.Adapter<LuresRVAdapter.ViewHolder> {
    private ArrayList<Fire_Lure> mLureArrayList;
    private Fire_TackleBox mTackleBox;
    private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            final Fire_Lure theLure = (Fire_Lure) view.getTag();
            Log.d("fart", "clicked Lure for deletion: " + theLure.getUid());
            Snackbar.make(view, "Delete lure!", Snackbar.LENGTH_LONG)
                    .setAction("Delete lure", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            FirebaseFirestore mFS_Store = FirebaseFirestore.getInstance();
                            FirebaseUser mCurUser = FirebaseAuth.getInstance().getCurrentUser();
                            if(mCurUser != null){
                                FirestoreDeletes.deleteFS_lure(view.getContext(), new Fire_User(mCurUser), mTackleBox, theLure, mFS_Store);
                                Toast.makeText(view.getContext(), "Deleting a Lure...", Toast.LENGTH_SHORT).show();
                            }
                            else
                                Toast.makeText(view.getContext(), "mCurUser is null", Toast.LENGTH_LONG).show();
                        }
                    }).show();
            Log.i("fart", "clicked FAB");
        }
    };

    public LuresRVAdapter(AppCompatActivity parent, ArrayList<Fire_Lure> items, Fire_TackleBox tackleBox) {
        mLureArrayList = items;
        mTackleBox = tackleBox;
    }

    @Override @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.rv_content_lures, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        holder.mName.setText(mLureArrayList.get(position).getName());
        holder.mDescription.setText(mLureArrayList.get(position).getDesc());

        if(mLureArrayList.get(position).getImage_url() != null && !mLureArrayList.get(position).getImage_url().isEmpty())
            Picasso.get().load(mLureArrayList.get(position).getImage_url())
                    .into(holder.mLurePic);
        else
            Picasso.get().load(R.drawable.hula_popper_frog)
                    .into(holder.mLurePic);

        holder.itemView.setTag(mLureArrayList.get(position));
        holder.itemView.setOnClickListener(mOnClickListener);
    }

    @Override
    public int getItemCount() {
        return mLureArrayList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView mDescription;
        TextView mName;
        ImageView mLurePic;

        ViewHolder(View view) {
            super(view);
            mDescription = view.findViewById(R.id.tvDescription);
            mName = view.findViewById(R.id.tvName);
            mLurePic = view.findViewById(R.id.imgLurePic);
        }
    }
}
