package com.mycane.usermanagement;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import play.Logger;
import play.Play;

import javax.annotation.PreDestroy;

/**
 * Created by esfandiaramirrahimi on 2015-05-06.
 */
@Configuration
public class DBConfig {
    public static final String _mongoURI = Play.application().configuration().getString("mongodb.uri");
    public static final MongoClientURI mongoURI = new MongoClientURI(_mongoURI);

    @Bean
    public MongoClient mongoClient() {
        MongoClient mongo = null;
        try {
            mongo = new MongoClient(mongoURI);
            Logger.info("Connected to Database!");
        } catch (Exception e) {
            Logger.info("Unknown Host");
        }
        return mongo;
    }

    @Bean
    public Morphia morphia() {
        return new Morphia();
    }

    @Bean
    public Datastore datastore() {
        Datastore datastore = null;

        if (mongoClient() != null) {
            datastore = morphia().createDatastore(mongoClient(), mongoURI.getDatabase());
        }
        return datastore;
    }

    @PreDestroy
    public void closeConnection() {
        if (mongoClient() != null) {
            mongoClient().close();
        }
    }
}

