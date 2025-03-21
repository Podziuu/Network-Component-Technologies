package command;

import model.user.User;

import java.util.Optional;

public interface UserCommandPort {
    User add(User user);

    void remove(User user);

    Optional<User> update(User user);
}
