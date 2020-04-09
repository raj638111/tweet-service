package com.occ.rankingservice;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import java.io.File;

public class RankCli {
    public static void main(String[] args) throws UnirestException {
        Unirest.setTimeouts(0, 0);
        HttpResponse<String> response = Unirest.post("http://localhost:8080/ranking")
            .field("file", new File("/Users/raj/Desktop/occ/OCC Take Home Coding names.txt"))
            .asString();
    }
}
