package pl.tks.repos.mappers;

import org.bson.types.ObjectId;
import pl.tks.model.user.*;
import pl.tks.repos.entities.user.*;

import java.util.List;
import java.util.stream.Collectors;

public class UserMapper {
    public static Client toClient(ClientEnt clientEnt) {
        return new Client(clientEnt.getId().toString(),
                clientEnt.getLogin(),
                clientEnt.getPassword(),
                clientEnt.getFirstName(),
                clientEnt.getLastName(),
                clientEnt.getClientType().toModel());
    }

    public static ClientEnt toClientEnt(Client client) {
        return new ClientEnt(new ObjectId(),
                client.getLogin(),
                client.getPassword(),
                client.getFirstName(),
                client.getLastName(),
                new ClientTypeEnt(client.getClientType().getMaxArticles(), client.getClientType().getDiscount()));
    }

    public static Admin toAdmin(AdminEnt adminEnt) {
        return new Admin(adminEnt.getId().toString(),
                adminEnt.getLogin(),
                adminEnt.getPassword(),
                adminEnt.getFirstName(),
                adminEnt.getLastName());
    }

    public static AdminEnt toAdminEnt(Admin admin) {
        return new AdminEnt(new ObjectId(),
                admin.getLogin(),
                admin.getPassword(),
                admin.getFirstName(),
                admin.getLastName());
    }

    public static Manager toManager(ManagerEnt managerEnt) {
        return new Manager(managerEnt.getId().toString(),
                managerEnt.getLogin(),
                managerEnt.getPassword(),
                managerEnt.getFirstName(),
                managerEnt.getLastName());
    }

    public static ManagerEnt toManagerEnt(Manager manager) {
        return new ManagerEnt(new ObjectId(),
                manager.getLogin(),
                manager.getPassword(),
                manager.getFirstName(),
                manager.getLastName());
    }

    public static User toUser(UserEnt userEnt) {
        return switch (userEnt.getRole()) {
            case CLIENT -> toClient((ClientEnt) userEnt);
            case ADMIN -> toAdmin((AdminEnt) userEnt);
            case MANAGER -> toManager((ManagerEnt) userEnt);
        };
    }

    public static UserEnt toUserEnt(User user) {
        return switch (user.getRole()) {
            case CLIENT -> toClientEnt((Client) user);
            case ADMIN -> toAdminEnt((Admin) user);
            case MANAGER -> toManagerEnt((Manager) user);
        };
    }

    public static List<User> toUserList(List<UserEnt> users) {
        return users.stream().map(UserMapper::toUser).collect(Collectors.toList());
    }

    public static List<UserEnt> toUserEntList(List<User> users) {
        return users.stream().map(UserMapper::toUserEnt).collect(Collectors.toList());
    }

//    public static UserEnt toUserEnt(User user) {
//        if (user instanceof Client client) {
//            return new ClientEnt(
//                    new ObjectId(client.getId()),
//                    client.getLogin(),
//                    client.getPassword(),
//                    client.getFirstName(),
//                    client.getLastName(),
//                    client.getClientType(),
//            );
//        }
//    }

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
