package com.charter.tweets.helpers;

import picocli.CommandLine;

/**
 * Arguments passed by user
 */
public class Param {

    @CommandLine.Option(names = "--port",
        description = "Port in which the service will listen")
    public String port = "8080";
}
