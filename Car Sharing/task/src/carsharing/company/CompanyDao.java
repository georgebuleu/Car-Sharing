package carsharing.company;

import java.util.List;

interface CompanyDao {
    List<Company> getAllCompanies();
    void createCompany(Company company);
}
