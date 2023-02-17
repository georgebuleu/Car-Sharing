package carsharing;

import carsharing.car.Car;
import carsharing.car.CarDao;
import carsharing.company.Company;
import carsharing.company.CompanyDao;
import carsharing.customer.Customer;
import carsharing.customer.CustomerDao;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {


    public static void main(String[] args) {

        String defaultDbName = "test";
        String dbName = parseDatabaseName(args, defaultDbName);
        InitDatabase.initDatabase(dbName);
        CompanyDao companyDao = new CompanyDao(dbName);
        CarDao carDao = new CarDao(dbName);
        CustomerDao customerDao = new CustomerDao(dbName);
        Scanner scanner = new Scanner(System.in);
        mainMenu(scanner,companyDao,carDao, customerDao);



    }

    public static void mainMenu(Scanner scanner, CompanyDao companyDao, CarDao carDao, CustomerDao customerDao) {
        System.out.println("1. Log in as a manager");
        System.out.println("2. Log in as a customer");
        System.out.println("3. Create a customer");
        System.out.println("0. Exit");
        System.out.print("Enter option: ");
        int mainOption = scanner.nextInt();
        switch (mainOption) {
            case 0:
                return;
            case 1:
                manageCompany(scanner, companyDao, carDao, customerDao);
                break;
            case 2:
                customerList(scanner, companyDao,carDao,customerDao);
                break;
            case 3:
                createCustomer(scanner, customerDao, companyDao, carDao);
                break;
            default:
                mainMenu(scanner,companyDao,carDao, customerDao);
                break;
        }

    }

    private static void createCustomer(Scanner scanner, CustomerDao customerDao,CompanyDao companyDao, CarDao carDao) {
        System.out.println("Enter the customer name: ");
        scanner.nextLine();
        customerDao.add(scanner.nextLine());
        System.out.println("The customer was added!");
        mainMenu(scanner, companyDao,carDao,customerDao);

    }

    private static void customerList(Scanner scanner, CompanyDao companyDao, CarDao carDao, CustomerDao customerDao){
        List<Customer> customers = new ArrayList<>(customerDao.getAllCustomers());
        if(customers.isEmpty()) {
            System.out.println("The customer list is empty!");
            mainMenu(scanner, companyDao,carDao,customerDao);
        } else {
            System.out.println("Choose the customer:");
            for (Customer customer : customers) {
                System.out.println(customers.indexOf(customer) + 1 + ". " + customer.getName());
            }
            System.out.println("0.Back");
            int customerOption = scanner.nextInt();
            if(customerOption == 0) {
                mainMenu(scanner, companyDao, carDao , customerDao);
            } else {
                Customer chosenCustomer = customers.get(customerOption - 1);
                manageCustomer(scanner,companyDao, carDao, customerDao, chosenCustomer);
            }
        }
    }
    private static void manageCustomer(Scanner scanner, CompanyDao companyDao, CarDao carDao, CustomerDao customerDao, Customer customer) {
        System.out.println("""
                1. Rent a car
                2. Return a rented car
                3. My rented car
                0. Back""");
        int option = scanner.nextInt();
        switch (option) {
            case 0 -> mainMenu(scanner, companyDao, carDao, customerDao);
            case 1 -> rentCar(scanner, companyDao,carDao,customerDao, customer);
            case 2 -> returnCar(scanner, companyDao,carDao,customerDao, customer);
            case 3 -> getRentedCar(scanner, companyDao,carDao,customerDao, customer);
        }
    }

    private static void returnCar(Scanner scanner, CompanyDao companyDao, CarDao carDao, CustomerDao customerDao, Customer customer) {

        if(customer.getRentedCarId() == 0) {
                System.out.println("You didn't rent a car!");
            } else {
                customerDao.returnCar(customer.getId());
                customer.setRentedCarId(0);
            System.out.println("You've returned a rented car!");
            }
        manageCustomer(scanner, companyDao, carDao, customerDao, customer);
    }

    public static void rentCar(Scanner scanner, CompanyDao companyDao, CarDao carDao, CustomerDao customerDao, Customer customer) {
        List<Company> companies = new ArrayList<>(companyDao.getAllCompanies());

        if (companies.isEmpty()) {
            System.out.println("The company list is empty");
        } else if(customer.getRentedCarId() != 0) {
            System.out.println("You've already rented a car!");
        }else {
            System.out.println("Choose the company:");
            for (Company company : companies) {
                System.out.println(company.getId() + ". " + company.getName());
            }
            System.out.println("0. Back");

            int companyOption = scanner.nextInt();
            if (companyOption == 0) {
                manageCompany(scanner, companyDao, carDao, customerDao);
            } else {
                Company company = companies.get(companyOption - 1);
                List<Car> availableCars = new ArrayList<>(carDao.availableCars(company.getId()));
                if(availableCars.isEmpty()) {
                    System.out.println("No available cars in the " + company.getName() + " company");
                } else {
                    System.out.println("Choose a car:");
                    for(Car car : availableCars) {
                        System.out.println(availableCars.indexOf(car) + 1 + ". " + car.getName());
                    }
                    System.out.println("0. Back");
                    int carOption = scanner.nextInt();
                    if(carOption == 0) {
                        manageCustomer(scanner, companyDao, carDao, customerDao, customer);
                    } else {
                        Car chosenCar = availableCars.get(carOption - 1);
                        customerDao.rentCar(chosenCar.getId(), customer.getId());
                        customer.setRentedCarId(chosenCar.getId());
                        //System.out.println(customer.getName() +" car: " + chosenCar.getName());
                        System.out.println("You rented " + "'" + chosenCar.getName() + "'");

                    }
                }
            }
        }
        manageCustomer(scanner, companyDao,carDao, customerDao, customer);

    }

    private static void getRentedCar(Scanner scanner, CompanyDao companyDao, CarDao carDao, CustomerDao customerDao, Customer customer) {
        if(customer.getRentedCarId() == 0) {
            System.out.println("You didn't rent a car!");
            System.out.println(customer.getRentedCarId());
        } else {
            Car car = carDao.getRentedCar(customer.getRentedCarId());
            Company company = companyDao.getById(car.getCompanyId());
            System.out.println("Your rented car:\n" +
                    car.getName() +
                    "\nCompany:\n" +
                    company.getName());
        }
        manageCustomer(scanner, companyDao, carDao, customerDao, customer);
    }

    private static void manageCompany(Scanner scanner, CompanyDao companyDao, CarDao carDao, CustomerDao customerDao) {
            System.out.println("1. Company list");
            System.out.println("2. Create a company");
            System.out.println("0. Back");
            System.out.print("Enter option: ");
            int managerOption = scanner.nextInt();

        switch (managerOption) {
            case 0 -> mainMenu(scanner, companyDao, carDao, customerDao);
            case 1 -> manageCompanyList(scanner, companyDao, carDao, customerDao);
            case 2 -> createCompany(scanner, companyDao, carDao, customerDao);
            default -> manageCompany(scanner, companyDao, carDao, customerDao);
        }
        }

    private static void createCompany(Scanner scanner, CompanyDao companyDao, CarDao carDao, CustomerDao customerDao) {
        System.out.println("Enter the company name:");
        scanner.nextLine();
        String name = scanner.nextLine();
        companyDao.createCompany(new Company(name));
        System.out.println("The company was created!");
        manageCompany(scanner, companyDao, carDao, customerDao);
    }

    private static void manageCompanyList(Scanner scanner, CompanyDao companyDao, CarDao carDao, CustomerDao customerDao) {

            List<Company> companies = new ArrayList<>(companyDao.getAllCompanies());
            if (companies.isEmpty()) {
                System.out.println("The company list is empty");
            } else {
                System.out.println("Choose the company:");
                for (Company company : companies) {
                    System.out.println(company.getId() + ". " + company.getName());
                }
                System.out.println("0. Back");

                int companyOption = scanner.nextInt();
                if (companyOption == 0) {
                    manageCompany(scanner, companyDao, carDao, customerDao);
                } else {
                    Company company = companyDao.getById(companyOption);
                    manageCar(scanner, carDao, companyDao, company, customerDao);
                }
            }
            manageCompany(scanner,companyDao,carDao, customerDao);
    }
    private static void manageCar(Scanner scanner,CarDao carDao,CompanyDao companyDao ,Company company, CustomerDao customerDao) {
            System.out.println("'" + company.getName() + "' " + "company");
            System.out.println("1. Car list");
            System.out.println("2. Create a car");
            System.out.println("0. Back");

            int carOption = scanner.nextInt();

        switch (carOption) {
            case 0 -> manageCompany(scanner, companyDao, carDao,customerDao);
            case 1 -> manageCarList(carDao, company,companyDao, customerDao);
            case 2 -> addCar(scanner, company, carDao, companyDao, customerDao);
            default -> manageCar(scanner, carDao, companyDao, company, customerDao);
        }
    }

    private static void addCar(Scanner scanner, Company company, CarDao carDao, CompanyDao companyDao, CustomerDao customerDao) {
        System.out.println("Enter the car name:");
        scanner.nextLine();
        String name = scanner.nextLine();
        carDao.addCar(new Car(name, company.getId()));
        System.out.println("The car was added!");

        manageCar(scanner, carDao, companyDao, company, customerDao);
    }

    private static void manageCarList(CarDao carDao, Company company, CompanyDao companyDao, CustomerDao customerDao) {
        List<Car> cars = new ArrayList<>(carDao.getCarsByCompanyId(company.getId()));
        if (cars.isEmpty()) {
            System.out.println("The car list is empty!");
        } else {
            System.out.println("Car list:");
            int countCars = 1;
            for (Car car : cars) {
                System.out.println(countCars + ". " + car.getName());
                countCars++;
            }
        }

        manageCar(new Scanner(System.in), carDao, companyDao, company, customerDao );
    }



    private static String parseDatabaseName(String[] args, String defaultDbName) {
        String dbName = defaultDbName;
        for (int i = 0; i < args.length - 1; i++) {
            if ("-databaseFileName".equals(args[i]) && !args[i + 1].isEmpty()) {
                dbName = args[i + 1];
            }
        }
        return dbName;
    }


}