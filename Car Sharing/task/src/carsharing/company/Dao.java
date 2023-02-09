package carsharing.company;

import java.util.List;

interface Dao {
    List<Company> getAllCompanies();
    void createCompany(Company company);
}
