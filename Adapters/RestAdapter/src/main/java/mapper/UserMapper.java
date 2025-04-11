package mapper;

import dto.ClientDTO;
import dto.CreateUserDTO;
import dto.UserDTO;
import model.user.*;
import org.bson.types.ObjectId;

import java.util.List;
import java.util.stream.Collectors;

import static model.user.Role.*;

public class UserMapper {
    public List<UserDTO> toDTO(List<User> users) {
        return users.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public UserDTO convertToDTO(User user) {
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

    public UserDTO convertToUserDTO(User user) {
        return new UserDTO(
                user.getId().toString(),
                user.getLogin(),
                user.getFirstName(),
                user.getLastName(),
                user.getRole(),
                user.getActive()
        );
    }

    public User convertToUser(CreateUserDTO createUserDTO) {
        ObjectId objectId = createUserDTO.getId() != null ? new ObjectId(createUserDTO.getId()) : null;
        return switch (createUserDTO.getRole()) {
            case CLIENT -> new Client(
                    objectId.toString(),
                    createUserDTO.getLogin(),
                    createUserDTO.getPassword(),
                    createUserDTO.getFirstName(),
                    createUserDTO.getLastName(),
                    ClientType.createNoMembership());
            case ADMIN -> new Admin(
                    objectId.toString(),
                    createUserDTO.getLogin(),
                    createUserDTO.getPassword(),
                    createUserDTO.getFirstName(),
                    createUserDTO.getLastName());
            case MANAGER -> new Manager(
                    objectId.toString(),
                    createUserDTO.getLogin(),
                    createUserDTO.getPassword(),
                    createUserDTO.getFirstName(),
                    createUserDTO.getLastName());
        };
    }
}
