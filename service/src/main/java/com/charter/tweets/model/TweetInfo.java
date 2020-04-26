package com.charter.tweets.model;

public class TweetInfo {
    public String tweet;
    public String hashtag;
    public String time;

    @Override
    public String toString() {
        return "tweet = " + tweet + ", hashtag = " + hashtag + ", time = " + time;
    }
}
