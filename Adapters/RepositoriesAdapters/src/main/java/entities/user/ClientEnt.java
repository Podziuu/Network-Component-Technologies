package entities.user;

import org.bson.codecs.pojo.annotations.BsonDiscriminator;
import org.bson.codecs.pojo.annotations.BsonProperty;
import org.bson.types.ObjectId;

@BsonDiscriminator("Client")
public class ClientEnt extends UserEnt {

    @BsonProperty("clientType")
    private ClientType clientType;

    public ClientEnt(ObjectId id,
                     String login,
                     String password,
                     String firstName,
                     String lastName,
                     @BsonProperty("clientType") ClientType clientType) {
        super(id, login, password, firstName, lastName);
        this.setRole(Role.CLIENT);
        this.clientType = clientType;
    }

    public ClientEnt() {
    }

    public ClientType getClientType() {
        return clientType;
    }

    public void setClientType(ClientType clientType) {
        this.clientType = clientType;
    }
}
