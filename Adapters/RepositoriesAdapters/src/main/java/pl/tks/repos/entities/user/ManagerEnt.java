package pl.tks.repos.entities.user;

import org.bson.codecs.pojo.annotations.BsonDiscriminator;
import org.bson.types.ObjectId;

@BsonDiscriminator("Manager")
public class ManagerEnt extends UserEnt {

    public ManagerEnt(ObjectId id, String login, String password, String firstName, String lastName) {
        super(id, login, password, firstName, lastName);
        this.setRole(RoleEnt.MANAGER);
    }

    public ManagerEnt() {
    }
}
