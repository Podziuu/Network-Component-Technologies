package pl.tks.repos.entities.user;

import org.bson.codecs.pojo.annotations.BsonDiscriminator;
import org.bson.codecs.pojo.annotations.BsonProperty;
import org.bson.types.ObjectId;

@BsonDiscriminator("Client")
public class ClientEnt extends UserEnt {

    @BsonProperty("clientType")
    private ClientTypeEnt clientType;

    public ClientEnt(ObjectId id,
                     String login,
                     String password,
                     String firstName,
                     String lastName,
                     @BsonProperty("clientType") ClientTypeEnt clientType) {
        super(id, login, password, firstName, lastName);
        this.setRole(RoleEnt.CLIENT);
        this.clientType = clientType;
    }

    public ClientEnt() {
    }

    public ClientTypeEnt getClientType() {
        return clientType;
    }

    public void setClientType(ClientTypeEnt clientType) {
        this.clientType = clientType;
    }
}
