package repo;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;

public class MongoEntity extends AbstractMongoEntity {
    public MongoEntity() {
//        initDbConnection();
        super("mongodb://mongodb1:27017,mongodb2:27018,mongodb3:27019/?replicaSet=replica_set_single");
    }

    public MongoEntity(String uri) {
        super(uri);
    }

    public MongoClient getMongoClient() {
        return mongoClient;
    }

    @Override
    public void close() throws Exception {
        mongoClient.close();
    }
}
