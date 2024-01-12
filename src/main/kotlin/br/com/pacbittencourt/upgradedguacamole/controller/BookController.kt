package br.com.pacbittencourt.upgradedguacamole.controller

import br.com.pacbittencourt.upgradedguacamole.data.vo.v1.BookVO
import br.com.pacbittencourt.upgradedguacamole.exceptions.model.ExceptionResponse
import br.com.pacbittencourt.upgradedguacamole.services.BookService
import br.com.pacbittencourt.upgradedguacamole.util.AppMediaType
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.ArraySchema
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/book")
@Tag(name = "Book", description = "Endpoints for managing all your books")
class BookController {

    @Autowired
    private lateinit var service: BookService

    @GetMapping(produces = [AppMediaType.APPLICATION_JSON])
    @Operation(
        summary = "Find all books",
        description = "Find all your books",
        tags = ["Book"],
        responses = [
            ApiResponse(
                description = "Success",
                responseCode = "200",
                content = [Content(array = ArraySchema(schema = Schema(implementation = BookVO::class)))]
            ),
            ApiResponse(
                description = "Internal Server Error",
                responseCode = "500",
                content = [Content(schema = Schema(implementation = Unit::class))]
            ),
            ApiResponse(
                description = "Not found",
                responseCode = "404",
                content = [Content(schema = Schema(implementation = ExceptionResponse::class))]
            )
        ]
    )
    fun getAll(): List<BookVO> = service.findAll()

    @GetMapping(
        value = ["/{id}"],
        produces = [AppMediaType.APPLICATION_JSON]
    )
    @Operation(
        summary = "Find one book",
        description = "Find one book given its ID",
        tags = ["Book"],
        responses = [
            ApiResponse(
                description = "Success",
                responseCode = "200",
                content = [Content(schema = Schema(implementation = BookVO::class))]
            ),
            ApiResponse(
                description = "Internal Server Error",
                responseCode = "500",
                content = [Content(schema = Schema(implementation = Unit::class))]
            ),
            ApiResponse(
                description = "Not found",
                responseCode = "404",
                content = [Content(schema = Schema(implementation = ExceptionResponse::class))]
            )
        ]
    )
    fun getBook(@PathVariable(value = "id") id: Long): BookVO = service.findById(id)

    @PostMapping(
        consumes = [AppMediaType.APPLICATION_JSON],
        produces = [AppMediaType.APPLICATION_JSON]
    )
    @Operation(
        summary = "Create one book",
        description = "Create one book in the database",
        tags = ["Book"],
        responses = [
            ApiResponse(
                description = "Success",
                responseCode = "200",
                content = [Content(schema = Schema(implementation = BookVO::class))]
            ),
            ApiResponse(
                description = "Internal Server Error",
                responseCode = "500",
                content = [Content(schema = Schema(implementation = Unit::class))]
            ),
            ApiResponse(
                description = "Bad request",
                responseCode = "400",
                content = [Content(schema = Schema(implementation = ExceptionResponse::class))]
            )
        ]
    )
    fun createBook(@RequestBody book: BookVO): BookVO = service.create(book)
}