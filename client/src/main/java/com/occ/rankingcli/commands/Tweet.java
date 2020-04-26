package com.occ.rankingcli.commands;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import lombok.extern.slf4j.Slf4j;
import picocli.CommandLine;

import java.io.File;
import java.util.concurrent.Callable;

/*
    This command provides rank for the given file
    submitted by the user.
 */


@Slf4j
@CommandLine.Command(name = "rank",
        description = "'rank' command computes rank for a given file")
public class Tweet implements Callable {

    @CommandLine.Option(names = "--host", required = true,
        description = "Reset service host name & port" +
                "\nExample: http://localhost:8080")
    String host;

    @CommandLine.Option(names = "--file", required = true,
            description = "Absolute path of the file (for which rank needs to be calculated)" +
            "\nData format: \"fname1 lname1\",\"fname2 lname2\",...")
    String file;

    @CommandLine.Option(names = "--service", required = true,
        description = "Ranking Service to be used to calculate rank." +
            " Run 'ls' command to get the list of Ranking Service available")
    String service;

    @CommandLine.Option(names = "--nameselect", required = false,
        defaultValue = "FIRST_NAME",
        description = "Should we sort only the first name, last name or both." +
            " Available values are FIRST_NAME, LAST_NAME, BOTH")
    String nameselect;

    @CommandLine.Option(names = "--descending", required = false,
        defaultValue = "false",
        description = "Should we sort the name in ascending or descending order")
    Boolean descending;

    public Integer call() throws UnirestException {
        Unirest.setTimeouts(0, 0);
        String url = host + "/rank";
        log.info("\nGetting rank for file " + file + " from " + url + "...");
        HttpResponse<String> response = Unirest.post(url)
            .field("file", new File(this.file))
            .field("service", this.service)
            .field("nameselect", this.nameselect)
            .field("descending", this.descending)
            .asString();
        log.info("\nRank for file is " + response.getBody() + "\n");
        return response.getStatus();
    }

}
