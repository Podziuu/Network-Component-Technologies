package pl.lodz.p.edu.rest.model.user;

import org.bson.codecs.pojo.annotations.BsonDiscriminator;
import org.bson.types.ObjectId;

@BsonDiscriminator("Manager")
public class Manager extends User {
    public Manager(ObjectId id, String login, String password, String firstName, String lastName) {
        super(id, login, password, firstName, lastName);
        this.setRole(Role.MANAGER);
    }

    public Manager() {
    }
}
