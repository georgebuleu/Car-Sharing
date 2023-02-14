package carsharing;

import carsharing.car.Car;
import carsharing.car.CarDao;
import carsharing.company.Company;
import carsharing.company.CompanyDao;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        String dbName = "test";
        for(int i = 0; i < args.length - 1; i++) {
            if("-databaseFileName".equals(args[i]) && !args[i + 1].isEmpty())
                dbName = args[i+1];
        }
        @SuppressWarnings("unused")
        InitDatabase initDb = new InitDatabase(dbName);
        CompanyDao companyDao = new CompanyDao(dbName);
        CarDao carDao = new CarDao(dbName);

        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("1. Log in as a manager");
            System.out.println("0. Exit");
            System.out.print("Enter option: ");
            int mainOption = scanner.nextInt();

            if (mainOption == 0) {
                break;
            } else if (mainOption == 1) {
                companyList:
                while (true) {
                    System.out.println("1. Company list");
                    System.out.println("2. Create a company");
                    System.out.println("0. Back");
                    System.out.print("Enter option: ");
                    int managerOption = scanner.nextInt();

                    if (managerOption == 0) {
                            break;
                    } else if (managerOption == 1) {

                        while(true){
                        List<Company> companies = new ArrayList<>(companyDao.getAllCompanies());
                        if (companies.isEmpty()) {
                            System.out.println("The company list is empty");
                            break;
                        } else {
                            System.out.println("Choose the company:");
                            for (Company comp : companies) {
                                System.out.println(comp.getId() + ". " + comp.getName());
                            }
                            System.out.println("0. Back");

                                int companyOption = scanner.nextInt();
                                if (companyOption == 0) {
                                        break;
                                } else {
                                    while (true) {
                                        Company company = companyDao.getById(companyOption);
                                        System.out.println("'" + company.getName() + "' " + "company");
                                        System.out.println("1. Car list");
                                        System.out.println("2. Create a car");
                                        System.out.println("0. Back");
                                        int carOption = scanner.nextInt();

                                        if (carOption == 0) {
                                                continue companyList;
                                        } else if (carOption == 1) {
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
                                        } else if (carOption == 2) {
                                            System.out.println("Enter the car name:");
                                            scanner.nextLine();
                                            String name = scanner.nextLine();
                                            carDao.addCar(new Car(name, company.getId()));
                                            System.out.println("The car was added!");

                                        }
                                    }

                                }
                            }
                        }
                    } else if (managerOption == 2) {
                        System.out.println("Enter the company name:");
                        scanner.nextLine();
                        String name = scanner.nextLine();
                        companyDao.createCompany(new Company(name));
                        System.out.println("The company was created!");

                    }
                }
            }
        }

    }


}