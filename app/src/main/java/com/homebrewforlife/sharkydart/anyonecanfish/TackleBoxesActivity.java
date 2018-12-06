package com.homebrewforlife.sharkydart.anyonecanfish;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

import com.homebrewforlife.sharkydart.anyonecanfish.adapters.TackleBoxesRVAdapter;
import com.homebrewforlife.sharkydart.anyonecanfish.models.Fire_Lure;
import com.homebrewforlife.sharkydart.anyonecanfish.models.Fire_TackleBox;

import java.util.ArrayList;

public class TackleBoxesActivity extends AppCompatActivity {

    ArrayList<Fire_TackleBox> mTackleBoxArrayList;
    TackleBoxesRVAdapter mTackleBoxRVAdapter;
    RecyclerView mTackleBoxRV;
    public static final String LURES_ARRAYLIST = "lures-array-list";
    public static final String THE_TACKLEBOX = "this-is-a-string-for-the-tackleboxes";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tackle_boxes);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        if(savedInstanceState == null){
            mTackleBoxArrayList = new ArrayList<>();
        }else if(savedInstanceState.containsKey(MainActivity.TACKLE_BOXES_ARRAYLIST)){
            mTackleBoxArrayList = savedInstanceState.getParcelableArrayList(MainActivity.TACKLE_BOXES_ARRAYLIST);
        }

        Intent intent = getIntent();
        if (intent == null) {
            closeOnError();
        }else {
            mTackleBoxArrayList = intent.getParcelableArrayListExtra(MainActivity.TACKLE_BOXES_ARRAYLIST);
        }

        mTackleBoxRV = findViewById(R.id.rvTackleBoxes);
        assert mTackleBoxRV != null;
        setupRecyclerView(mTackleBoxRV);
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        mTackleBoxRVAdapter = new TackleBoxesRVAdapter(this, mTackleBoxArrayList);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(mTackleBoxRVAdapter);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(MainActivity.TACKLE_BOXES_ARRAYLIST, mTackleBoxArrayList);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mTackleBoxArrayList = savedInstanceState.getParcelableArrayList(MainActivity.TACKLE_BOXES_ARRAYLIST);
    }

    private void closeOnError() {
        finish();
        Toast.makeText(this, "Can't find Game Fish", Toast.LENGTH_SHORT).show();
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

}
