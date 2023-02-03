package dao.inter;

import java.sql.Connection;
import java.sql.DriverManager;

public abstract class AbstractDao {
    public static Connection connect() throws Exception {
        String url = "jdbc:mysql://localhost:3306/resume";
        String username = "root";
        String password = "my1783406sql";
        Connection connection = DriverManager.getConnection(url, username, password);
        return connection;
    }
}
