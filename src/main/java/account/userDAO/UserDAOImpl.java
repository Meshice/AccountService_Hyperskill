package account.userDAO;

import account.entity.Payment;
import account.entity.User;
import account.request.UpdatePaymentRequest;
import account.response.PaymentUserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;
import org.springframework.web.server.ResponseStatusException;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Repository
public class UserDAOImpl implements UserDAO {

    @Autowired
    DataSource dataSource;

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
    public void changePassword(int id, String newPassword) {
        String sqlRequestChangePassword = "UPDATE users SET password = ? WHERE id = ?";
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(sqlRequestChangePassword)) {
                preparedStatement.setString(1, newPassword);
                preparedStatement.setInt(2, id);
                preparedStatement.execute();
            }
        } catch (SQLException ex) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public void updatePersonRoles(User user) {
        String sqlUpdatePersonRoles = "UPDATE users SET role = ? WHERE id = ?";
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(sqlUpdatePersonRoles)) {
                preparedStatement.setString(1, user.getRole());
                preparedStatement.setInt(2, user.getId());
                preparedStatement.execute();
            }
        } catch (SQLException ex) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public void addPayment(List<Payment> payment) {
        String sqlRequestAddPayment = "INSERT INTO payments(employee,period,salary) VALUES (?,?,?)";
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(false);
            try (PreparedStatement preparedStatement = connection.prepareStatement(sqlRequestAddPayment)) {
                for (Payment p : payment) {
                    preparedStatement.setString(1, p.getEmployee().toLowerCase());
                    preparedStatement.setString(2, p.getPeriod());
                    preparedStatement.setLong(3, p.getSalary());
                    preparedStatement.execute();
                }
            }
            connection.commit();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public boolean findEmailPeriodUnique(Payment p) {
        String sqlRequestCheckUniquePeriodEmail = "SELECT * FROM payments WHERE employee = LOWER(?) AND period = ?";
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(sqlRequestCheckUniquePeriodEmail)) {
                preparedStatement.setString(1, p.getEmployee());
                preparedStatement.setString(2, p.getPeriod());
                ResultSet resultSet = preparedStatement.executeQuery();
                if (resultSet.next()) {
                    return false;
                }
            }
            return true;
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public void updatePaymentByEmployeePeriod(UpdatePaymentRequest payment) {
        String sqlRequestAddNew = "UPDATE payments SET salary = ? WHERE employee = LOWER(?) AND period = ?";
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(sqlRequestAddNew)) {
                preparedStatement.setLong(1, payment.getSalary());
                preparedStatement.setString(2, payment.getEmployee());
                preparedStatement.setString(3, payment.getPeriod());
                preparedStatement.execute();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public int getUserCount() {
        String sqlGetUserCount = "SELECT COUNT(*) FROM users";
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(sqlGetUserCount)) {
                ResultSet resultSet = preparedStatement.executeQuery();
                resultSet.next();
                return resultSet.getInt(1);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public List<PaymentUserInfo> getInfoUserByPeriod(String period, String employee) {
        try (Connection connection = dataSource.getConnection()) {
            if (period == null) {
                try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM payments WHERE employee = LOWER(?)")) {
                    preparedStatement.setString(1, employee);
                    return mapRowPayment(preparedStatement.executeQuery());
                }
            } else {
                try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM payments WHERE employee = LOWER(?) AND period = ?")) {
                    preparedStatement.setString(1, employee);
                    preparedStatement.setString(2, period);
                    return mapRowPayment(preparedStatement.executeQuery());
                }
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

    @Override
    public void decreaseAttempt(String email) {
        String sqlDecreaseAttempt = "UPDATE users SET attempt = attempt - 1 WHERE email = LOWER(?)";
        try (Connection connection = dataSource.getConnection()) {

            try (PreparedStatement preparedStatement = connection.prepareStatement(sqlDecreaseAttempt)) {
                preparedStatement.setString(1,email);
                preparedStatement.execute();
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public void returnUserAttempt(String email) {
        String sqlReturnUserAttempt = "UPDATE users SET attempt = 5 WHERE email = LOWER(?)";
        try (Connection connection = dataSource.getConnection()) {

            try (PreparedStatement preparedStatement = connection.prepareStatement(sqlReturnUserAttempt)) {
                preparedStatement.setString(1,email);
                preparedStatement.executeUpdate();
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
            user.setRoles(List.of(user.getRole().split(" ")));
            return user;
        } else {
            return null;
        }
    }

    private List<PaymentUserInfo> mapRowPayment(ResultSet resultSet) throws SQLException {
        List<PaymentUserInfo> list = new ArrayList<>();
        while (resultSet.next()) {
            PaymentUserInfo info = new PaymentUserInfo();
            info.setPeriod(resultSet.getString("period"));
            info.setSalary(Long.toString(resultSet.getLong("salary")));
            list.add(info);
        }
        return list;
    }
}
