package ro.persistence;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DbUtils extends Properties {
    private final Properties jdbcProps;

    public DbUtils(Properties props) {
        this.jdbcProps = props;
    }

    private Connection instance = null;

    private Connection getNewConnection() {
        String url = jdbcProps.getProperty("jdbc.url");
        String user = jdbcProps.getProperty("jdbc.user");
        String pass = jdbcProps.getProperty("jdbc.password");
        Connection con = null;
        try {
            if (user != null && pass != null)
                con = DriverManager.getConnection(url, user, pass);
            else
                con = DriverManager.getConnection(url);
        } catch (SQLException ex) {
            System.out.println("Error getting connection to the database " + ex);
        }
        return con;
    }

    public Connection getConnection() {
        try {
            if (instance == null || instance.isClosed())
                instance = getNewConnection();
        } catch (SQLException ex) {
            System.out.println("Error DB " + ex);
        }
        return instance;
    }
}