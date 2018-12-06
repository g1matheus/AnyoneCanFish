package com.homebrewforlife.sharkydart.anyonecanfish.models;

import com.google.firebase.auth.FirebaseUser;

public class Fire_User {
    private Boolean mentor;
    private String name;
    private String uid;

    public Fire_User(){}

    public Fire_User(Boolean mentor, String name, String uid) {
        this.mentor = mentor;
        this.name = name;
        this.uid = uid;
    }
    public Fire_User(FirebaseUser in){
        this.mentor = false;
        this.name = in.getDisplayName();
        this.uid = in.getUid();
    }

    public Boolean getMentor() {
        return mentor;
    }

    public void setMentor(Boolean mentor) {
        this.mentor = mentor;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
