package br.com.pacbittencourt.upgradedguacamole.integrationtests.controller.withyml

import br.com.pacbittencourt.upgradedguacamole.integrationtests.ConfigsTest
import br.com.pacbittencourt.upgradedguacamole.integrationtests.controller.withyml.mapper.YMLMapper
import br.com.pacbittencourt.upgradedguacamole.integrationtests.testcontainers.AbstractIntegrationTest
import br.com.pacbittencourt.upgradedguacamole.integrationtests.vo.AccountCredentialsVO
import br.com.pacbittencourt.upgradedguacamole.integrationtests.vo.PersonVO
import br.com.pacbittencourt.upgradedguacamole.integrationtests.vo.TokenVO
import br.com.pacbittencourt.upgradedguacamole.integrationtests.vo.wrappers.ResultYml
import io.restassured.RestAssured.given
import io.restassured.builder.RequestSpecBuilder
import io.restassured.config.EncoderConfig
import io.restassured.config.RestAssuredConfig
import io.restassured.filter.log.LogDetail
import io.restassured.filter.log.RequestLoggingFilter
import io.restassured.filter.log.ResponseLoggingFilter
import io.restassured.http.ContentType
import io.restassured.specification.RequestSpecification
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.MethodOrderer
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestMethodOrder
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class PersonControllerYmlTest : AbstractIntegrationTest() {

    private lateinit var objectMapper: YMLMapper
    private lateinit var specification: RequestSpecification
    private lateinit var config: RestAssuredConfig

    private var personVO = PersonVO(
        firstName = "Pedro",
        lastName = "Bittencourt",
        address = "Juiz de Fora",
        gender = "male",
        enabled = true
    )

    @BeforeAll
    fun setupTests() {
        objectMapper = YMLMapper()
        config = RestAssuredConfig
            .config()
            .encoderConfig(
                EncoderConfig.encoderConfig()
                    .encodeContentTypeAs(ConfigsTest.CONTENT_TYPE_YAML, ContentType.TEXT)
            )

        val user = AccountCredentialsVO(
            username = "pacb",
            password = "pedro123"
        )

        val accessToken = given()
            .config(config)
            .basePath("/auth/signin")
            .port(ConfigsTest.SERVER_PORT)
            .contentType(ConfigsTest.CONTENT_TYPE_YAML)
            .body(user, objectMapper)
            .`when`()
            .post()
            .then()
            .statusCode(200)
            .extract()
            .body()
            .`as`(TokenVO::class.java, objectMapper)
            .accessToken

        specification = RequestSpecBuilder()
            .addHeader(ConfigsTest.HEADER_PARAM_AUTHOZIATION, "Bearer $accessToken")
            .setAccept(ConfigsTest.CONTENT_TYPE_YAML)
            .setContentType(ConfigsTest.CONTENT_TYPE_YAML)
            .setBasePath("/api/person/v1")
            .setPort(ConfigsTest.SERVER_PORT)
            .addFilter(RequestLoggingFilter(LogDetail.ALL))
            .addFilter(ResponseLoggingFilter(LogDetail.ALL))
            .build()

    }

    @Test
    @Order(0)
    fun testCreatePerson() {
        val result = given()
            .config(config)
            .spec(specification)
            .body(personVO, objectMapper)
            .`when`()
            .post()
            .then()
            .statusCode(200)
            .extract()
            .body()
            .`as`(PersonVO::class.java, objectMapper)

        assertNotNull(result.id)
        assert(result.id > 0)
        assertEquals(result.firstName, "Pedro")
        assertEquals(result.lastName, "Bittencourt")
        assertEquals(result.address, "Juiz de Fora")
        assertEquals(result.gender, "male")
        assert(result.enabled)

        personVO = result
    }

    @Test
    @Order(1)
    fun testGetPerson() {
        val result = given()
            .config(config)
            .spec(specification)
            .pathParam("id", personVO.id)
            .`when`()
            .get("{id}")
            .then()
            .statusCode(200)
            .extract()
            .body()
            .`as`(PersonVO::class.java, objectMapper)

        assertNotNull(result)
        assertNotNull(result.id)
        assertEquals(result.firstName, personVO.firstName)
        assertEquals(result.lastName, personVO.lastName)
        assertEquals(result.address, personVO.address)
        assertEquals(result.gender, personVO.gender)
        assert(result.enabled)
    }

    @Test
    @Order(2)
    fun testDisablePerson() {
        val result = given()
            .config(config)
            .spec(specification)
            .pathParam("id", personVO.id)
            .`when`()
            .patch("{id}")
            .then()
            .statusCode(200)
            .extract()
            .body()
            .`as`(PersonVO::class.java, objectMapper)

        assertNotNull(result)
        assertNotNull(result.id)
        assertEquals(result.firstName, personVO.firstName)
        assertEquals(result.lastName, personVO.lastName)
        assertEquals(result.address, personVO.address)
        assertEquals(result.gender, personVO.gender)
        assertFalse(result.enabled)
    }

    @Test
    @Order(3)
    fun testFindAllPerson() {
        val resultYml = given()
            .config(config)
            .spec(specification)
            .`when`()
            .get()
            .then()
            .statusCode(200)
            .extract()
            .body()
            .`as`(ResultYml::class.java, objectMapper)

        assertNotNull(resultYml)
        assert(resultYml.content!!.isNotEmpty())
        val personOne = resultYml.content?.get(0)!!
        assert(personOne.firstName == "Aaron")
        assert(personOne.lastName == "Oddy")
        assert(personOne.address == "01 Colorado Court")
        assert(personOne.gender == "Male")
        assert(!personOne.enabled)
    }

    @Test
    @Order(4)
    fun testFindByName() {
        val resultYml = given()
            .config(config)
            .spec(specification)
            .pathParam("firstName", "ped")
            .queryParams("page", 0, "size", 6, "direction", "asc")
            .`when`()["findPersonByName/{firstName}"]
            .then()
            .statusCode(200)
            .extract()
            .body()
            .`as`(ResultYml::class.java, objectMapper)

        assertNotNull(resultYml)
        assert(resultYml.content!!.isNotEmpty())
        val person = resultYml.content?.get(0)!!
        assert(person.firstName == "Pedro")
        assert(person.lastName == "Bittencourt")
        assert(person.address == "Juiz de Fora, MG")
        assert(person.gender == "male")
        assert(person.enabled)
    }

    @Test
    @Order(4)
    fun testUpdatePerson() {
        val newAddress = "Brasil"
        personVO.address = newAddress

        val personResult = given()
            .config(config)
            .spec(specification)
            .body(personVO, objectMapper)
            .`when`()
            .put()
            .then()
            .statusCode(200)
            .extract()
            .body()
            .`as`(PersonVO::class.java, objectMapper)

        assertNotNull(personResult)
        assertEquals(personResult.address, newAddress)
        assertFalse(personResult.enabled)
    }

    @Test
    @Order(5)
    fun testDeletePerson() {
        given()
            .spec(specification)
            .pathParam("id", personVO.id)
            .`when`()
            .delete("{id}")
            .then()
            .statusCode(204)

        given()
            .spec(specification)
            .pathParam("id", personVO.id)
            .`when`()
            .get("{id}")
            .then()
            .statusCode(404)
    }

}