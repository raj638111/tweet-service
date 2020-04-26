package com.charter.tweets.model;

public class TimeNCount {
    String time;
    String count;

    public void setCount(String count) {
        this.count = count;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getCount() {
        return count;
    }

    public String getTime() {
        return time;
    }

    @Override
    public String toString() {
        return "tag = " + time + ", count = " + count;
    }
}
