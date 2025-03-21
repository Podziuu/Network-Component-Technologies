package mappers;

import entities.user.*;
import model.user.*;

import org.bson.types.ObjectId;

import java.util.List;
import java.util.stream.Collectors;

public class UserMapper {

//    public static List<UserDTO> toDTO(List<User> users) {
//        return users.stream()
//                .map(UserMapper::convertToDTO)
//                .collect(Collectors.toList());
//    }
//
//    public static UserDTO convertToDTO(User user) {
//        if (user instanceof Client client) {
//            return new ClientDTO(
//                    client.getId().toString(),
//                    client.getLogin(),
//                    client.getFirstName(),
//                    client.getLastName(),
//                    client.getRole(),
//                    client.getClientType(),
//                    client.getActive()
//            );
//        } else {
//            return new UserDTO(
//                    user.getId().toString(),
//                    user.getLogin(),
//                    user.getFirstName(),
//                    user.getLastName(),
//                    user.getRole(),
//                    user.getActive()
//            );
//        }
//    }
//
//    public static UserDTO convertToUserDTO(User user) {
//        return new UserDTO(
//                user.getId().toString(),
//                user.getLogin(),
//                user.getFirstName(),
//                user.getLastName(),
//                user.getRole(),
//                user.getActive()
//        );
//    }

    public static User toModel(UserEnt userEnt) {
        if (userEnt instanceof ClientEnt clientEnt) {
            return new Client(
                    clientEnt.getId().toString(),
                    clientEnt.getLogin(),
                    clientEnt.getPassword(),
                    clientEnt.getFirstName(),
                    clientEnt.getLastName(),
                    clientEnt.getClientType().toModel()
            );
        } else if (userEnt instanceof AdminEnt adminEnt) {
            return new Admin(
                    adminEnt.getId().toString(),
                    adminEnt.getLogin(),
                    adminEnt.getPassword(),
                    adminEnt.getFirstName(),
                    adminEnt.getLastName()
            );
        } else if (userEnt instanceof ManagerEnt managerEnt) {
            return new Manager(
                    managerEnt.getId().toString(),
                    managerEnt.getLogin(),
                    managerEnt.getPassword(),
                    managerEnt.getFirstName(),
                    managerEnt.getLastName()
            );
        } else {
            throw new IllegalArgumentException("Unknown user type: " + userEnt.getClass().getSimpleName());
        }
    }

    public static ClientType toModel(ClientTypeEnt clientTypeEnt) {
        return new ClientType(clientTypeEnt.getMaxArticles(), clientTypeEnt.getDiscount());
    }
}
