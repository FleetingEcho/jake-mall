
package com.jake.mall.config;

import com.github.xiaoymin.knife4j.spring.annotations.EnableKnife4j;
import com.jake.mall.entity.AdminUserToken;
import com.jake.mall.entity.MallUser;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import springfox.bean.validators.configuration.BeanValidatorPluginsConfiguration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.RequestParameterBuilder;
import springfox.documentation.oas.annotations.EnableOpenApi;
import springfox.documentation.schema.ScalarType;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ParameterType;
import springfox.documentation.service.RequestParameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableOpenApi
@EnableKnife4j
@Import(BeanValidatorPluginsConfiguration.class)
public class Swagger3Config {

    @Bean
    public Docket api() {
            return new Docket(DocumentationType.OAS_30)
                    .apiInfo(apiInfo())
                    .ignoredParameterTypes(MallUser.class, AdminUserToken.class)
                    .select()
                    .apis(RequestHandlerSelectors.basePackage("com.jake.mall.api"))
                    .paths(PathSelectors.any())
                    .build()
                    .globalRequestParameters(getGlobalRequestParameters());
    }

    // Generate global universal parameters
    private List<RequestParameter> getGlobalRequestParameters() {
        List<RequestParameter> parameters = new ArrayList<>();
        parameters.add(new RequestParameterBuilder()
                .name("token")
                .description("Login certification token")
                // not necessary
                .required(false)
                //Request the parameters in the header, other types can be viewed in the ParameterType class
                .in(ParameterType.HEADER)
                .query(q -> q.model(m -> m.scalarModel(ScalarType.STRING)))
                .build());
        return parameters;
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("Interface Document")
                .description("swagger API docs")
                .version("2.0")
                .build();
    }



}
