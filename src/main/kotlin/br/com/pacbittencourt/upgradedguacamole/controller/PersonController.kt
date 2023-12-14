package br.com.pacbittencourt.upgradedguacamole.controller

import br.com.pacbittencourt.upgradedguacamole.data.vo.v1.PersonVO
import br.com.pacbittencourt.upgradedguacamole.services.PersonService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/person")
class PersonController {

    @Autowired
    private lateinit var service: PersonService

    @GetMapping(
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun findAll(): List<PersonVO> {
        return service.findAll()
    }

    @GetMapping(
        value = ["/{id}"],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun getPerson(@PathVariable(value = "id") id: Long): PersonVO {
        return service.findById(id)
    }

    @PostMapping(
        consumes = [MediaType.APPLICATION_JSON_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun createPerson(@RequestBody person: PersonVO): PersonVO {
        return service.create(person)
    }

    @PutMapping(
        consumes = [MediaType.APPLICATION_JSON_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun updatePerson(@RequestBody person: PersonVO): PersonVO {
        return service.update(person)
    }

    @DeleteMapping(
        value = ["/{id}"],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun deletePerson(@PathVariable(value = "id") id: Long): ResponseEntity<Any> {
        service.delete(id)
        return ResponseEntity.noContent().build()
    }

}