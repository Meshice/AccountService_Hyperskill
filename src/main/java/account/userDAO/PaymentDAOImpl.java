package account.userDAO;

import account.entity.Payment;
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

@Repository
public class PaymentDAOImpl implements PaymentDAO {


    @Autowired
    DataSource dataSource;

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
    public boolean findEmailPeriodUnique(Payment p) {
        String sqlRequestCheckUniquePeriodEmail = "SELECT * FROM payments WHERE employee = LOWER(?) AND period = ?";
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(sqlRequestCheckUniquePeriodEmail)) {
                preparedStatement.setString(1, p.getEmployee());
                preparedStatement.setDate(2,p.getPeriod());
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
    public void addPayment(List<Payment> payment) {
        String sqlRequestAddPayment = "INSERT INTO payments(employee,period,salary) VALUES (?,?,?)";
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(false);
            try (PreparedStatement preparedStatement = connection.prepareStatement(sqlRequestAddPayment)) {
                for (Payment p : payment) {
                    preparedStatement.setString(1, p.getEmployee().toLowerCase());
                    preparedStatement.setDate(2, p.getPeriod());
                    preparedStatement.setLong(3, p.getSalary());
                    preparedStatement.execute();
                }
            }
            connection.commit();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private List<PaymentUserInfo> mapRowPayment(ResultSet resultSet) throws SQLException {
        List<PaymentUserInfo> list = new ArrayList<>();
        while (resultSet.next()) {
            PaymentUserInfo info = new PaymentUserInfo();
            info.setPeriod(resultSet.getDate("period"));
            info.setSalary(Long.toString(resultSet.getLong("salary")));
            list.add(info);
        }
        return list;
    }
}
