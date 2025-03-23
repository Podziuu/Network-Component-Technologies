package infrastructure;

import model.user.User;

public interface UserCommandPort {
    User add(User user);

    void update(String id, String firstName, String lastName);

    void activateUser(String id);

    void deactivateUser(String id);
}
