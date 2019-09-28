package com.virtualidentity.clearcontentfulspace;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import static org.slf4j.LoggerFactory.*;

@SpringBootApplication
public class ClearContentfulSpaceApplication implements CommandLineRunner {

	private static Logger LOG = getLogger(ClearContentfulSpaceApplication.class);


	public static void main(String[] args) {
		SpringApplication.run(ClearContentfulSpaceApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		LOG.info("Parsing arguments ... {}, {}", args, args.length);
		for (int i=0; i<args.length; i++) {
			LOG.info(i + ":" +args[i] + ".");
		}
	}
}
