package pl.patrykbober.soa.db.dao;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

public class AbstractRepository {

    @PersistenceContext(unitName = "CompanyUnit")
    protected EntityManager entityManager;

    public EntityManager getEntityManager() {
        return entityManager;
    }

    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    protected <T> Optional<T> checkForSingleResult(List<T> resultList) {
        return resultList.isEmpty() ? Optional.empty() : Optional.of(resultList.get(0));
    }
}
