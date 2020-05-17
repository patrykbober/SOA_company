package pl.patrykbober.soa.service;

import lombok.NoArgsConstructor;
import pl.patrykbober.soa.exception.RestException;
import pl.patrykbober.soa.model.Company;
import pl.patrykbober.soa.model.CompanyFilter;
import pl.patrykbober.soa.model.Employee;
import pl.patrykbober.soa.repository.CompanyRepository;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.*;

@Stateless
@NoArgsConstructor
public class CompanyServiceRest {

    private FileServiceRest fileService;
    private CompanyRepository companyRepository;

    @Inject
    public CompanyServiceRest(FileServiceRest fileService, CompanyRepository companyRepository) {
        this.fileService = fileService;
        this.companyRepository = companyRepository;
    }

    public Long create(Company company) {
        return companyRepository.create(company);
    }

    public void update(Long id, Company company) {
        companyRepository.update(id, company);
    }

    public void delete(Long id) {
        companyRepository.delete(id);
    }

    public Company findById(Long id) {
        return companyRepository.getById(id).orElseThrow(() -> new RestException("Resource not found"));
    }

    public List<Company> findAll() {
        return companyRepository.getAll();
    }

    public List<Company> findFiltered(CompanyFilter filter) {
        return companyRepository.getFiltered(filter);
    }

    public Long addEmployee(Long companyId, Employee employee) {
        return companyRepository.addEmployee(companyId, employee);
    }

    public byte[] getLogo(Long id) {
        Company company = findById(id);
        return fileService.getFileBase64Content(company.getLogoPath());
    }

}
