package br.com.pacbittencourt.upgradedguacamole.config

import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.info.License
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class OpenApiConfig {

    @Bean
    fun customOpenApi(): OpenAPI {
        return OpenAPI()
            .info(
                Info().title("Title")
                    .version("v1")
                    .description("Description")
                    .termsOfService("some url")
                    .license(
                        License().name("Apache 2.0")
                            .url("some url")
                    )
            )
    }
}