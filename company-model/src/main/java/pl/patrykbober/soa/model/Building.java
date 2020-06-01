package pl.patrykbober.soa.model;

import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Building {

    private Long id;
    private String name;
    private String address;

}
