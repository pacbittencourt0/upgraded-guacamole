package br.com.pacbittencourt.upgradedguacamole.mapper.custom

import br.com.pacbittencourt.upgradedguacamole.data.vo.v2.PersonVO
import br.com.pacbittencourt.upgradedguacamole.model.Person
import org.springframework.stereotype.Service
import java.util.Date

@Service
class PersonMapper {

    fun mapEntityToVO(person: Person): PersonVO {
        val vo = PersonVO().apply {
            id = person.id
            firstName = person.firstName
            lastName = person.lastName
            address = person.address
            birthDay = Date()
            gender = person.gender
        }
        return vo
    }

    fun mapVOToEntity(person: PersonVO): Person {
        val entity = Person().apply {
            id = person.id
            firstName = person.firstName
            lastName = person.lastName
            address = person.address
            // birthDay = person.birthDay
            gender = person.gender
        }
        return entity
    }
}