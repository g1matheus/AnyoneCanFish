package com.homebrewforlife.sharkydart.anyonecanfish.models;

import com.google.firebase.firestore.Exclude;

public class Fire_DataType {
    private String uid;

    public Fire_DataType() {
        this.uid = "";
    }

    public Fire_DataType(String uid) {
        this.uid = uid;
    }

    @Exclude
    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    @Exclude
    public String getQuickDescription(){
        return "Base Type - uid: " + this.uid;
    }
}
