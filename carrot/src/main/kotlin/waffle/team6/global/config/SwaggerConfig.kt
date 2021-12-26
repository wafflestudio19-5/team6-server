package waffle.team6.global.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import springfox.documentation.builders.ApiInfoBuilder
import springfox.documentation.builders.PathSelectors
import springfox.documentation.builders.RequestHandlerSelectors
import springfox.documentation.service.ApiInfo
import springfox.documentation.service.ParameterSpecification
import springfox.documentation.spi.DocumentationType
import springfox.documentation.spring.web.plugins.Docket

@Configuration
class SwaggerConfig {
    @Bean
    fun api(): Docket {
        return Docket(DocumentationType.OAS_30)
            .useDefaultResponseMessages(false)
            .apiInfo(apiInfo())
            .select()
            .apis(RequestHandlerSelectors.basePackage("waffle.team6"))
            .paths(PathSelectors.any())
            .build()
    }

    fun apiInfo(): ApiInfo {
        return ApiInfoBuilder()
            .title(API_NAME)
            .version(API_VERSION)
            .description(API_DESCRIPTION)
            .build()
    }

    companion object {
        private const val API_NAME = "Carrot API"
        private const val API_VERSION = "1.0"
        private const val API_DESCRIPTION = "Carrot Server API: Waffle team 6"
    }
}