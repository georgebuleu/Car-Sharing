package carsharing.car;

import java.util.List;

public interface Dao {
    List<Car> getAllCars();
    void addCar(Car car);
}
