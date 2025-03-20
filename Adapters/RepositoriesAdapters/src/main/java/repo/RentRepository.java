package repo;

import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.result.InsertOneResult;
import entities.RentEnt;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class RentRepository extends AbstractMongoEntity {
    private final MongoCollection<RentEnt> rentCollection;

    public RentRepository() {
        initDbConnection();
        this.rentCollection = database.getCollection("rents", RentEnt.class);
    }

    public ObjectId addRent(RentEnt rent) {
        InsertOneResult result = rentCollection.insertOne(rent);
        rent.setId(result.getInsertedId().asObjectId().getValue());
        return result.getInsertedId().asObjectId().getValue();
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
}
