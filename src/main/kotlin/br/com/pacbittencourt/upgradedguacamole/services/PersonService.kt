package br.com.pacbittencourt.upgradedguacamole.services

import br.com.pacbittencourt.upgradedguacamole.exceptions.ResourceNotFoundException
import br.com.pacbittencourt.upgradedguacamole.model.Person
import br.com.pacbittencourt.upgradedguacamole.repository.PersonRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.logging.Logger

@Service
class PersonService {

    @Autowired
    private lateinit var repository: PersonRepository

    private val logger = Logger.getLogger(PersonService::class.java.name)

    fun findAll(): List<Person> {
        logger.info("Finding all people")
        return repository.findAll()
    }

    fun findById(id: Long): Person {
        logger.info("Finding one person")
        return repository.findById(id)
            .orElseThrow { ResourceNotFoundException("No records found!") }
    }

    fun create(person: Person): Person {
        logger.info("Creating person with name: ${person.firstName}")
        return repository.save(person)
    }

    fun update(person: Person): Person {
        logger.info("Updating person with id: ${person.id}")
        val entity = findById(person.id).apply {
            firstName = person.firstName
            lastName = person.lastName
            address = person.address
            gender = person.gender
        }
        return repository.save(entity)
    }

    fun delete(id: Long) {
        logger.info("Deleting person with id: $id")
        val entity = findById(id)
        repository.delete(entity)
    }

}