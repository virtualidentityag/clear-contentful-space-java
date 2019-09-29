package com.virtualidentity.clearcontentfulspace;

import com.contentful.java.cma.CMAClient;
import com.contentful.java.cma.ModuleEntries;
import com.contentful.java.cma.model.CMAArray;
import com.contentful.java.cma.model.CMAEntry;
import com.contentful.java.cma.model.CMAHttpException;
import org.apache.commons.cli.*;
import org.slf4j.Logger;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

import static org.slf4j.LoggerFactory.getLogger;

@SpringBootApplication
public class ClearContentfulSpaceApplication implements CommandLineRunner {

    private static Logger LOG = getLogger(ClearContentfulSpaceApplication.class);

    private String space;
    private String environment;
    private String token;
    private String contentType;
    private boolean deleteAll;

    public static void main(String[] args) {
        SpringApplication.run(ClearContentfulSpaceApplication.class, args);
    }

    /**
     * examples:
     * token:CFPAT-zdtYIhtI-O2W9-ift6KJ-cP7DP6gVmcCyaEySAcvntQ
     * space id: f7lhvowaprbt
     * environemnt: dev
     * content_type: news
     *
     * @param args
     * @throws Exception
     */
    @Override
    public void run(String... args) throws Exception {

        parseOptions(args);

        final CMAClient client =
                new CMAClient
                        .Builder()
                        .setAccessToken(token)
                        .setSpaceId(space)
                        .setEnvironmentId(environment)
                        .build();
        ModuleEntries moduleEntries = client.entries();
        CMAArray<CMAEntry> array;
        int numberOfEntries = 0;

        try {
            if (deleteAll) {
                array = moduleEntries.fetchAll();
            } else {
                Map<String, String> query = new HashMap<>();
                query.put("content_type", contentType);
                array = moduleEntries.fetchAll(query);
            }

            numberOfEntries = array.getTotal();

            array.getItems().forEach(entry -> {
                deleteEntry(moduleEntries, entry);
            });
        } catch (CMAHttpException e) {
            LOG.error("Contentful error: {}", e.getErrorBody());
        }

        LOG.info("Deleted {} entries.", numberOfEntries);
        System.exit(0);
    }

    private void deleteEntry(ModuleEntries moduleEntries, CMAEntry entry) {
        try {
            LOG.info("Deleting entry: {}", entry.getId());
            if (entry.isPublished()) {
                moduleEntries.unPublish(entry);
            }
            moduleEntries.delete(entry);
        } catch (Exception e) {
            LOG.error("Error deleting entry {}", entry.getId());
        }
    }

    private void parseOptions(String[] args) {
        Options options = new Options();
        options.addOption("space", true, "The contentful space id");
        options.addOption("env", true, "The contentful environment name");
        options.addOption("token", true, "The contentful access token");
        options.addOption("type", true, "Delete all entries of this content type");
        options.addOption("all", false, "Delete all entries");
        CommandLineParser parser = new DefaultParser();
        try {
            CommandLine commandLine = parser.parse(options, args);
            if (commandLine.hasOption("space")) {
                space = commandLine.getOptionValue("space");
            }
            if (commandLine.hasOption("env")) {
                environment = commandLine.getOptionValue("env");
            }
            if (commandLine.hasOption("token")) {
                token = commandLine.getOptionValue("token");
            }
            if (commandLine.hasOption("type")) {
                contentType = commandLine.getOptionValue("type");
            }
            deleteAll = commandLine.hasOption("all");
        } catch (ParseException e) {
            LOG.error("Parameters could not be parsed.");
            System.exit(1);
        }

        if (StringUtils.isEmpty(space) || StringUtils.isEmpty(environment) || StringUtils.isEmpty(token)) {
            LOG.error("Parameters missing.");
            System.exit(1);
        }

        if (!deleteAll && StringUtils.isEmpty(contentType)) {
            LOG.error("No content type given.");
            System.exit(1);
		}
	}
}
