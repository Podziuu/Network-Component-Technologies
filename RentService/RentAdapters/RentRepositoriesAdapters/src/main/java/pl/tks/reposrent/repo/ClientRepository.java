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
import pl.tks.reposrent.entities.ClientEnt;

import java.util.ArrayList;
import java.util.List;

@Repository
public class ClientRepository extends AbstractMongoEntity {
    private final MongoCollection<ClientEnt> clientCollection;

    @Autowired
    public ClientRepository(@Qualifier("mongo-pl.tks.reposrent.config.MongoProperties") MongoProperties properties) {
        super(properties);
        this.clientCollection = database.getCollection("clients", ClientEnt.class);
    }

    public ClientEnt addClient(ClientEnt client) {
        InsertOneResult result = clientCollection.insertOne(client);
        ObjectId insertedId = result.getInsertedId().asObjectId().getValue();
        client.setId(insertedId);
        return clientCollection.find(Filters.eq("_id", insertedId)).first();
    }

    public ClientEnt getClient(String id) {
        ObjectId objectId = new ObjectId(id);
        return clientCollection.find(Filters.eq("_id", objectId)).first();
    }

    public void updateClient(ClientEnt client) {
        BasicDBObject object = new BasicDBObject();
        object.put("_id", client.getId());
        clientCollection.replaceOne(object, client);
    }

    public List<ClientEnt> getAllClients() {
        return clientCollection.find().into(new ArrayList<>());
    }

    public List<ClientEnt> findClientsByFirstName(String firstName) {
        return clientCollection.find(Filters.eq("firstName", firstName)).into(new ArrayList<>());
    }

    public void deleteClient(String clientId) {
        ObjectId objectId = new ObjectId(clientId);
        clientCollection.deleteOne(Filters.eq("_id", objectId));
    }

    public void deleteAllClients() {
        clientCollection.drop();
    }

    @Override
    public void close() {
        mongoClient.close();
    }
}
