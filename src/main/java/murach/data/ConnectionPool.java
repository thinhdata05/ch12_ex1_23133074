package murach.data;

import java.sql.*;
import javax.naming.*;
import javax.sql.DataSource;

public class ConnectionPool {

    private static ConnectionPool pool = null;
    private static DataSource dataSource = null;

    private ConnectionPool() {
        try {
            Context envContext = (Context) new InitialContext().lookup("java:/comp/env");
            dataSource = (DataSource) envContext.lookup("jdbc/sqlGatewayDB");
        } catch (NamingException e) {
            e.printStackTrace();
        }
    }

    public static synchronized ConnectionPool getInstance() {
        if (pool == null) {
            pool = new ConnectionPool();
        }
        return pool;
    }

    public Connection getConnection() {
        try {
            return dataSource.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void freeConnection(Connection c) {
        try {
            if (c != null) c.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
