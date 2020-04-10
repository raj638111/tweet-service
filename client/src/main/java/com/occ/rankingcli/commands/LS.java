package com.occ.rankingcli.commands;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import javafx.util.Pair;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import picocli.CommandLine;
import java.util.concurrent.Callable;


/*
    This command gets the list of available Ranking Service
    implementation supported by the web service
 */

@Slf4j
@CommandLine.Command(name = "ls",
    description = "'ls' command lists all the Ranking service implementation/algorithm available")
public class LS implements Callable {

    @CommandLine.Option(names = "--host", required = true,
            description = "Reset service host name & port" +
                    "\nExample: http://localhost:8080")
    String host;


    public Integer call() throws UnirestException {
        Unirest.setTimeouts(10000, 10000);
        String url = host + "/ls";
        log.info("\nGetting list of available Ranking service implementation from "
            + url + "...");
        HttpResponse<JsonNode> response = Unirest.get(url).asJson();
        response.getBody().getArray().forEach( x -> {
            JSONObject obj = (JSONObject)x;
            log.info("\n== Service: " + obj.get("serviceName").toString() + " ==");
            log.info("\n" + obj.get("description").toString());
        });
        log.info("\n**USE ^ any one one of the service as --service " +
                "<service> in 'rank' command \n");
        return response.getStatus();
    }
}
