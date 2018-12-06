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

import com.homebrewforlife.sharkydart.anyonecanfish.adapters.GameFishRVAdapter;
import com.homebrewforlife.sharkydart.anyonecanfish.models.Fire_GameFish;

import java.util.ArrayList;

public class GameFishActivity extends AppCompatActivity {

    ArrayList<Fire_GameFish> mGameFishArrayList;
    GameFishRVAdapter mGameFishRVAdapter;
    RecyclerView mGameFishRV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_fish);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        if(savedInstanceState == null){
            mGameFishArrayList = new ArrayList<>();
        }else if(savedInstanceState.containsKey(MainActivity.GAME_FISH_ARRAYLIST)){
            mGameFishArrayList = savedInstanceState.getParcelableArrayList(MainActivity.GAME_FISH_ARRAYLIST);
        }

        Intent intent = getIntent();
        if (intent == null) {
            closeOnError();
        }else {
            mGameFishArrayList = intent.getParcelableArrayListExtra(MainActivity.GAME_FISH_ARRAYLIST);
        }

        mGameFishRV = findViewById(R.id.rvGameFish);
        assert mGameFishRV != null;
        setupRecyclerView(mGameFishRV);
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        mGameFishRVAdapter = new GameFishRVAdapter(this, mGameFishArrayList);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(mGameFishRVAdapter);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(MainActivity.GAME_FISH_ARRAYLIST, mGameFishArrayList);
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
