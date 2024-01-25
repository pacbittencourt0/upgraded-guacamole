package br.com.pacbittencourt.upgradedguacamole.integrationtests.controller.cors.withjson

import br.com.pacbittencourt.upgradedguacamole.integrationtests.ConfigsTest
import br.com.pacbittencourt.upgradedguacamole.integrationtests.testcontainers.AbstractIntegrationTest
import br.com.pacbittencourt.upgradedguacamole.integrationtests.vo.AccountCredentialsVO
import br.com.pacbittencourt.upgradedguacamole.integrationtests.vo.PersonVO
import br.com.pacbittencourt.upgradedguacamole.integrationtests.vo.TokenVO
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import io.restassured.RestAssured.given
import io.restassured.builder.RequestSpecBuilder
import io.restassured.filter.log.LogDetail
import io.restassured.filter.log.RequestLoggingFilter
import io.restassured.filter.log.ResponseLoggingFilter
import io.restassured.specification.RequestSpecification
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.*
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class PersonControllerCorsWithJson : AbstractIntegrationTest() {

    private lateinit var specification: RequestSpecification
    private lateinit var objectMapper: ObjectMapper
    private lateinit var person: PersonVO
    private lateinit var token: String

    @BeforeAll
    fun setupTests() {
        objectMapper = ObjectMapper()
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
        person = PersonVO()
        token = ""
    }

    @Test
    @Order(0)
    fun authorization() {
        val user = AccountCredentialsVO(
            username = "pacb",
            password = "pedro123"
        )

        token = given()
            .basePath("/auth/signin")
            .port(ConfigsTest.SERVER_PORT)
            .contentType(ConfigsTest.CONTENT_TYPE_JSON)
            .body(user)
            .`when`()
            .post()
            .then()
            .statusCode(200)
            .extract()
            .body()
            .`as`(TokenVO::class.java)
            .accessToken!!
    }

    @Test
    @Order(1)
    fun testCreate() {
        mockPerson()

        specification = RequestSpecBuilder()
            .addHeader(
                ConfigsTest.HEADER_PARAM_ORIGIN,
                ConfigsTest.ORIGIN_ERUDIO
            )
            .addHeader(
                ConfigsTest.HEADER_PARAM_AUTHOZIATION, "Bearer $token"
            )
            .setBasePath("/api/person/v1")
            .setPort(ConfigsTest.SERVER_PORT)
            .addFilter(RequestLoggingFilter(LogDetail.ALL))
            .addFilter(ResponseLoggingFilter(LogDetail.ALL))
            .build()


        val content = given()
            .spec(specification)
            .contentType(ConfigsTest.CONTENT_TYPE_JSON)
            .body(person)
            .`when`()
            .post()
            .then()
            .statusCode(200)
            .extract().body().asString()

        val createdPerson = objectMapper.readValue(
            content, PersonVO::class.java
        )

        person = createdPerson

        assertNotNull(createdPerson.id)
        assertNotNull(createdPerson.firstName)
        assertNotNull(createdPerson.lastName)
        assertNotNull(createdPerson.address)
        assertNotNull(createdPerson.gender)

        assertTrue(createdPerson.id > 0)

        assertEquals("Nelson", createdPerson.firstName)
        assertEquals("Piquet", createdPerson.lastName)
        assertEquals("Brasil", createdPerson.address)
        assertEquals("male", createdPerson.gender)
    }

    @Test
    @Order(2)
    fun testCreateWithWrongOrigin() {
        mockPerson()

        specification = RequestSpecBuilder()
            .addHeader(
                ConfigsTest.HEADER_PARAM_ORIGIN,
                ConfigsTest.ORIGIN_SEMERU
            )
            .addHeader(
                ConfigsTest.HEADER_PARAM_AUTHOZIATION, "Bearer $token"
            )
            .setBasePath("/api/person/v1")
            .setPort(ConfigsTest.SERVER_PORT)
            .addFilter(RequestLoggingFilter(LogDetail.ALL))
            .addFilter(ResponseLoggingFilter(LogDetail.ALL))
            .build()


        val content = given()
            .spec(specification)
            .contentType(ConfigsTest.CONTENT_TYPE_JSON)
            .body(person)
            .`when`()
            .post()
            .then()
            .statusCode(403)
            .extract().body().asString()

        assertEquals("Invalid CORS request", content)
    }

    @Test
    @Order(3)
    fun findById() {
        mockPerson()

        specification = RequestSpecBuilder()
            .addHeader(
                ConfigsTest.HEADER_PARAM_ORIGIN,
                ConfigsTest.ORIGIN_LOCALHOST
            )
            .addHeader(
                ConfigsTest.HEADER_PARAM_AUTHOZIATION, "Bearer $token"
            )
            .setBasePath("/api/person/v1")
            .setPort(ConfigsTest.SERVER_PORT)
            .addFilter(RequestLoggingFilter(LogDetail.ALL))
            .addFilter(ResponseLoggingFilter(LogDetail.ALL))
            .build()


        val content = given()
            .spec(specification)
            .contentType(ConfigsTest.CONTENT_TYPE_JSON)
            .pathParam("id", person.id)
            .`when`()["{id}"]
            .then()
            .statusCode(200)
            .extract().body().asString()

        val createdPerson = objectMapper.readValue(
            content, PersonVO::class.java
        )
        assertNotNull(createdPerson.id)
        assertNotNull(createdPerson.firstName)
        assertNotNull(createdPerson.lastName)
        assertNotNull(createdPerson.address)
        assertNotNull(createdPerson.gender)

        assertTrue(createdPerson.id > 0)

        assertEquals("Nelson", createdPerson.firstName)
        assertEquals("Piquet", createdPerson.lastName)
        assertEquals("Brasil", createdPerson.address)
        assertEquals("male", createdPerson.gender)
    }

    @Test
    @Order(4)
    fun findByIdWithWrongOrigin() {
        mockPerson()

        specification = RequestSpecBuilder()
            .addHeader(
                ConfigsTest.HEADER_PARAM_ORIGIN,
                ConfigsTest.ORIGIN_SEMERU
            )
            .addHeader(
                ConfigsTest.HEADER_PARAM_AUTHOZIATION, "Bearer $token"
            )
            .setBasePath("/api/person/v1")
            .setPort(ConfigsTest.SERVER_PORT)
            .addFilter(RequestLoggingFilter(LogDetail.ALL))
            .addFilter(ResponseLoggingFilter(LogDetail.ALL))
            .build()


        val content = given()
            .spec(specification)
            .contentType(ConfigsTest.CONTENT_TYPE_JSON)
            .pathParam("id", person.id)
            .`when`()["{id}"]
            .then()
            .statusCode(403)
            .extract().body().asString()

        assertEquals("Invalid CORS request", content)
    }

    private fun mockPerson() {
        person.firstName = "Nelson"
        person.lastName = "Piquet"
        person.address = "Brasil"
        person.gender = "male"
    }

}