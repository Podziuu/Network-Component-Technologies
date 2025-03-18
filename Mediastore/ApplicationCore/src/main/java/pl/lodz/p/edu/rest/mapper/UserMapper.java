package pl.lodz.p.edu.rest.mapper;

import org.bson.types.ObjectId;
import pl.lodz.p.edu.rest.dto.ClientDTO;
import pl.lodz.p.edu.rest.dto.CreateUserDTO;
import pl.lodz.p.edu.rest.dto.UserDTO;
import pl.lodz.p.edu.rest.model.user.*;

import java.util.List;
import java.util.stream.Collectors;

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
                    objectId,
                    createUserDTO.getLogin(),
                    createUserDTO.getPassword(),
                    createUserDTO.getFirstName(),
                    createUserDTO.getLastName(),
                    ClientType.createNoMembership());
            case ADMIN -> new Admin(
                    objectId,
                    createUserDTO.getLogin(),
                    createUserDTO.getPassword(),
                    createUserDTO.getFirstName(),
                    createUserDTO.getLastName());
            case MANAGER -> new Manager(
                    objectId,
                    createUserDTO.getLogin(),
                    createUserDTO.getPassword(),
                    createUserDTO.getFirstName(),
                    createUserDTO.getLastName());
        };
    }
}
