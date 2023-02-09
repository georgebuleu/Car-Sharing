package carsharing.car;

import carsharing.DatabaseConfig;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

public class CarDao implements Dao{

    Connection connection;

    public CarDao(String dbName){
        try {
            Class.forName(DatabaseConfig.JDBC_DRIVER);
            connection = DriverManager.getConnection(DatabaseConfig.DB_URL + dbName);
            connection.setAutoCommit(true);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Car> getAllCars() {
        return null;
    }

    @Override
    public void addCar(Car car) {

    }
}
