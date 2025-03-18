package pl.lodz.p.edu.rest.repository;

import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.result.InsertOneResult;
import com.mongodb.client.result.UpdateResult;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Repository;
import pl.lodz.p.edu.rest.model.user.Role;
import pl.lodz.p.edu.rest.model.user.User;

import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Updates.combine;
import static com.mongodb.client.model.Updates.set;

@Repository
public class UserRepository extends AbstractMongoEntity {
    private MongoCollection<User> userCollection;

    public UserRepository() {
        initDbConnection();
        userCollection = database.getCollection("users", User.class);
    }

    @Override
    public void close() throws Exception {
        mongoClient.close();
    }

    public ObjectId save(User user) {
        InsertOneResult result = userCollection.insertOne(user);
        return result.getInsertedId().asObjectId().getValue();
    }
    
    public User findById(String id) {
        ObjectId objectId = new ObjectId(id);
        return userCollection.find(Filters.eq("_id", objectId)).first();
    }

    public List<User> findByRole(Role role) {
        return userCollection.find(Filters.eq("role", role)).into(new ArrayList<>());
    }

    public List<User> findByFirstName(String firstName) {
        Bson filter = Filters.regex("firstName", firstName, "i");
        return userCollection.find(filter).into(new ArrayList<>());
    }

    public List<User> findByRoleAndFirstName(Role role, String firstName) {
        Bson roleFilter = Filters.eq("role", role);
        Bson firstNameFilter = Filters.regex("firstName", firstName, "i");
        Bson combinedFilter = Filters.and(roleFilter, firstNameFilter);
        return userCollection.find(combinedFilter).into(new ArrayList<>());
    }

    public List<User> findAll() {
        return userCollection.find().into(new ArrayList<>());
    }

    public UpdateResult update(String id, String firstName, String lastName) {
        ObjectId objectId = new ObjectId(id);
        return userCollection.updateOne(
                Filters.eq("_id", objectId),
                combine(
                        set("firstName", firstName),
                        set("lastName", lastName)
                )
        );
    }

    public UpdateResult activateUser(String id) {
        ObjectId objectId = new ObjectId(id);
        return userCollection.updateOne(
                Filters.eq("_id", objectId),
                set("active", true)
        );
    }

    public UpdateResult deactivateUser(String id) {
        ObjectId objectId = new ObjectId(id);
        return userCollection.updateOne(
                Filters.eq("_id", objectId),
                set("active", false)
        );
    }

    public boolean userExists(String login) {
        return userCollection.find(Filters.eq("login", login)).first() != null;
    }

    public User findByLogin(String login) {
        return userCollection.find(Filters.eq("login", login)).first();
    }

    public UpdateResult updatePassword(String login, String encodedNewPassword) {
        return userCollection.updateOne(
                Filters.eq("login", login),
                set("password", encodedNewPassword)
        );
    }
}
