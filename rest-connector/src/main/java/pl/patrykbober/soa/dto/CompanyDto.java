package pl.patrykbober.soa.dto;

import lombok.*;

import java.util.List;

@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CompanyDto {

    private Long id;
    private String name;
    private String city;
    private List<EmployeeDto> employees;

}
