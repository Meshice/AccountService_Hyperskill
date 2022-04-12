package account.userDAO;

import account.entity.User;

import java.util.List;

public interface UserDAO {

    void save(User user);

    void update(User user);

    User findByEmailIgnoreCase(String email);

    List<User> getAllUsersOrderById();

    void deleteUser(String email);

    void lockUnlockUser(String email, String operation);

}
