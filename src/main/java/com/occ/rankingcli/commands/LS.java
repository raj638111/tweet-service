package com.occ.rankingcli.commands;

import lombok.extern.slf4j.Slf4j;
import picocli.CommandLine;

import java.util.concurrent.Callable;

@Slf4j
@CommandLine.Command(name = "ls", description = "Lists all the Ranking services available")
public class LS implements Callable {
    @CommandLine.Option(names = "--host") String host;

    @Override public Integer call() {
        log.info("List command");
        return 23;
    }
}
