package entities.user;

import org.bson.codecs.pojo.annotations.BsonDiscriminator;
import org.bson.types.ObjectId;

@BsonDiscriminator("Admin")
public class AdminEnt extends UserEnt {
    public AdminEnt(String id, String login, String password, String firstName, String lastName) {
        super(id, login, password, firstName, lastName);
        this.setRole(RoleEnt.ADMIN);
    }

    public AdminEnt() {
    }
}
