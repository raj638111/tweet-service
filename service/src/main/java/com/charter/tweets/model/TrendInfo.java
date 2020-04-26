package com.charter.tweets.model;

import java.util.ArrayList;

public class TrendInfo {

    String time;
    ArrayList<TagNCount> countList;

    @Override
    public String toString() {
        return "time = " + time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTime() {
        return time;
    }

    public void setCountList(ArrayList<TagNCount> countList) {
        this.countList = countList;
    }

    public ArrayList<TagNCount> getCountList() {
        return countList;
    }
}

