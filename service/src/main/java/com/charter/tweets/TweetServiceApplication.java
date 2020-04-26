package com.charter.tweets;

import com.charter.tweets.helpers.Param;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import picocli.CommandLine;

import java.util.Collections;

/**
	Application starts here
 */

@SpringBootApplication
public class TweetServiceApplication {

	public static void main(String[] args) {
		Param param = new Param();
		new CommandLine(param).parseArgs(args);
		SpringApplication app = new SpringApplication(TweetServiceApplication.class);
		app.setDefaultProperties(Collections
				.singletonMap("server.port", param.port));
		app.run(args);
	}

}
