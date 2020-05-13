package pl.patrykbober.soa;

import io.swagger.jaxrs.config.BeanConfig;
import io.swagger.jaxrs.listing.ApiListingResource;
import io.swagger.jaxrs.listing.SwaggerSerializers;
import pl.patrykbober.soa.controller.CompanyController;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import java.util.HashSet;
import java.util.Set;

@ApplicationPath("/api")
public class CompanyApplication extends Application {

    public CompanyApplication() {
        BeanConfig beanConfig = new BeanConfig();
        beanConfig.setTitle("Company REST API");
        beanConfig.setVersion("1.0.0");
        beanConfig.setBasePath("/rest-api/api");
        beanConfig.setResourcePackage("pl.patrykbober.soa.controller");
        beanConfig.setPrettyPrint(true);
        beanConfig.setContact("patrykbober@student.agh.edu.pl");
        beanConfig.setScan(true);
    }

    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> resources = new HashSet<>();

        addControllers(resources);
        addSwagger(resources);

        return resources;
    }

    private void addSwagger(Set<Class<?>> resources) {
        resources.add(ApiListingResource.class);
        resources.add(SwaggerSerializers.class);
    }

    private void addControllers(Set<Class<?>> resources) {
        resources.add(CompanyController.class);
    }
}
