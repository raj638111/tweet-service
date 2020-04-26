package com.occ.ranking.model;

import java.util.ArrayList;

public class TagInfo {

    String tag;
    ArrayList<TimeNCount> countList;

    @Override
    public String toString() {
        return "time = " + tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getTag() {
        return tag;
    }

    public void setCountList(ArrayList<TimeNCount> countList) {
        this.countList = countList;
    }

    public ArrayList<TimeNCount> getCountList() {
        return countList;
    }
}

