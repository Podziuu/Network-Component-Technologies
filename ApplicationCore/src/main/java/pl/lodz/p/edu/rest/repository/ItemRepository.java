package pl.lodz.p.edu.rest.repository;

import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.result.InsertOneResult;
import org.springframework.stereotype.Repository;
import org.springframework.web.context.annotation.ApplicationScope;
import pl.lodz.p.edu.rest.model.item.Item;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

@Repository
@ApplicationScope
public class ItemRepository extends AbstractMongoEntity {
    private final MongoCollection<Item> itemCollection;

    public ItemRepository() {
        initDbConnection();
        this.itemCollection = database.getCollection("items", Item.class);
    }

    public ObjectId addItem(Item item) {
        InsertOneResult result = itemCollection.insertOne(item);
        item.setId(result.getInsertedId().asObjectId().getValue());
        return result.getInsertedId().asObjectId().getValue();
    }

    public Item getItemById(String id) {
        ObjectId objectId = new ObjectId(id);
        return itemCollection.find(Filters.eq("_id", objectId)).first();
    }

    public List<Item> getItemsByBasePrice(int basePrice) {
        return itemCollection.find(Filters.eq("basePrice", basePrice)).into(new ArrayList<>());
    }

    public List<Item> getItemsByItemName(String itemName) {
        return itemCollection.find(Filters.eq("itemName", itemName)).into(new ArrayList<>());
    }

    public List<Item> getItemsByItemType(String itemType) {
        return itemCollection.find(Filters.eq("itemType", itemType)).into(new ArrayList<>());
    }

    public void updateItem(Item item) {
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
    public void close() throws Exception {
        mongoClient.close();
    }

    public List<Item> getAllItems() {
        return itemCollection.find().into(new ArrayList<>());
    }
}