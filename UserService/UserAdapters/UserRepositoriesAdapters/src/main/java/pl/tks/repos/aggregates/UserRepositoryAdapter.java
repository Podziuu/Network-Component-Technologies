package pl.tks.repos.aggregates;

import org.springframework.stereotype.Component;
import pl.tks.model.user.Role;
import pl.tks.model.user.User;
import pl.tks.ports.infrastructure.UserPort;
import pl.tks.repos.mappers.UserMapper;
import pl.tks.repos.repo.UserRepository;

import java.util.List;

@Component
public class UserRepositoryAdapter implements UserPort {
    private final UserRepository userRepository;

    public UserRepositoryAdapter(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User addUser(User user) {
        return UserMapper.toUser(userRepository.save(UserMapper.toUserEnt(user)));
    }

    @Override
    public void updateUser(String id, String firstName, String lastName) {
        userRepository.update(id, firstName, lastName);
    }

    @Override
    public void deleteAll() {
        userRepository.deleteAll();
    }

    @Override
    public User getById(String id) {
        return UserMapper.toUser(userRepository.findById(id));
    }

    @Override
    public List<User> getAll() {
        return UserMapper.toUserList(userRepository.findAll());
    }

    @Override
    public User findByLogin(String login) {
        return UserMapper.toUser(userRepository.findByLogin(login));
    }

    @Override
    public List<User> findByRole(Role role) {
        return UserMapper.toUserList(userRepository.findByRole(role));
    }

    @Override
    public List<User> findByFirstName(String firstName) {
        return UserMapper.toUserList(userRepository.findByFirstName(firstName));
    }

    @Override
    public List<User> findByRoleAndFirstName(Role role, String firstName) {
        return UserMapper.toUserList(userRepository.findByRoleAndFirstName(role, firstName));
    }

    @Override
    public boolean userExists(String login) {
        return userRepository.userExists(login);
    }

    @Override
    public void activateUser(String id) {
        userRepository.activateUser(id);
    }

    @Override
    public void deactivateUser(String id) {
        userRepository.deactivateUser(id);
    }

    @Override
    public void updatePassword(String id, String newPassword) {
        userRepository.updatePassword(id, newPassword);
    }
}
