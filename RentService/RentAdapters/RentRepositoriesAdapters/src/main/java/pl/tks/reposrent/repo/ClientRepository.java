package pl.tks.repos.repo;

import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.result.InsertOneResult;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import pl.tks.repos.config.MongoProperties;
import pl.tks.repos.entities.ClientEnt;

import java.util.ArrayList;
import java.util.List;

@Repository
public class ClientRepository extends AbstractMongoEntity {
    private final MongoCollection<ClientEnt> clientCollection;

    @Autowired
    public ClientRepository(@Qualifier("mongo-pl.tks.repos.config.MongoProperties") MongoProperties properties) {
        super(properties);
        this.clientCollection = database.getCollection("clients", ClientEnt.class);
    }

    // Dodanie nowego klienta
    public ClientEnt addClient(ClientEnt client) {
        InsertOneResult result = clientCollection.insertOne(client);
        ObjectId insertedId = result.getInsertedId().asObjectId().getValue();
        client.setId(insertedId);
        return clientCollection.find(Filters.eq("_id", insertedId)).first();
    }

    // Pobranie klienta po ID
    public ClientEnt getClient(String id) {
        ObjectId objectId = new ObjectId(id);
        return clientCollection.find(Filters.eq("_id", objectId)).first();
    }

    // Zaktualizowanie danych klienta
    public void updateClient(ClientEnt client) {
        BasicDBObject object = new BasicDBObject();
        object.put("_id", client.getId());
        clientCollection.replaceOne(object, client);
    }

    // Znalezienie wszystkich klientów
    public List<ClientEnt> getAllClients() {
        return clientCollection.find().into(new ArrayList<>());
    }

    // Znalezienie klientów po imieniu
    public List<ClientEnt> findClientsByFirstName(String firstName) {
        return clientCollection.find(Filters.eq("firstName", firstName)).into(new ArrayList<>());
    }

    // Znalezienie klienta po loginie (zakładając, że masz pole 'login' w ClientEnt)
    public ClientEnt findClientsByLogin(String login) {
        return clientCollection.find(Filters.eq("login", login)).first();
    }

    // Usunięcie klienta
    public void deleteClient(String clientId) {
        ObjectId objectId = new ObjectId(clientId);
        clientCollection.deleteOne(Filters.eq("_id", objectId));
    }

    // Usunięcie wszystkich klientów
    public void deleteAllClients() {
        clientCollection.drop();
    }

    @Override
    public void close() {
        mongoClient.close();
    }
}
