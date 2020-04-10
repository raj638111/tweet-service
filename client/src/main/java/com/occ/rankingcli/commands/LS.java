package com.occ.rankingcli.commands;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import lombok.extern.slf4j.Slf4j;
import picocli.CommandLine;

import java.io.File;
import java.util.concurrent.Callable;

@Slf4j
@CommandLine.Command(name = "ls",
    description = "'ls' command lists all the Ranking service implementation/algorithm available")
public class LS implements Callable {

    // Example: http://localhost:8080
    @CommandLine.Option(names = "--host", required = true,
            description = "Rest service location") String host;


    public Integer call() throws UnirestException {
        Unirest.setTimeouts(0, 0);
        String url = host + "/ls";
        log.info("\nGetting list of available Ranking service implementation from "
            + url + "...");
        HttpResponse<String> response = Unirest.get(url)
                .asString();
        log.info("\n" + response.getBody());
        log.info("\n Use ^ any one of the service in --service option in 'rank' command \n");
        return 23;
    }
}
