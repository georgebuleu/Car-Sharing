package carsharing.car;

import carsharing.DatabaseConfig;
import carsharing.customer.Customer;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CarDao implements Dao{

    private Connection connection;

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
        List<Car> cars = new ArrayList<>();

        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM CAR;");
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                cars.add(new Car(resultSet.getInt("id"), resultSet.getString("name"), resultSet.getInt("company_id")));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cars;
    }
    public List<Car> getCarsByCompanyId(int id)  {
        List<Car> cars = new ArrayList<>();
        try{
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM CAR WHERE company_id = ?;");
            statement.setInt(1,id);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()){
                cars.add(new Car(resultSet.getInt("id"),resultSet.getString("name"),resultSet.getInt("company_id")));
            }
        } catch (SQLException e) {

        }
        return cars;
    }
    @Override
    public Car getRentedCar(int id) {
        Car car = null;
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM CAR WHERE id = ?;");
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return car = new Car(resultSet.getInt("id"), resultSet.getString("name"), resultSet.getInt("company_id"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return car;
    }

    @Override
    public List<Car> availableCars(int id) {
        List<Car> cars = new ArrayList<>();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT CAR.id as id, CAR.name as name, CAR.company_id as company_id \n" +
                    "FROM CAR \n" +
                    "LEFT JOIN CUSTOMER ON CAR.id = CUSTOMER.rented_car_id \n" +
                    "WHERE CAR.company_id = ? AND CUSTOMER.rented_car_id is null;");
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                cars.add(new Car(resultSet.getInt("id"), resultSet.getString("name")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cars;
    }


    @Override
    public void addCar(Car car) {
    try{
        PreparedStatement statement = connection.prepareStatement("INSERT INTO CAR(NAME, COMPANY_ID) VALUES(?,?);");
        statement.setString(1, car.getName());
        statement.setInt(2, car.getCompanyId());
        statement.executeUpdate();
    }catch (SQLException e){

    }
    }
}
