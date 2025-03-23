package repo;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.result.InsertOneResult;
import com.mongodb.client.result.UpdateResult;
import entities.user.RoleEnt;
import entities.user.UserEnt;
import model.user.Role;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Updates.combine;
import static com.mongodb.client.model.Updates.set;

@Repository
public class UserRepository extends AbstractMongoEntity {
    private final MongoCollection<UserEnt> userCollection;

    public UserRepository() {
        initDbConnection();
        userCollection = database.getCollection("users", UserEnt.class);
    }

    @Override
    public void close() {
        mongoClient.close();
    }

    public UserEnt save(UserEnt user) {
        InsertOneResult result = userCollection.insertOne(user);
        ObjectId insertedId = result.getInsertedId().asObjectId().getValue();
        return userCollection.find(Filters.eq("_id", insertedId)).first();
    }

    public UserEnt findById(String id) {
        return userCollection.find(Filters.eq("_id", new ObjectId(id))).first();
    }

    public List<UserEnt> findByRole(Role role) {
        return userCollection.find(Filters.eq("role", role)).into(new ArrayList<>());
    }

    public List<UserEnt> findByFirstName(String firstName) {
        return userCollection.find(Filters.regex("firstName", firstName, "i")).into(new ArrayList<>());
    }

    public List<UserEnt> findByRoleAndFirstName(Role role, String firstName) {
        Bson combinedFilter = Filters.and(
                Filters.eq("role", role),
                Filters.regex("firstName", firstName, "i")
        );
        return userCollection.find(combinedFilter).into(new ArrayList<>());
    }

    public List<UserEnt> findAll() {
        return userCollection.find().into(new ArrayList<>());
    }

    public UpdateResult update(String id, String firstName, String lastName) {
        return userCollection.updateOne(
                Filters.eq("_id", new ObjectId(id)),
                combine(
                        set("firstName", firstName),
                        set("lastName", lastName)
                )
        );
    }

    public UpdateResult activateUser(String id) {
        return userCollection.updateOne(
                Filters.eq("_id", new ObjectId(id)),
                set("active", true)
        );
    }

    public UpdateResult deactivateUser(String id) {
        return userCollection.updateOne(
                Filters.eq("_id", new ObjectId(id)),
                set("active", false)
        );
    }

    public boolean userExists(String login) {
        return userCollection.find(Filters.eq("login", login)).first() != null;
    }

    public UserEnt findByLogin(String login) {
        return userCollection.find(Filters.eq("login", login)).first();
    }

    public UpdateResult updatePassword(String login, String encodedNewPassword) {
        return userCollection.updateOne(
                Filters.eq("login", login),
                set("password", encodedNewPassword)
        );
    }
}
