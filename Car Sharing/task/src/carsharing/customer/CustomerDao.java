package carsharing.customer;

import carsharing.DatabaseConfig;
import carsharing.car.Car;
import carsharing.company.Company;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CustomerDao implements Dao {

    Connection connection;

    public CustomerDao(String dbName) {
        try {
            Class.forName(DatabaseConfig.JDBC_DRIVER);
            connection = DriverManager.getConnection(DatabaseConfig.DB_URL + dbName);
            connection.setAutoCommit(true);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Customer> getAllCustomers() {
        List<Customer> customers = new ArrayList<>();

        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM CUSTOMER");
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                customers.add(new Customer(resultSet.getInt("id"), resultSet.getString("name"), resultSet.getInt("rented_car_id")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return customers;
    }

    @Override
    public void add(String name) {
    try{
        PreparedStatement statement = connection.prepareStatement("INSERT INTO CUSTOMER(NAME) VALUES(?);");
        statement.setString(1, name);
       // statement.setInt(2, customer.getRentedCarId());
        statement.executeUpdate();
    } catch (SQLException e) {
        throw new RuntimeException(e);
    }
    }

    @Override
    public void returnCar(int id) {
    try {
        PreparedStatement statement = connection.prepareStatement("UPDATE CUSTOMER SET RENTED_CAR_ID = NULL WHERE id = ?;");
        statement.setInt(1, id);
        statement.executeUpdate();
    } catch (SQLException e) {
        e.printStackTrace();
        }
    }

    @Override
    public void rentCar(int carId, int customerId) {
        try {
            PreparedStatement statement = connection.prepareStatement("UPDATE CUSTOMER SET RENTED_CAR_ID = ? WHERE id = ?;");
            statement.setInt(1, carId);
            statement.setInt(2, customerId);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public Customer getById(int id) {
        Customer customer = null;
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM CUSTOMER WHERE id = ?;");
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                customer = new Customer(resultSet.getInt("id"), resultSet.getString("name"), resultSet.getInt("rented_car_id"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return customer;
    }
}
