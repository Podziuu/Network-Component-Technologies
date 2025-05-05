package pl.tks.reposrent.repo;

import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.result.InsertOneResult;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import pl.tks.reposrent.config.MongoProperties;
import pl.tks.reposrent.entities.RentEnt;

import java.util.ArrayList;
import java.util.List;

@Repository
public class RentRepository extends AbstractMongoEntity {
    private final MongoCollection<RentEnt> rentCollection;

    @Autowired
    public RentRepository(@Qualifier("mongo-pl.tks.reposrent.config.MongoProperties") MongoProperties properties) {
        super(properties);
        this.rentCollection = database.getCollection("rents", RentEnt.class);
    }

//    public RentRepository(String connectionString, String dbName) {
//        super(connectionString, dbName);
//        this.rentCollection = database.getCollection("rents", RentEnt.class);
//    }

    public RentEnt addRent(RentEnt rent) {
        InsertOneResult result = rentCollection.insertOne(rent);
        ObjectId insertedId = result.getInsertedId().asObjectId().getValue();
        rent.setId(insertedId);
        return rentCollection.find(Filters.eq("_id", insertedId)).first();
    }

    public RentEnt getRent(String id) {
        ObjectId objectId = new ObjectId(id);
        return rentCollection.find(Filters.eq("_id", objectId)).first();
    }

    public void updateRent(RentEnt rent) {
        BasicDBObject object = new BasicDBObject();
        object.put("_id", rent.getId());
        rentCollection.replaceOne(object, rent);
    }

    public List<RentEnt> findActiveRents() {
        return rentCollection.find(Filters.eq("endTime", null)).into(new ArrayList<>());
    }

    public List<RentEnt> findInactiveRents() {
        return rentCollection.find(Filters.ne("endTime", null)).into(new ArrayList<>());
    }

    public List<RentEnt> findRentsByItemId(String itemId) {
        ObjectId objectId = new ObjectId(itemId);
        return rentCollection.find(Filters.eq("item._id", objectId)).into(new ArrayList<>());
    }

    public List<RentEnt> findActiveRentsByItemId(String itemId) {
        ObjectId objectId = new ObjectId(itemId);
        return rentCollection.find(Filters.and(
                Filters.eq("item._id", objectId),
                Filters.eq("endTime", null)
        )).into(new ArrayList<>());
    }

    public List<RentEnt> findInactiveRentsByItemId(String itemId) {
        ObjectId objectId = new ObjectId(itemId);
        return rentCollection.find(Filters.and(
                Filters.eq("item._id", objectId),
                Filters.ne("endTime", null)
        )).into(new ArrayList<>());
    }

    public List<RentEnt> findRentsByClientId(String clientId) {
        ObjectId objectId = new ObjectId(clientId);
        return rentCollection.find(Filters.eq("client._id", objectId)).into(new ArrayList<>());
    }

    public List<RentEnt> findActiveRentsByClientId(String clientId) {
        ObjectId objectId = new ObjectId(clientId);
        return rentCollection.find(Filters.and(
                Filters.eq("client._id", objectId),
                Filters.eq("endTime", null)
        )).into(new ArrayList<>());
    }

    public List<RentEnt> findInactiveRentsByClientId(String clientId) {
        ObjectId objectId = new ObjectId(clientId);
        return rentCollection.find(Filters.and(
                Filters.eq("client._id", objectId),
                Filters.ne("endTime", null)
        )).into(new ArrayList<>());
    }

    @Override
    public void close() {
        mongoClient.close();
    }

    public void deleteAll() {
        rentCollection.drop();
    }
}
