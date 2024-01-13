package br.com.pacbittencourt.upgradedguacamole.integrationtests.swagger

import br.com.pacbittencourt.upgradedguacamole.integrationtests.ConfigsTest
import br.com.pacbittencourt.upgradedguacamole.integrationtests.testcontainers.AbstractIntegrationTest
import io.restassured.RestAssured
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class SwaggerIntegrationTest : AbstractIntegrationTest() {

    @Test
    fun showDisplaySwaggerUiPage() {
        val content = RestAssured.given()
            .basePath("/swagger-ui/index.html")
            .port(ConfigsTest.SERVER_PORT)
            .`when`().get().then()
            .statusCode(200)
            .extract().body().asString()
        assertTrue(content.contains("Swagger UI"))
    }

}