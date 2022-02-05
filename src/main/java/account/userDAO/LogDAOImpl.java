package account.userDAO;

import account.entity.Log;
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
public class LogDAOImpl implements LogDAO {

    @Autowired
    DataSource dataSource;

    @Override
    public void addLog(Log log) {
        String sqlAddLog = "INSERT INTO logs (date, action, subject, object, path) VALUES (?, ?, ?, ?, ?)";

        try (Connection connection = dataSource.getConnection()) {

            try (PreparedStatement preparedStatement = connection.prepareStatement(sqlAddLog)) {
                preparedStatement.setString(1,log.getDate());
                preparedStatement.setString(2,log.getAction());
                preparedStatement.setString(3,log.getSubject());
                preparedStatement.setString(4,log.getObject());
                preparedStatement.setString(5,log.getPath());
                preparedStatement.execute();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public List<Log> getLogsOrderById() {
        String sqlGetLogsOrderById = "SELECT * FROM logs ORDER BY id";

        try (Connection connection = dataSource.getConnection()) {

            try (PreparedStatement preparedStatement = connection.prepareStatement(sqlGetLogsOrderById)) {
                ResultSet resultSet = preparedStatement.executeQuery();
                return mapRowToLogList(resultSet);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private List<Log> mapRowToLogList(ResultSet resultSet) throws SQLException {
        List<Log> logs = new ArrayList<>();
        while (resultSet.next()) {
            Log log = new Log();
            log.setId(resultSet.getInt("id"));
            log.setDate(resultSet.getString("date"));
            log.setAction(resultSet.getString("action"));
            log.setSubject(resultSet.getString("subject"));
            log.setObject(resultSet.getString("object"));
            log.setPath(resultSet.getString("path"));
            logs.add(log);
        }
        return logs;
    }
}
