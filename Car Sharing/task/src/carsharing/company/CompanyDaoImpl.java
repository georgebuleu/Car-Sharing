package carsharing.company;

import carsharing.DatabaseConfig;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CompanyDaoImpl implements CompanyDao{
    private Connection connection;
    public CompanyDaoImpl(String dbName) {
        try {
            Class.forName(DatabaseConfig.JDBC_DRIVER);
            connection = DriverManager.getConnection(DatabaseConfig.DB_URL + dbName);
            connection.setAutoCommit(true);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Company> getAllCompanies() {
        List<Company> companies = new ArrayList<>();

        try {

            PreparedStatement statement = connection.prepareStatement("SELECT * FROM COMPANY");
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next())  {
                companies.add(new Company(resultSet.getString("NAME")));
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
        return companies;
    }

    @Override
    public void createCompany(Company company) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO COMPANY(NAME) VALUES(?)");
            preparedStatement.setString(1, company.getName());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}