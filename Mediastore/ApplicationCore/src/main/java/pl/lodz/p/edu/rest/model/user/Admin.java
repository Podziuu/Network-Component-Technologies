package pl.lodz.p.edu.rest.model.user;

import org.bson.codecs.pojo.annotations.BsonDiscriminator;
import org.bson.types.ObjectId;

@BsonDiscriminator("Admin")
public class Admin extends User {
    public Admin(ObjectId id, String login, String password, String firstName, String lastName) {
        super(id, login, password, firstName, lastName);
        this.setRole(Role.ADMIN);
    }

    public Admin() {
    }
}
