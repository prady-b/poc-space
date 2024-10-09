package com.prady.sample.mongodb.runner;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.Field;
import com.mongodb.client.model.search.SearchCount;

import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.mongodb.client.model.search.SearchOperator.exists;
import static com.mongodb.client.model.search.SearchOperator.text;
import static com.mongodb.client.model.search.SearchOptions.searchOptions;
import static com.mongodb.client.model.search.SearchPath.fieldPath;

/**
 * @author Pradeep Balakrishnan
 */
@Component
@Slf4j
public class SearchRunner implements CommandLineRunner {

    public static final String MOVIES_COLLECTION_NAME = "movies";
    public static final String INDEX_NAME = "default";
    public static final String TITLE_ATTRIBUTE_NAME = "title";

    @Autowired
    private MongoClient mongoClient;

    @Value("${spring.data.mongodb.database}")
    private String databaseName;

    @Override
    public void run(String... args) {

        MongoDatabase database = mongoClient.getDatabase(databaseName);

        findTopNMovies(database, 5);
        searchByTitle(database, "Batman", 10);
        searchByTitle(database, "Dark Knight", 5);
        searchByPath(database, "directors", "Nolan", 20);
    }

    @SuppressWarnings("unchecked")
    private void searchByTitle(MongoDatabase database, String titleName, int limit) {
        //@formatter:off
        List<Bson> pipeline = List.of(
            Aggregates.search(text(fieldPath(TITLE_ATTRIBUTE_NAME), titleName), searchOptions().index(INDEX_NAME).count(SearchCount.total())),
            Aggregates.addFields(new Field("meta", "$$SEARCH_META")),
            Aggregates.limit(limit)
        );
        //@formatter:on

        log.info("Executing search pipeline {}", pipeline);
        List<Document> results = database.getCollection(MOVIES_COLLECTION_NAME).aggregate(pipeline).into(new ArrayList<>());
        log.info("Results for search by title {} ", titleName);
        for (Document doc : results) {
            log.info(doc.getString(TITLE_ATTRIBUTE_NAME));
            log.info("Meta {}", doc.get("meta"));
        }
    }

    private void findTopNMovies(MongoDatabase database, int limit) {
        //@formatter:off
        List<Bson> pipeline = Arrays.asList(
            Aggregates.search(exists(fieldPath("_id")), searchOptions().index(INDEX_NAME)),
            //Aggregates.project(new Document("_id", 0).append("title", 1).append("released", 1)),
            Aggregates.limit(limit)
        );
        //@formatter:on
        log.info("Executing Find Top pipeline {}", pipeline);
        List<Document> results = database.getCollection(MOVIES_COLLECTION_NAME).aggregate(pipeline).into(new ArrayList<>());
        log.info("Find first 5 movies");
        for (Document doc : results) {
            log.info(doc.getString(TITLE_ATTRIBUTE_NAME));
        }
    }

    private void searchByPath(MongoDatabase database, String path, String query, int limit) {
        //@formatter:off
        List<Bson> pipeline = List.of(
                Aggregates.search(text(fieldPath(path), query), searchOptions().index(INDEX_NAME)),
                Aggregates.limit(limit),
                Aggregates.sort(new Document().append("released",-1))
        );
        //@formatter:on

        log.info("Executing search by path pipeline {}", pipeline);
        List<Document> results = database.getCollection(MOVIES_COLLECTION_NAME).aggregate(pipeline).into(new ArrayList<>());
        log.info("Results for search by path {} query {} ", path, query);
        for (Document doc : results) {
            log.info("{} released on {}", doc.getString(TITLE_ATTRIBUTE_NAME), doc.get("released"));
        }
    }
}
