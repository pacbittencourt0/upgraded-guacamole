package br.com.pacbittencourt.upgradedguacamole.integrationtests.controller.withjson

import br.com.pacbittencourt.upgradedguacamole.integrationtests.ConfigsTest
import br.com.pacbittencourt.upgradedguacamole.integrationtests.testcontainers.AbstractIntegrationTest
import br.com.pacbittencourt.upgradedguacamole.integrationtests.vo.AccountCredentialsVO
import br.com.pacbittencourt.upgradedguacamole.integrationtests.vo.BookVO
import br.com.pacbittencourt.upgradedguacamole.integrationtests.vo.TokenVO
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import io.restassured.RestAssured.given
import io.restassured.builder.RequestSpecBuilder
import io.restassured.filter.log.LogDetail
import io.restassured.filter.log.RequestLoggingFilter
import io.restassured.filter.log.ResponseLoggingFilter
import io.restassured.specification.RequestSpecification
import java.util.Date
import org.junit.jupiter.api.Assertions.assertEquals
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
class BookControllerJsonTest : AbstractIntegrationTest() {

    private lateinit var objectMapper: ObjectMapper
    private lateinit var specification: RequestSpecification

    private val now = Date()
    private var bookVO = BookVO(
        title = "Livro",
        author = "Autor",
        price = 10.0,
        launchDate = now
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
            .setAccept(ConfigsTest.CONTENT_TYPE_JSON)
            .setContentType(ConfigsTest.CONTENT_TYPE_JSON)
            .setBasePath("/api/book/v1")
            .setPort(ConfigsTest.SERVER_PORT)
            .addFilter(RequestLoggingFilter(LogDetail.ALL))
            .addFilter(ResponseLoggingFilter(LogDetail.ALL))
            .build()

    }

    @Test
    @Order(0)
    fun testCreateBook() {
        val response = given()
            .spec(specification)
            .body(bookVO)
            .`when`()
            .post()
            .then()
            .statusCode(200)
            .extract()
            .body()
            .asString()
        val result = objectMapper.readValue(response, BookVO::class.java)

        assertNotNull(result.id)
        assert(result.id > 0)
        assertEquals(result.title, "Livro")
        assertEquals(result.author, "Autor")
        assertEquals(result.price, 10.0)
        assertEquals(result.launchDate, now)

        bookVO = result
    }


    @Test
    @Order(1)
    fun testGetBook() {
        val response = given()
            .spec(specification)
            .pathParam("id", bookVO.id)
            .`when`()
            .get("{id}")
            .then()
            .statusCode(200)
            .extract()
            .body()
            .asString()

        val result = objectMapper.readValue(response, BookVO::class.java)

        assertNotNull(result)
        assertNotNull(result.id)
        assertEquals(result.title, bookVO.title)
        assertEquals(result.author, bookVO.author)
        assertEquals(result.price, bookVO.price)
        assertEquals(result.launchDate, bookVO.launchDate)
    }

        @Test
        @Order(2)
        fun testFindAllBook() {
            val response = given()
                .spec(specification)
                .`when`()
                .get()
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString()
            val bookList = objectMapper.readValue(response, Array<BookVO>::class.java)

            assertNotNull(bookList)
            assert(bookList.isNotEmpty())
        }

        @Test
        @Order(3)
        fun testUpdateBook() {
            val newAuthor = "Zezé"
            bookVO.author = newAuthor

            val result = given()
                .spec(specification)
                .body(bookVO)
                .`when`()
                .put()
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString()

            val bookResult = objectMapper.readValue(result, BookVO::class.java)

            assertNotNull(result)
            assertEquals(bookResult.author, newAuthor)
        }

        @Test
        @Order(4)
        fun testDeleteBook() {
            given()
                .spec(specification)
                .pathParam("id", bookVO.id)
                .`when`()
                .delete("{id}")
                .then()
                .statusCode(204)

            given()
                .spec(specification)
                .pathParam("id", bookVO.id)
                .`when`()
                .get("{id}")
                .then()
                .statusCode(404)
        }

}