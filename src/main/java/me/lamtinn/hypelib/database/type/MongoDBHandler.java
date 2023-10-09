package me.lamtinn.hypelib.database.type;

import com.mongodb.ServerApi;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.ServerApiVersion;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import me.lamtinn.hypelib.database.DatabaseCredentials;
import me.lamtinn.hypelib.database.DatabaseType;
import me.lamtinn.hypelib.database.connector.impl.MongoConnector;
import me.lamtinn.hypelib.utils.AdventureUtils;
import org.bson.Document;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

public abstract class MongoDBHandler extends MongoConnector {

    private MongoDatabase db;

    public MongoDBHandler(@NotNull DatabaseCredentials credentials) {
        super("MongoDB", credentials);
    }

    @Override
    public void init() throws Exception {
        if (getCredentials().getType() != DatabaseType.MONGODB) {
            return;
        }

        AdventureUtils.consoleMessage("{prefix} <gray>Connecting the database with MongoDB...");

        final String str = this.getCredentials().getMongoConnectString();
        if (str.isEmpty()) {
            throw new RuntimeException("MongoDB connection string is empty. Please fill it in config.yml!");
        }

        ServerApi serverApi = ServerApi.builder()
                .version(ServerApiVersion.V1)
                .build();

        final MongoClientSettings settings = MongoClientSettings.builder()
                .applyConnectionString(new ConnectionString(str))
                .serverApi(serverApi)
                .applicationName("hypelib_mongo")
                .build();

        this.mongo = MongoClients.create(settings);

        this.db = mongo.getDatabase(getCredentials().getDatabase());
        this.db.runCommand(new Document("ping", 1));

        AdventureUtils.consoleMessage("{prefix} <green>Successfully connected to MySQL database!");
    }

    public @NotNull MongoDatabase getMongo() {
        return this.db;
    }

    public @Nullable MongoCollection<Document> getCollection(@NotNull final String name) {
        return this.db.getCollection(name);
    }

    public boolean isCollectionExists(@NotNull final String name) {
        return this.db.listCollectionNames().into(new ArrayList<>()).contains(name);
    }
}
