package carsharing;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class InitDatabase {
    private static Connection conn;
    private static Statement stmt;
    private static String dbUrl = null;
    private final static String alterCompany= "ALTER TABLE company ALTER COLUMN id RESTART WITH 1";
    private final static String alterCustomer= "ALTER TABLE CUSTOMER ALTER COLUMN rented_car_id int;";
    private final static String updateCustomer= "UPDATE CUSTOMER SET rented_car_id = -1 WHERE rented_car_id IS NULL;";

    //private String dropCompany = "DROP TABLE COMPANY IF EXISTS;";
   // private String dropCar = "DROP TABLE CAR IF EXISTS";
    private static final String createCompany = "CREATE TABLE IF NOT EXISTS COMPANY" +
            "(id INT PRIMARY KEY AUTO_INCREMENT," +
            "name VARCHAR(50) NOT NULL," +
            "UNIQUE(name));";
    private static final String createCar ="CREATE TABLE IF NOT EXISTS CAR" +
            "(id INT PRIMARY KEY AUTO_INCREMENT," +
            "name VARCHAR(50) NOT NULL," +
            "company_id INT NOT NULL," +
            "UNIQUE(name)," +
            "CONSTRAINT fk_company FOREIGN KEY (company_id) REFERENCES COMPANY(id)" +
            ");";
    private static final String createCustomer = """
            CREATE TABLE IF NOT EXISTS CUSTOMER (
              ID INT PRIMARY KEY AUTO_INCREMENT,
              NAME VARCHAR(255) NOT NULL UNIQUE,
              RENTED_CAR_ID INT,
              CONSTRAINT FK_CUSTOMER_CAR FOREIGN KEY (RENTED_CAR_ID) REFERENCES CAR(ID)
            );""";
    private static final String showCustomer = "SHOW CREATE TABLE CUSTOMER;";

    public static void initDatabase(String url) {
        try {
            File dir = new File(DatabaseConfig.DB_URL);
            if (!dir.exists()) {
                dir.mkdir();
            }
        } catch (Exception ex) {
            System.err.println(ex.toString());
        }
        dbUrl =DatabaseConfig.DB_URL + url;
        connect();
        closeConnection(stmt);
    }


    private static void connect() {
        try {
            Class.forName(DatabaseConfig.JDBC_DRIVER);
            conn = DriverManager.getConnection(dbUrl);
            conn.setAutoCommit(true);

            stmt = conn.createStatement();
            stmt.executeUpdate(createCompany);
            stmt.executeUpdate(createCar);
            stmt.executeUpdate(createCustomer);

        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static void closeConnection(Statement stmt) {
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