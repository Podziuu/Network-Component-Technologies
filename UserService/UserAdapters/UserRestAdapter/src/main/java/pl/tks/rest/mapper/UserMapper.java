package pl.tks.rest.mapper;

import org.springframework.stereotype.Component;
import pl.tks.model.user.*;
import pl.tks.rest.dto.ClientDTO;
import pl.tks.rest.dto.CreateUserDTO;
import pl.tks.rest.dto.UserDTO;

import java.util.List;
import java.util.stream.Collectors;

@Component
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
