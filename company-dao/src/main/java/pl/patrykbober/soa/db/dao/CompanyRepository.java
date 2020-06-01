package pl.patrykbober.soa.db.dao;

import lombok.extern.java.Log;
import pl.patrykbober.soa.jpa.CompanyEntity;
import pl.patrykbober.soa.jpa.CompanyEntity_;
import pl.patrykbober.soa.jpa.EmployeeEntity;
import pl.patrykbober.soa.model.CompanyFilter;

import javax.ejb.Stateless;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Optional;

@Log
@Stateless
public class CompanyRepository extends AbstractRepository {

    public Long create(CompanyEntity company) {
        log.info(".create invoked for company " + company.getName());
        company.getEmployees().forEach(company::addEmployee);
        entityManager.persist(company);
        return company.getId();
    }

    public Optional<CompanyEntity> getById(Long id) {
        log.info(".getById invoked for id " + id);
        CompanyEntity company = entityManager.find(CompanyEntity.class, id);

        return Optional.ofNullable(company);
    }

    public List<CompanyEntity> getAll() {
        log.info(".getAll invoked");

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<CompanyEntity> cq = cb.createQuery(CompanyEntity.class);
        Root<CompanyEntity> root = cq.from(CompanyEntity.class);
        cq.select(root);

        return entityManager.createQuery(cq).getResultList();
    }

    public List<CompanyEntity> getFiltered(CompanyFilter filter) {
        log.info(".getFiltered invoked");

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<CompanyEntity> cq = cb.createQuery(CompanyEntity.class);
        Root<CompanyEntity> root = cq.from(CompanyEntity.class);

        if (filter != null && filter.getName() != null) {
            cq.where(cb.like(cb.lower(root.get(CompanyEntity_.name)), "%" + filter.getName().toLowerCase() + "%"));
        }
        if (filter != null && filter.getCity() != null) {
            cq.where(cb.like(cb.lower(root.get(CompanyEntity_.city)), "%" + filter.getCity().toLowerCase() + "%"));
        }

        return entityManager.createQuery(cq).getResultList();
    }

    public void update(CompanyEntity company) {
        log.info(".update invoked for id " + company.getId());
        entityManager.merge(company);
    }

    public void delete(Long id) {
        log.info(".delete invoked for id " + id);
        entityManager.remove(entityManager.find(CompanyEntity.class, id));
    }

}
