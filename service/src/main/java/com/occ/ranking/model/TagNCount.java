package com.occ.ranking.model;

public class TagNCount {
    String tag;
    String count;

    public void setCount(String count) {
        this.count = count;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getCount() {
        return count;
    }

    public String getTag() {
        return tag;
    }

    @Override
    public String toString() {
        return "tag = " + tag + ", count = " + count;
    }
}
