package carsharing.company;

import java.util.List;

interface Dao {
    List<Company> getAllCompanies();
    Company getById(int id);
    void createCompany(Company company);
}
