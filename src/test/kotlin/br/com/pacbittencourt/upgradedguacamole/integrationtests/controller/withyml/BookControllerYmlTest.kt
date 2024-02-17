package br.com.pacbittencourt.upgradedguacamole.integrationtests.controller.withyml

import br.com.pacbittencourt.upgradedguacamole.integrationtests.ConfigsTest
import br.com.pacbittencourt.upgradedguacamole.integrationtests.controller.withyml.mapper.YMLMapper
import br.com.pacbittencourt.upgradedguacamole.integrationtests.testcontainers.AbstractIntegrationTest
import br.com.pacbittencourt.upgradedguacamole.integrationtests.vo.AccountCredentialsVO
import br.com.pacbittencourt.upgradedguacamole.integrationtests.vo.BookVO
import br.com.pacbittencourt.upgradedguacamole.integrationtests.vo.TokenVO
import br.com.pacbittencourt.upgradedguacamole.integrationtests.vo.wrappers.ResultBookYml
import io.restassured.RestAssured.given
import io.restassured.builder.RequestSpecBuilder
import io.restassured.config.EncoderConfig
import io.restassured.config.RestAssuredConfig
import io.restassured.filter.log.LogDetail
import io.restassured.filter.log.RequestLoggingFilter
import io.restassured.filter.log.ResponseLoggingFilter
import io.restassured.http.ContentType
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
class BookControllerYmlTest : AbstractIntegrationTest() {

    private lateinit var objectMapper: YMLMapper
    private lateinit var specification: RequestSpecification
    private lateinit var config: RestAssuredConfig

    private val now = Date()
    private var bookVO = BookVO(
        title = "Livro",
        author = "Autor",
        price = 10.0,
        launchDate = now
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
            .setBasePath("/api/book/v1")
            .setPort(ConfigsTest.SERVER_PORT)
            .addFilter(RequestLoggingFilter(LogDetail.ALL))
            .addFilter(ResponseLoggingFilter(LogDetail.ALL))
            .build()

    }

    @Test
    @Order(0)
    fun testCreateBook() {
        val result = given()
            .config(config)
            .spec(specification)
            .body(bookVO, objectMapper)
            .`when`()
            .post()
            .then()
            .statusCode(200)
            .extract()
            .body()
            .`as`(BookVO::class.java, objectMapper)

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
        val result = given()
            .config(config)
            .spec(specification)
            .pathParam("id", bookVO.id)
            .`when`()
            .get("{id}")
            .then()
            .statusCode(200)
            .extract()
            .body()
            .`as`(BookVO::class.java, objectMapper)

        assertNotNull(result)
        assertNotNull(result.id)
        assertEquals(result.title, bookVO.title)
        assertEquals(result.author, bookVO.author)
        assertEquals(result.price, bookVO.price)
        assertEquals(result.launchDate, bookVO.launchDate)
    }

    @Test
    @Order(2)
    fun testFindAllBooks() {
        val resultYml = given()
            .config(config)
            .spec(specification)
            .`when`()
            .get()
            .then()
            .statusCode(200)
            .extract()
            .body()
            .`as`(ResultBookYml::class.java, objectMapper)

        assertNotNull(resultYml)
        assert(resultYml.content!!.isNotEmpty())

        val bookOne = resultYml!!.content!![0]
        assert(bookOne.title == "Big Data: como extrair volume, variedade, velocidade e valor da avalanche de informação cotidiana")
        assert(bookOne.price == 54.0)
        assert(bookOne.author == "Viktor Mayer-Schonberger e Kenneth Kukier")
    }

    @Test
    @Order(3)
    fun testUpdatePerson() {
        val newAuthor = "Brasil"
        bookVO.author = newAuthor

        val bookResult = given()
            .config(config)
            .spec(specification)
            .body(bookVO, objectMapper)
            .`when`()
            .put()
            .then()
            .statusCode(200)
            .extract()
            .body()
            .`as`(BookVO::class.java, objectMapper)

        assertNotNull(bookResult)
        assertEquals(bookResult.author, newAuthor)
    }

    @Test
    @Order(4)
    fun testDeletePerson() {
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