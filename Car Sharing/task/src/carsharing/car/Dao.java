package carsharing.car;

import carsharing.customer.Customer;

import java.util.List;

public interface Dao {
    List<Car> getAllCars();
    List<Car> getCarsByCompanyId(int id);

    Car getRentedCar(int id);
    List<Car> availableCars(int companyId);
    void addCar(Car car);
}
