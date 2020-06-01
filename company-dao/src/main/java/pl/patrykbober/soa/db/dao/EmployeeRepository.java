package pl.patrykbober.soa.db.dao;

import lombok.extern.java.Log;
import pl.patrykbober.soa.jpa.*;

import javax.ejb.Stateless;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

@Log
@Stateless
public class EmployeeRepository extends AbstractRepository {

    public Long create(EmployeeEntity employee) {
        log.info(".create invoked for employee " + employee.getFirstName() + " " + employee.getLastName());
        entityManager.persist(employee);
        return employee.getId();
    }

    public List<EmployeeEntity> getByCompanyId(Long companyId) {
        log.info(".getByCompanyId invoked for id " + companyId);

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<EmployeeEntity> cq = cb.createQuery(EmployeeEntity.class);
        Root<EmployeeEntity> root = cq.from(EmployeeEntity.class);

        cq.where(cb.equal(root.join(EmployeeEntity_.company).get(CompanyEntity_.id), companyId));
        return entityManager.createQuery(cq).getResultList();
    }

}
