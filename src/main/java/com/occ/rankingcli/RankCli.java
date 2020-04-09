package com.occ.rankingcli;

import com.occ.rankingcli.commands.LS;
import com.occ.rankingcli.commands.Rank;
import lombok.extern.slf4j.Slf4j;
import picocli.CommandLine;

@Slf4j
@CommandLine.Command(subcommands = {LS.class, Rank.class})
class RankCli{

    public static void main(String[] args) {
        log.info("Inside main");
        RankCli app = new RankCli();
        new CommandLine(app).execute(args);
    }

}





























/*
        Unirest.setTimeouts(0, 0);
        HttpResponse<String> response = Unirest.post("http://localhost:8080/ranking")
            .field("file", new File("/Users/raj/Desktop/occ/OCC Take Home Coding names.txt"))
            .asString();

 */