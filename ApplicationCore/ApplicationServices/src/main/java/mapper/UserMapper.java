package mapper;

import dto.ClientDTO;
import dto.CreateUserDTO;
import dto.UserDTO;
import model.user.*;
import org.bson.types.ObjectId;

import java.util.List;
import java.util.stream.Collectors;

public class UserMapper {
    public static List<UserDTO> toDTO(List<User> users) {
        return users.stream()
                .map(UserMapper::convertToDTO)
                .collect(Collectors.toList());
    }

    public static UserDTO convertToDTO(User user) {
        if (user instanceof Client client) {
            return new ClientDTO(
                    client.getId().toString(),
                    client.getLogin(),
                    client.getFirstName(),
                    client.getLastName(),
                    client.getRole(),
                    client.getClientType(),
                    client.getActive()
            );
        } else {
            return new UserDTO(
                    user.getId().toString(),
                    user.getLogin(),
                    user.getFirstName(),
                    user.getLastName(),
                    user.getRole(),
                    user.getActive()
            );
        }
    }

    public static UserDTO convertToUserDTO(User user) {
        return new UserDTO(
                user.getId().toString(),
                user.getLogin(),
                user.getFirstName(),
                user.getLastName(),
                user.getRole(),
                user.getActive()
        );
    }

    public static User convertToUser(CreateUserDTO createUserDTO) {
//        ObjectId objectId = createUserDTO.getId() != null ? new ObjectId(createUserDTO.getId()) : null;
        return switch (createUserDTO.getRole()) {
            case CLIENT -> new Client(
                    createUserDTO.getId(),
                    createUserDTO.getLogin(),
                    createUserDTO.getPassword(),
                    createUserDTO.getFirstName(),
                    createUserDTO.getLastName(),
                    ClientType.createNoMembership());
            case ADMIN -> new Admin(
                    createUserDTO.getId(),
                    createUserDTO.getLogin(),
                    createUserDTO.getPassword(),
                    createUserDTO.getFirstName(),
                    createUserDTO.getLastName());
            case MANAGER -> new Manager(
                    createUserDTO.getId(),
                    createUserDTO.getLogin(),
                    createUserDTO.getPassword(),
                    createUserDTO.getFirstName(),
                    createUserDTO.getLastName());
        };
    }
}
