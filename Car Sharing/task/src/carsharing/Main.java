package carsharing;

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

        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("1. Log in as a manager");
            System.out.println("0. Exit");
            System.out.print("Enter option: ");
            int mainOption = scanner.nextInt();

            if (mainOption == 0) {
                break;
            } else if (mainOption == 1) {
                while (true) {
                    System.out.println("1. Company list");
                    System.out.println("2. Create a company");
                    System.out.println("0. Back");
                    System.out.print("Enter option: ");
                    int managerOption = scanner.nextInt();

                    if (managerOption == 0) {
                        break;
                    } else if (managerOption == 1) {
                        List<Company> companies = new ArrayList<>(companyDao.getAllCompanies());
                        if (companies.isEmpty()) {
                            System.out.println("The company list is empty");
                        } else {
                            int count = 1;
                            for (Company comp : companies) {
                                System.out.println(count + ". " + comp.getName());
                                count++;
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