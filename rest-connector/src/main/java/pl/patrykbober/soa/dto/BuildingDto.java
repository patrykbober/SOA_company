package pl.patrykbober.soa.dto;

import lombok.*;

@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class BuildingDto {

    private Long id;
    private String name;
    private String address;

}