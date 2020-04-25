package com.occ.ranking;

import com.occ.ranking.helpers.Param;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import picocli.CommandLine;

import java.util.Collections;

/**
	Application starts here
 */

@SpringBootApplication
public class RankingServiceApplication {

	public static void main(String[] args) {
		Param param = new Param();
		new CommandLine(param).parseArgs(args);
		SpringApplication app = new SpringApplication(RankingServiceApplication.class);
		app.setDefaultProperties(Collections
				.singletonMap("server.port", param.port));
		app.run(args);
	}

}
