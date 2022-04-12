package account.userDAO;

import account.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;
import org.springframework.web.server.ResponseStatusException;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class UserDAOImpl implements UserDAO {

    @Autowired
    DataSource dataSource;

    @Override
    public void update(User user) {
        String sqlUpdateUserRequest = "UPDATE users SET email = ?, name = ?, lastname = ?, password = ?, role = ?, attempt = ?, locked = ? WHERE id = ?";
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(sqlUpdateUserRequest)) {
                preparedStatement.setString(1, user.getEmail());
                preparedStatement.setString(2, user.getName());
                preparedStatement.setString(3, user.getLastname());
                preparedStatement.setString(4, user.getPassword());
                preparedStatement.setString(5, user.getRole());
                preparedStatement.setInt(6, user.getAttemptsForLogging());
                preparedStatement.setBoolean(7, user.isLocked());
                preparedStatement.setInt(8, user.getId());
                preparedStatement.executeUpdate();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public User findByEmailIgnoreCase(String email) {
        String sqlRequest = "SELECT * FROM users WHERE email = LOWER(?)";
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(sqlRequest)) {
                preparedStatement.setString(1, email);
                ResultSet resultSet = preparedStatement.executeQuery();
                return mapRowUser(resultSet);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public void save(User user) {
        String sqlRequestAddNew = "INSERT INTO users(name,lastname,email,password,role,locked,attempt) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(sqlRequestAddNew)) {
                preparedStatement.setString(1, user.getName());
                preparedStatement.setString(2, user.getLastname());
                preparedStatement.setString(3, user.getEmail().toLowerCase());
                preparedStatement.setString(4, user.getPassword());
                preparedStatement.setString(5, user.getRole());
                preparedStatement.setBoolean(6, user.isLocked());
                preparedStatement.setInt(7, user.getAttemptsForLogging());
                preparedStatement.executeUpdate();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @Override
    public List<User> getAllUsersOrderById() {
        String sqlGetAllUsersOrderById = "SELECT * FROM users ORDER BY id";
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(sqlGetAllUsersOrderById)) {
                ResultSet resultSet = preparedStatement.executeQuery();
                return mapRowToUserList(resultSet);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public void deleteUser(String email) {
        String sqlDeleteUserByEmail = "DELETE FROM users WHERE email = LOWER(?)";
        try (Connection connection = dataSource.getConnection()) {

            try (PreparedStatement preparedStatement = connection.prepareStatement(sqlDeleteUserByEmail)) {
                preparedStatement.setString(1,email);
                preparedStatement.execute();
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public void lockUnlockUser(String email, String operation) {
        try (Connection connection = dataSource.getConnection()) {
            if (operation.equals("LOCK")) {
                String sqlLock = "UPDATE users SET locked = true WHERE email = LOWER(?)";
                try (PreparedStatement preparedStatement = connection.prepareStatement(sqlLock)) {
                    preparedStatement.setString(1,email);
                    preparedStatement.executeUpdate();
                }
            } else {
                String sqlUnlock = "UPDATE users SET locked = false, attempt = 5 WHERE email = LOWER(?)";
                try (PreparedStatement preparedStatement = connection.prepareStatement(sqlUnlock)) {
                    preparedStatement.setString(1,email);
                    preparedStatement.executeUpdate();
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    private List<User> mapRowToUserList(ResultSet resultSet) throws SQLException {
        List<User> userList = new ArrayList<>();
        while (resultSet.next()) {
            User user = new User();
            user.setId(resultSet.getInt("id"));
            user.setName(resultSet.getString("name"));
            user.setLastname(resultSet.getString("lastname"));
            user.setEmail(resultSet.getString("email"));
            user.setPassword(resultSet.getString("password"));
            user.setRole(resultSet.getString("role"));
            user.setLocked(resultSet.getBoolean("locked"));
            user.setAttemptsForLogging(resultSet.getInt("attempt"));
            user.setRoles(List.of(user.getRole().split(" ")));
            userList.add(user);
        }
        return userList;
    }


    private User mapRowUser(ResultSet resultSet) throws SQLException {
        if (resultSet.next()) {
            User user = new User();
            user.setId(resultSet.getInt("id"));
            user.setName(resultSet.getString("name"));
            user.setLastname(resultSet.getString("lastname"));
            user.setEmail(resultSet.getString("email"));
            user.setPassword(resultSet.getString("password"));
            user.setRole(resultSet.getString("role"));
            user.setLocked(resultSet.getBoolean("locked"));
            user.setAttemptsForLogging(resultSet.getInt("attempt"));
            user.setRoles(new ArrayList<>(List.of(user.getRole().split(" "))));
            return user;
        } else {
            return null;
        }
    }

}
