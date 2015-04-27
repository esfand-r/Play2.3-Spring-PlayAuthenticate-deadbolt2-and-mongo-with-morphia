package com.mycane.usermanagement;

import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import de.flapdoodle.embed.mongo.Command;
import de.flapdoodle.embed.mongo.MongodExecutable;
import de.flapdoodle.embed.mongo.MongodProcess;
import de.flapdoodle.embed.mongo.MongodStarter;
import de.flapdoodle.embed.mongo.config.ArtifactStoreBuilder;
import de.flapdoodle.embed.mongo.config.MongodConfigBuilder;
import de.flapdoodle.embed.mongo.config.Net;
import de.flapdoodle.embed.mongo.config.RuntimeConfigBuilder;
import de.flapdoodle.embed.mongo.distribution.Version;
import de.flapdoodle.embed.process.config.io.ProcessOutput;
import de.flapdoodle.embed.process.io.IStreamProcessor;
import de.flapdoodle.embed.process.io.NullProcessor;
import de.flapdoodle.embed.process.runtime.Network;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import play.Logger;

import java.io.IOException;

/**
 * Created by esfandiaramirrahimi on 2015-05-10.
 */
@Configuration
@ComponentScan(basePackages = {"com.mycane.usermanagement.dao", "com.mycane.usermanagement.service",
        "com.mycane.usermanagement.security", "com.mycane.usermanagement.strategy"})
@Import(MorphiaConfig.class)
public class TestConfig {
    public static final MongoClientURI mongoURI = new MongoClientURI("mongodb://127.0.0.1:12345/ITTest");
    private static final String PROCESS_ADDRESS = "localhost";
    private static final int PROCESS_PORT = 12345;

    private static MongodExecutable mongodExecutable = null;
    private static MongodProcess mongodProcess = null;
    private static Mongo mongoClient = null;

    @Bean
    public MongoClient mongoClient() throws IOException {
        IStreamProcessor stream = new NullProcessor();
        MongodStarter runtime = MongodStarter.getInstance(new RuntimeConfigBuilder()
                .defaults(Command.MongoD)
                .processOutput(new ProcessOutput(stream, stream, stream))
                .artifactStore(new ArtifactStoreBuilder()
                        .defaults(Command.MongoD)
                        .executableNaming(new MongoIntegrationTestBase.BasicExecutableNaming())
                        .build())
                .build());
        mongodExecutable = runtime.prepare(new MongodConfigBuilder()
                .version(Version.Main.PRODUCTION)
                .net(new Net(PROCESS_PORT, Network.localhostIsIPv6()))
                .build());

        mongodProcess = mongodExecutable.start();
        mongoClient = new MongoClient(PROCESS_ADDRESS, PROCESS_PORT);
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
    public Datastore datastore() throws IOException {
        Datastore datastore = null;

        if (mongoClient() != null) {
            datastore = morphia().createDatastore(mongoClient(), mongoURI.getDatabase());
        }
        return datastore;
    }
}
