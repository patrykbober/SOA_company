package pl.patrykbober.soa.db.dao;

import lombok.extern.java.Log;
import pl.patrykbober.soa.jpa.BuildingEntity;

import javax.ejb.Stateless;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Optional;

@Log
@Stateless
public class BuildingRepository extends AbstractRepository {

    public Long create(BuildingEntity building) {
        log.info(".create invoked for building " + building.getName());
        entityManager.persist(building);
        return building.getId();
    }

    public Optional<BuildingEntity> getById(Long id) {
        log.info(".getById invoked for id " + id);
        BuildingEntity building = entityManager.find(BuildingEntity.class, id);

        return Optional.ofNullable(building);
    }

    public List<BuildingEntity> getAll() {
        log.info(".getAll invoked");

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<BuildingEntity> cq = cb.createQuery(BuildingEntity.class);
        Root<BuildingEntity> root = cq.from(BuildingEntity.class);
        cq.select(root);

        return entityManager.createQuery(cq).getResultList();
    }

    public void update(BuildingEntity building) {
        log.info(".update invoked for id " + building.getId());
        entityManager.merge(building);
    }

    public void delete(Long id) {
        log.info(".delete invoked for id " + id);
        entityManager.remove(entityManager.find(BuildingEntity.class, id));
    }

}
