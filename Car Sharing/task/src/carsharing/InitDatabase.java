package carsharing;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class InitDatabase {
    private Connection conn;
    private Statement stmt;
    private final String url;

    public InitDatabase(String url) {
        try {
            File dir = new File(DatabaseConfig.DB_URL);
            if (!dir.exists()) {
                dir.mkdir();
            }
        } catch (Exception ex) {
            System.err.println(ex.toString());
        }
        this.url =DatabaseConfig.DB_URL + url;
        connect();
        closeConnection();
    }


    private void connect() {
        try {
            Class.forName(DatabaseConfig.JDBC_DRIVER);
            conn = DriverManager.getConnection(url);
            conn.setAutoCommit(true);

            stmt = conn.createStatement();
            String sql = "CREATE TABLE COMPANY " +
                    "(id INT PRIMARY KEY AUTO_INCREMENT," +
                    "name VARCHAR(50) NOT NULL," +
                    "UNIQUE(name));";
            String dropCompany = "DROP TABLE COMPANY IF EXISTS;";

            stmt.executeUpdate(dropCompany);
            stmt.executeUpdate(sql);

        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public void closeConnection() {
        try {
            if (stmt != null) stmt.close();
        } catch (SQLException ignored) {
        } // nothing we can do
        try {
            if (conn != null) conn.close();
        } catch (SQLException se) {
            se.printStackTrace();
        }
    }

}