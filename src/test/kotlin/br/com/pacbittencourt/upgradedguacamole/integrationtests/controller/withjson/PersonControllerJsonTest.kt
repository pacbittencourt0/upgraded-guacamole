package br.com.pacbittencourt.upgradedguacamole.integrationtests.controller.withjson

import br.com.pacbittencourt.upgradedguacamole.integrationtests.ConfigsTest
import br.com.pacbittencourt.upgradedguacamole.integrationtests.testcontainers.AbstractIntegrationTest
import br.com.pacbittencourt.upgradedguacamole.integrationtests.vo.AccountCredentialsVO
import br.com.pacbittencourt.upgradedguacamole.integrationtests.vo.PersonVO
import br.com.pacbittencourt.upgradedguacamole.integrationtests.vo.TokenVO
import br.com.pacbittencourt.upgradedguacamole.integrationtests.vo.wrappers.WrapperPersonVO
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import io.restassured.RestAssured.given
import io.restassured.builder.RequestSpecBuilder
import io.restassured.filter.log.LogDetail
import io.restassured.filter.log.RequestLoggingFilter
import io.restassured.filter.log.ResponseLoggingFilter
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
class PersonControllerJsonTest : AbstractIntegrationTest() {

    private lateinit var objectMapper: ObjectMapper
    private lateinit var specification: RequestSpecification

    private var personVO = PersonVO(
        firstName = "Pedro",
        lastName = "Bittencourt",
        address = "Juiz de Fora",
        gender = "male",
        enabled = true,
    )

    @BeforeAll
    fun setupTests() {
        objectMapper = ObjectMapper()
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)

        val user = AccountCredentialsVO(
            username = "pacb",
            password = "pedro123"
        )

        val accessToken = given()
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
            .accessToken

        specification = RequestSpecBuilder()
            .addHeader(ConfigsTest.HEADER_PARAM_AUTHOZIATION, "Bearer $accessToken")
            .setBasePath("/api/person/v1")
            .setPort(ConfigsTest.SERVER_PORT)
            .addFilter(RequestLoggingFilter(LogDetail.ALL))
            .addFilter(ResponseLoggingFilter(LogDetail.ALL))
            .build()

    }

    @Test
    @Order(0)
    fun testCreatePerson() {
        val response = given()
            .spec(specification)
            .contentType(ConfigsTest.CONTENT_TYPE_JSON)
            .body(personVO)
            .`when`()
            .post()
            .then()
            .statusCode(200)
            .extract()
            .body()
            .asString()
        val result = objectMapper.readValue(response, PersonVO::class.java)

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
        val response = given()
            .spec(specification)
            .contentType(ConfigsTest.CONTENT_TYPE_JSON)
            .pathParam("id", personVO.id)
            .`when`()
            .get("{id}")
            .then()
            .statusCode(200)
            .extract()
            .body()
            .asString()

        val result = objectMapper.readValue(response, PersonVO::class.java)

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
        val response = given()
            .spec(specification)
            .contentType(ConfigsTest.CONTENT_TYPE_JSON)
            .pathParam("id", personVO.id)
            .`when`()
            .patch("{id}")
            .then()
            .statusCode(200)
            .extract()
            .body()
            .asString()

        val result = objectMapper.readValue(response, PersonVO::class.java)

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
        val response = given()
            .spec(specification)
            .contentType(ConfigsTest.CONTENT_TYPE_JSON)
            .queryParams("page", 3, "size", 6, "direction", "asc")
            .`when`()
            .get()
            .then()
            .statusCode(200)
            .extract()
            .body()
            .asString()
        val wrapper = objectMapper.readValue(response, WrapperPersonVO::class.java)
        val listPerson = wrapper.embedded?.persons

        assertNotNull(listPerson)
        assert(listPerson!!.isNotEmpty())
    }

    @Test
    @Order(4)
    fun testFindByName() {
        val response = given()
            .spec(specification)
            .contentType(ConfigsTest.CONTENT_TYPE_JSON)
            .pathParam("firstName", "ped")
            .queryParams("page", 0, "size", 6, "direction", "asc")
            .`when`()["findPersonByName/{firstName}"]
            .then()
            .statusCode(200)
            .extract()
            .body()
            .asString()
        val wrapper = objectMapper.readValue(response, WrapperPersonVO::class.java)
        val listPerson = wrapper.embedded?.persons

        assertNotNull(listPerson)
        assert(listPerson!!.isNotEmpty())

        val person = listPerson[0]
        assert(person.firstName == "Pedro")
        assert(person.lastName == "Bittencourt")
        assert(person.address == "Juiz de Fora, MG")
        assert(person.gender == "male")
        assert(person.enabled)
    }

    @Test
    @Order(5)
    fun testUpdatePerson() {
        val newAddress = "Brasil"
        personVO.address = newAddress

        val result = given()
            .spec(specification)
            .contentType(ConfigsTest.CONTENT_TYPE_JSON)
            .body(personVO)
            .`when`()
            .put()
            .then()
            .statusCode(200)
            .extract()
            .body()
            .asString()

        val personResult = objectMapper.readValue(result, PersonVO::class.java)

        assertNotNull(result)
        assertEquals(personResult.address, newAddress)
        assertFalse(personResult.enabled)
    }

    @Test
    @Order(6)
    fun testHATEOAS() {
        val response = given()
            .spec(specification)
            .contentType(ConfigsTest.CONTENT_TYPE_JSON)
            .queryParams("page", 3, "size", 6, "direction", "asc")
            .`when`()
            .get()
            .then()
            .statusCode(200)
            .extract()
            .body()
            .asString()

        assert(response.contains(""""_links":{"self":{"href":"http://localhost:8888/api/person/v1/586"}}"""))
        assert(response.contains(""""first":{"href":"http://localhost:8888/api/person/v1?direction=asc&page=0&size=6&sort=firstName,asc"}"""))
        assert(response.contains(""""last":{"href":"http://localhost:8888/api/person/v1?direction=asc&page=167&size=6&sort=firstName,asc"}"""))
        assert(response.contains(""""prev":{"href":"http://localhost:8888/api/person/v1?direction=asc&page=2&size=6&sort=firstName,asc"}"""))
        assert(response.contains(""""page":{"size":6,"totalElements":1007,"totalPages":168,"number":3}"""))
    }

    @Test
    @Order(7)
    fun testDeletePerson() {
        given()
            .spec(specification)
            .contentType(ConfigsTest.CONTENT_TYPE_JSON)
            .pathParam("id", personVO.id)
            .`when`()
            .delete("{id}")
            .then()
            .statusCode(204)

        given()
            .spec(specification)
            .contentType(ConfigsTest.CONTENT_TYPE_JSON)
            .pathParam("id", personVO.id)
            .`when`()
            .get("{id}")
            .then()
            .statusCode(404)
    }



}