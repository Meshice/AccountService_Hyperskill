package account.userDAO;

import account.entity.Log;

import java.util.List;

public interface LogDAO {

    public void addLog(Log log);

    public List<Log> getLogsOrderById();

}
