package query;

import model.user.User;

import java.util.List;
import java.util.Optional;

public interface UserQueryPort {
    Optional<User> getById(String id);

    List<User> getAll();

//    Optional<User> getUserByLogin

}
