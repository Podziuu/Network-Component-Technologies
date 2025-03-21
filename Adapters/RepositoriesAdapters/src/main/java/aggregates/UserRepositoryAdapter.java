package aggregates;

import command.UserCommandPort;
import model.user.Role;
import model.user.User;
import query.UserQueryPort;

import java.util.List;

public class UserRepositoryAdapter implements UserCommandPort, UserQueryPort {
    @Override
    public User add(User user) {
        return null;
    }

    @Override
    public void remove(User user) {

    }

    @Override
    public User update(String id, String firstName, String lastName) {
        return null;
    }

    @Override
    public User getById(String id) {
        return null;
    }

    @Override
    public List<User> getAll() {
        return List.of();
    }

    @Override
    public User findByLogin(String login) {
        return null;
    }

    @Override
    public List<User> findByRole(Role role) {
        return List.of();
    }

    @Override
    public List<User> findByFirstName(String firstName) {
        return List.of();
    }

    @Override
    public List<User> findByRoleAndFirstName(Role role, String firstName) {
        return List.of();
    }

    @Override
    public boolean userExists(String login) {
        return false;
    }
}
