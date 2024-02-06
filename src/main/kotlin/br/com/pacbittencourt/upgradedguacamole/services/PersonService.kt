package br.com.pacbittencourt.upgradedguacamole.services

import br.com.pacbittencourt.upgradedguacamole.controller.PersonController
import br.com.pacbittencourt.upgradedguacamole.data.vo.v1.PersonVO
import br.com.pacbittencourt.upgradedguacamole.exceptions.RequireObjectIsNullException
import br.com.pacbittencourt.upgradedguacamole.exceptions.ResourceNotFoundException
import br.com.pacbittencourt.upgradedguacamole.mapper.DozerMapper
import br.com.pacbittencourt.upgradedguacamole.mapper.custom.PersonMapper
import br.com.pacbittencourt.upgradedguacamole.model.Person
import br.com.pacbittencourt.upgradedguacamole.repository.PersonRepository
import java.util.logging.Logger
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import br.com.pacbittencourt.upgradedguacamole.data.vo.v2.PersonVO as PersonVOV2

@Service
class PersonService {

    @Autowired
    private lateinit var repository: PersonRepository

    @Autowired
    private lateinit var personMapper: PersonMapper

    private val logger = Logger.getLogger(PersonService::class.java.name)

    fun findAll(): List<PersonVO> {
        logger.info("Finding all people")
        val persons = repository.findAll()
        val vos = DozerMapper.parseObjectList(persons, PersonVO::class.java)
        vos.forEach {
            val withSelfRel = linkTo(PersonController::class.java).slash(it.key).withSelfRel()
            it.add(withSelfRel)
        }
        return vos
    }

    fun findById(id: Long): PersonVO {
        logger.info("Finding one person with ID $id")
        val personVO = DozerMapper.parseObject(findOne(id), PersonVO::class.java)

        val withSelfRel = linkTo(PersonController::class.java).slash(personVO.key).withSelfRel()
        personVO.add(withSelfRel)

        return personVO
    }

    fun create(person: PersonVO?): PersonVO {
        if (person == null) throw RequireObjectIsNullException()
        logger.info("Creating person with name: ${person.firstName}")
        val entity: Person = DozerMapper.parseObject(person, Person::class.java)
        val personVO = DozerMapper.parseObject(repository.save(entity), PersonVO::class.java)
        val withSelfRel = linkTo(PersonController::class.java).slash(personVO.key).withSelfRel()
        personVO.add(withSelfRel)

        return personVO
    }

    fun update(person: PersonVO?): PersonVO {
        if (person == null) throw RequireObjectIsNullException()
        logger.info("Updating person with id: ${person.key}")
        val entity = findOne(person.key).apply {
            firstName = person.firstName
            lastName = person.lastName
            address = person.address
            gender = person.gender
        }
        val personVO = DozerMapper.parseObject(repository.save(entity), PersonVO::class.java)
        val withSelfRel = linkTo(PersonController::class.java).slash(personVO.key).withSelfRel()
        personVO.add(withSelfRel)

        return personVO
    }

    @Transactional
    fun disablePerson(id: Long): PersonVO {
        logger.info("Disabling one person with ID $id")
        repository.disablePerson(id)
        val personVO = DozerMapper.parseObject(findOne(id), PersonVO::class.java)

        val withSelfRel = linkTo(PersonController::class.java).slash(personVO.key).withSelfRel()
        personVO.add(withSelfRel)

        return personVO
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

    fun createV2(person: PersonVOV2): PersonVOV2 {
        logger.info("Creating person with name: ${person.firstName}")
        val entity: Person = personMapper.mapVOToEntity(person)
        return personMapper.mapEntityToVO(repository.save(entity))
    }

}