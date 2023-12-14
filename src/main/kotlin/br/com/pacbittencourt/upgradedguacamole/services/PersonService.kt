package br.com.pacbittencourt.upgradedguacamole.services

import br.com.pacbittencourt.upgradedguacamole.data.vo.v1.PersonVO
import br.com.pacbittencourt.upgradedguacamole.exceptions.ResourceNotFoundException
import br.com.pacbittencourt.upgradedguacamole.mapper.DozerMapper
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

    fun findAll(): List<PersonVO> {
        logger.info("Finding all people")
        return DozerMapper.parseObjectList(repository.findAll(), PersonVO::class.java)
    }

    fun findById(id: Long): PersonVO {
        logger.info("Finding one person")
        return DozerMapper.parseObject(findOne(id), PersonVO::class.java)
    }

    fun create(person: PersonVO): PersonVO {
        logger.info("Creating person with name: ${person.firstName}")
        val entity: Person = DozerMapper.parseObject(person, Person::class.java)
        return DozerMapper.parseObject(repository.save(entity), PersonVO::class.java)
    }

    fun update(person: PersonVO): PersonVO {
        logger.info("Updating person with id: ${person.id}")
        val entity = findOne(person.id).apply {
            firstName = person.firstName
            lastName = person.lastName
            address = person.address
            gender = person.gender
        }
        return DozerMapper.parseObject(repository.save(entity), PersonVO::class.java)
    }

    fun delete(id: Long) {
        logger.info("Deleting person with id: $id")
        val entity = findOne(id)
        repository.delete(entity)
    }

    private fun findOne(id: Long): Person {
        return repository.findById(id)
            .orElseThrow { ResourceNotFoundException("No records found!") }
    }

}