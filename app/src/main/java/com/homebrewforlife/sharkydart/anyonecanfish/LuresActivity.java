package com.homebrewforlife.sharkydart.anyonecanfish;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.homebrewforlife.sharkydart.anyonecanfish.AsyncTask.DataGrabListener;
import com.homebrewforlife.sharkydart.anyonecanfish.AsyncTask.FirestoreGrabber;
import com.homebrewforlife.sharkydart.anyonecanfish.adapters.LuresRVAdapter;
import com.homebrewforlife.sharkydart.anyonecanfish.fireX.FirestoreAdds;
import com.homebrewforlife.sharkydart.anyonecanfish.fireX.FirestoreStuff;
import com.homebrewforlife.sharkydart.anyonecanfish.models.Fire_Lure;
import com.homebrewforlife.sharkydart.anyonecanfish.models.Fire_TackleBox;
import com.homebrewforlife.sharkydart.anyonecanfish.models.Fire_User;

import java.util.ArrayList;

public class LuresActivity extends AppCompatActivity implements DataGrabListener{

    ArrayList<Fire_Lure> mLuresArrayList;
    LuresRVAdapter mLuresRVAdapter;
    RecyclerView mLuresRV;
    private Context mContext;
    Fire_TackleBox mInTacklebox;
    FirestoreGrabber fs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lures);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mContext = this;
        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        if(savedInstanceState == null)
            mLuresArrayList = new ArrayList<>();
        else if(savedInstanceState.containsKey(TackleBoxesActivity.LURES_ARRAYLIST) && savedInstanceState.containsKey(TackleBoxesActivity.THE_TACKLEBOX)){
            mLuresArrayList = savedInstanceState.getParcelableArrayList(TackleBoxesActivity.LURES_ARRAYLIST);
            mInTacklebox = savedInstanceState.getParcelable(TackleBoxesActivity.THE_TACKLEBOX);
        }

        Intent intent = getIntent();
        if (intent == null) {
            closeOnError();
        }else {
            mInTacklebox = intent.getParcelableExtra(TackleBoxesActivity.THE_TACKLEBOX);
            Log.d("fart", mInTacklebox.getUid() + "  " + mInTacklebox.getName());
            loadLuresFromFirebase();
        }

        mLuresRV = findViewById(R.id.rvLures);
        assert mLuresRV != null;
        setupRecyclerView(mLuresRV);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.lures_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Add a lure!", Snackbar.LENGTH_LONG)
                        .setAction("Add lure", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                FirebaseFirestore mFS_Store = FirebaseFirestore.getInstance();
                                FirebaseUser mCurUser = FirebaseAuth.getInstance().getCurrentUser();
                                if(mCurUser != null){
                                    //name, size, type, desc
                                    Fire_Lure fire_lure = new Fire_Lure(
                                        ((EditText)findViewById(R.id.etName)).getText().toString(),
                                        ((EditText)findViewById(R.id.etDesc)).getText().toString(),
                                        ((EditText)findViewById(R.id.etType)).getText().toString(),
                                        ((EditText)findViewById(R.id.etSize)).getText().toString()
                                    );
                                    FirestoreAdds.addFS_lure(mContext, mFS_Store, new Fire_User(mCurUser), mInTacklebox, fire_lure, mLuresArrayList);
                                    Toast.makeText(mContext, "Making a Lure...", Toast.LENGTH_SHORT).show();
                                }
                                else
                                    Toast.makeText(mContext, "mCurUser is null", Toast.LENGTH_LONG).show();
                            }
                        }).show();
                Log.i("fart", "clicked FAB");
            }
        });
    }

    private void loadLuresFromFirebase(){
        ArrayList<Fire_Lure> theLures = new ArrayList<>();
        FirebaseUser theuser = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseFirestore theFS = FirebaseFirestore.getInstance();
        FirestoreStuff fss = new FirestoreStuff(mContext, theuser, theFS);
        FirestoreGrabber fs = new FirestoreGrabber();
        fs.setMyTackleBox(mInTacklebox);
        fs.setMyFirestoreStuff(fss);
        fs.setMyArrayList(theLures);
        fs.execute(this);
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        mLuresRVAdapter = new LuresRVAdapter(this, mLuresArrayList, mInTacklebox);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(mLuresRVAdapter);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(TackleBoxesActivity.LURES_ARRAYLIST, mLuresArrayList);
        outState.putParcelable(TackleBoxesActivity.THE_TACKLEBOX, mInTacklebox);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mLuresArrayList = savedInstanceState.getParcelableArrayList(TackleBoxesActivity.LURES_ARRAYLIST);
        mInTacklebox = savedInstanceState.getParcelable(TackleBoxesActivity.THE_TACKLEBOX);
    }

    private void closeOnError() {
        finish();
        Toast.makeText(this, "Can't find lures", Toast.LENGTH_SHORT).show();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onComplete(ArrayList<Fire_Lure> output) {
        if(output != null)
            Log.d("fart", "Lure count: " + output.size());
        mLuresArrayList = output;
        mLuresRVAdapter.notifyDataSetChanged();
        setupRecyclerView(mLuresRV);
    }
}
