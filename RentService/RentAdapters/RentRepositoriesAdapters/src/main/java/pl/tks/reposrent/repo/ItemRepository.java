package pl.tks.reposrent.repo;

import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.result.InsertOneResult;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import org.springframework.web.context.annotation.ApplicationScope;
import pl.tks.reposrent.config.MongoProperties;
import pl.tks.reposrent.entities.item.ItemEnt;

import java.util.ArrayList;
import java.util.List;

@Repository
@ApplicationScope
public class ItemRepository extends AbstractMongoEntity {
    private final MongoCollection<ItemEnt> itemCollection;

    @Autowired
    public ItemRepository(MongoProperties properties) {
        super(properties);
        this.itemCollection = database.getCollection("items", ItemEnt.class);
    }

//    public ItemRepository(String connectionString, String dbName) {
//        super(connectionString, dbName);
//        this.itemCollection = database.getCollection("items", ItemEnt.class);
//    }

    public ItemEnt addItem(ItemEnt item) {
        InsertOneResult result = itemCollection.insertOne(item);
        ObjectId insertedId = result.getInsertedId().asObjectId().getValue();
        item.setId(insertedId);
        return itemCollection.find(Filters.eq("_id", insertedId)).first();
    }

    public ItemEnt getItemById(String id) {
        ObjectId objectId = new ObjectId(id);
        return itemCollection.find(Filters.eq("_id", objectId)).first();
    }

    public List<ItemEnt> getItemsByBasePrice(int basePrice) {
        return itemCollection.find(Filters.eq("basePrice", basePrice)).into(new ArrayList<>());
    }

    public List<ItemEnt> getItemsByItemName(String itemName) {
        return itemCollection.find(Filters.eq("itemName", itemName)).into(new ArrayList<>());
    }

    public List<ItemEnt> getItemsByItemType(String itemType) {
        return itemCollection.find(Filters.eq("itemType", itemType)).into(new ArrayList<>());
    }

    public void updateItem(ItemEnt item) {
        BasicDBObject object = new BasicDBObject();
        object.put("_id", item.getId());
        itemCollection.replaceOne(object, item);
    }

    public void removeItem(ObjectId id) {
        BasicDBObject object = new BasicDBObject();
        object.put("_id", id);
        itemCollection.deleteOne(object);
    }

    @Override
    public void close() {
        mongoClient.close();
    }

    public void deleteAllItems() {
        itemCollection.drop();
    }

    public List<ItemEnt> getAllItems() {
        return itemCollection.find().into(new ArrayList<>());
    }
}
