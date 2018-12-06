package com.homebrewforlife.sharkydart.anyonecanfish.AsyncTask;

import com.homebrewforlife.sharkydart.anyonecanfish.models.Fire_DataType;
import com.homebrewforlife.sharkydart.anyonecanfish.models.Fire_Lure;

import java.util.ArrayList;

public interface DataGrabListener{
    void onComplete(ArrayList<Fire_Lure> output);
}

