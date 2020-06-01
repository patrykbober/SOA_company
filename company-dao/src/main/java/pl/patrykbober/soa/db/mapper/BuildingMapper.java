package pl.patrykbober.soa.db.mapper;

import pl.patrykbober.soa.jpa.BuildingEntity;
import pl.patrykbober.soa.model.Building;

public class BuildingMapper {

    public static BuildingEntity toJpa(Building building) {
        return BuildingEntity.builder()
                .id(building.getId())
                .name(building.getName())
                .address(building.getAddress())
                .build();
    }

    public static Building toModel(BuildingEntity building) {
        return Building.builder()
                .id(building.getId())
                .name(building.getName())
                .address(building.getAddress())
                .build();
    }

}
