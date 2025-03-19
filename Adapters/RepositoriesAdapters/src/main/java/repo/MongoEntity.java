package repo;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;

public class MongoEntity extends AbstractMongoEntity {
    public MongoEntity() {
        initDbConnection();
    }

    public MongoDatabase getDatabase() {
        return database;
    }

    public MongoClient getMongoClient() {
        return mongoClient;
    }

    @Override
    public void close() throws Exception {
        mongoClient.close();
    }
}