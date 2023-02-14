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
    private final String alterCompany= "ALTER TABLE company ALTER COLUMN id RESTART WITH 1";
    private final String alterCar= "ALTER TABLE car ALTER COLUMN id RESTART WITH 1";

    //private String dropCompany = "DROP TABLE COMPANY IF EXISTS;";
   // private String dropCar = "DROP TABLE CAR IF EXISTS";
    private final String createCompany = "CREATE TABLE IF NOT EXISTS COMPANY" +
            "(id INT PRIMARY KEY AUTO_INCREMENT," +
            "name VARCHAR(50) NOT NULL," +
            "UNIQUE(name));";
    private final String createCar ="CREATE TABLE IF NOT EXISTS CAR" +
            "(id INT PRIMARY KEY AUTO_INCREMENT," +
            "name VARCHAR(50) NOT NULL," +
            "company_id INT NOT NULL," +
            "UNIQUE(name)," +
            "CONSTRAINT fk_company FOREIGN KEY (company_id) REFERENCES COMPANY(id)" +
            ");";

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
            stmt.executeUpdate(createCompany);
            stmt.executeUpdate(createCar);

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