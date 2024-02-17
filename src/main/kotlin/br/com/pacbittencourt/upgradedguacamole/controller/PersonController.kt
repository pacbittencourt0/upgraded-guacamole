package br.com.pacbittencourt.upgradedguacamole.controller

import br.com.pacbittencourt.upgradedguacamole.data.vo.v1.PersonVO
import br.com.pacbittencourt.upgradedguacamole.services.PersonService
import br.com.pacbittencourt.upgradedguacamole.util.AppMediaType
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.ArraySchema
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.hateoas.EntityModel
import org.springframework.hateoas.PagedModel
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

//@CrossOrigin
@RestController
@RequestMapping("/api/person/v1")
@Tag(name = "People", description = "Endpoints for Managing People")
class PersonController {

    @Autowired
    private lateinit var service: PersonService

    @GetMapping(
        produces = [AppMediaType.APPLICATION_JSON, AppMediaType.APPLICATION_XML, AppMediaType.APPLICATION_YAML]
    )
    @Operation(
        summary = "Finds all People", description = "Finds all People",
        tags = ["People"],
        responses = [
            ApiResponse(
                description = "Success",
                responseCode = "200",
                content = [Content(array = ArraySchema(schema = Schema(implementation = PersonVO::class)))]
            ),
            ApiResponse(
                description = "No content",
                responseCode = "204",
                content = [Content(schema = Schema(implementation = Unit::class))]
            ),
            ApiResponse(
                description = "Bad request",
                responseCode = "400",
                content = [Content(schema = Schema(implementation = Unit::class))]
            ),
        ]
    )
    fun findAll(
        @RequestParam(value = "page", defaultValue = "0") page: Int,
        @RequestParam(value = "size", defaultValue = "10") size: Int,
        @RequestParam(value = "direction", defaultValue = "asc") direction: String,
    ): ResponseEntity<PagedModel<EntityModel<PersonVO>>> {
        val sortDirection: Sort.Direction =
            if ("desc".equals(direction, ignoreCase = true)) Sort.Direction.DESC else Sort.Direction.ASC
        val pageable: Pageable = PageRequest.of(page, size, Sort.by(sortDirection, "firstName"))
        return ResponseEntity.ok(service.findAll(pageable))
    }

    @GetMapping(
        value = ["findPersonByName/{firstName}"],
        produces = [AppMediaType.APPLICATION_JSON, AppMediaType.APPLICATION_XML, AppMediaType.APPLICATION_YAML]
    )
    @Operation(
        summary = "Finds all People", description = "Finds all People",
        tags = ["People"],
        responses = [
            ApiResponse(
                description = "Success",
                responseCode = "200",
                content = [Content(array = ArraySchema(schema = Schema(implementation = PersonVO::class)))]
            ),
            ApiResponse(
                description = "No content",
                responseCode = "204",
                content = [Content(schema = Schema(implementation = Unit::class))]
            ),
            ApiResponse(
                description = "Bad request",
                responseCode = "400",
                content = [Content(schema = Schema(implementation = Unit::class))]
            ),
        ]
    )
    fun findPersonByName(
        @PathVariable(value = "firstName") firstName: String,
        @RequestParam(value = "page", defaultValue = "0") page: Int,
        @RequestParam(value = "size", defaultValue = "10") size: Int,
        @RequestParam(value = "direction", defaultValue = "asc") direction: String,
    ): ResponseEntity<PagedModel<EntityModel<PersonVO>>> {
        val sortDirection: Sort.Direction =
            if ("desc".equals(direction, ignoreCase = true)) Sort.Direction.DESC else Sort.Direction.ASC
        val pageable: Pageable = PageRequest.of(page, size, Sort.by(sortDirection, "firstName"))
        return ResponseEntity.ok(service.findPersonByName(firstName, pageable))
    }

    @CrossOrigin(origins = ["https://localhost:8080"])
    @GetMapping(
        value = ["/{id}"],
        produces = [AppMediaType.APPLICATION_JSON, AppMediaType.APPLICATION_XML, AppMediaType.APPLICATION_YAML]
    )
    fun getPerson(@PathVariable(value = "id") id: Long): PersonVO {
        return service.findById(id)
    }

    @CrossOrigin(origins = ["https://localhost:8080", "https://erudio.com.br"])
    @PostMapping(
        consumes = [AppMediaType.APPLICATION_JSON, AppMediaType.APPLICATION_XML, AppMediaType.APPLICATION_YAML],
        produces = [AppMediaType.APPLICATION_JSON, AppMediaType.APPLICATION_XML, AppMediaType.APPLICATION_YAML]
    )
    fun createPerson(@RequestBody person: PersonVO): PersonVO {
        return service.create(person)
    }

    @PutMapping(
        consumes = [AppMediaType.APPLICATION_JSON, AppMediaType.APPLICATION_XML, AppMediaType.APPLICATION_YAML],
        produces = [AppMediaType.APPLICATION_JSON, AppMediaType.APPLICATION_XML, AppMediaType.APPLICATION_YAML]
    )
    fun updatePerson(@RequestBody person: PersonVO): PersonVO {
        return service.update(person)
    }

    @PatchMapping(
        value = ["/{id}"],
        produces = [AppMediaType.APPLICATION_JSON, AppMediaType.APPLICATION_XML, AppMediaType.APPLICATION_YAML]
    )
    fun disablePersonById(@PathVariable(value = "id") id: Long): PersonVO {
        return service.disablePerson(id)
    }

    @DeleteMapping(
        value = ["/{id}"],
        produces = [AppMediaType.APPLICATION_JSON, AppMediaType.APPLICATION_XML, AppMediaType.APPLICATION_YAML]
    )
    fun deletePerson(@PathVariable(value = "id") id: Long): ResponseEntity<Any> {
        service.delete(id)
        return ResponseEntity.noContent().build()
    }

}