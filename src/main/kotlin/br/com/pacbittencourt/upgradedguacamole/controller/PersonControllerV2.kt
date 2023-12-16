package br.com.pacbittencourt.upgradedguacamole.controller

import br.com.pacbittencourt.upgradedguacamole.data.vo.v2.PersonVO
import br.com.pacbittencourt.upgradedguacamole.services.PersonService
import br.com.pacbittencourt.upgradedguacamole.util.AppMediaType
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/person/v2")
class PersonControllerV2 {

    @Autowired
    private lateinit var service: PersonService

    @PostMapping(
        consumes = [AppMediaType.APPLICATION_JSON, AppMediaType.APPLICATION_XML, AppMediaType.APPLICATION_YAML],
        produces = [AppMediaType.APPLICATION_JSON, AppMediaType.APPLICATION_XML, AppMediaType.APPLICATION_YAML]
    )
    fun createPersonV2(@RequestBody person: PersonVO): PersonVO {
        return service.createV2(person)
    }
}