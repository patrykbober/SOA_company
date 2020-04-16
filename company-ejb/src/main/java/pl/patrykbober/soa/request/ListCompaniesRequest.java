package pl.patrykbober.soa.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.patrykbober.soa.model.CompanyFilter;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ListCompaniesRequest {

    private CompanyFilter filter;

}
