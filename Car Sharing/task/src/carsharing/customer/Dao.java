package carsharing.customer;

import carsharing.car.Car;

import java.util.List;

public interface Dao {
    List<Customer> getAllCustomers();
    Customer getById(int id);
    void add(String name);
    void returnCar(int id);
    void rentCar(int carId, int customerId);

}
