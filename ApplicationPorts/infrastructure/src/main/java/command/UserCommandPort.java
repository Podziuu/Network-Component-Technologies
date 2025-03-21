package command;

import model.user.User;

import java.util.Optional;

public interface UserCommandPort {
    User add(User user);

    void remove(User user);

    User update(String id, String firstName, String lastName);
}
