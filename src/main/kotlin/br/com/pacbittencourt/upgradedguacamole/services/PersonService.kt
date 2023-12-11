package br.com.pacbittencourt.upgradedguacamole.services

import br.com.pacbittencourt.upgradedguacamole.model.Person
import org.springframework.stereotype.Service
import java.util.concurrent.atomic.AtomicLong
import java.util.logging.Logger

@Service
class PersonService {

    private val counter: AtomicLong = AtomicLong()

    private val logger = Logger.getLogger(PersonService::class.java.name)

    fun findAll(): List<Person> {
        logger.info("Finding all people")

        val persons = mutableListOf<Person>()

        for (i in 0..7) {
            val person = mockPerson(i)
            persons.add(person)
        }

        return persons
    }

    fun findById(id: Long): Person {
        logger.info("Finding one person")

        return Person(
            id = counter.incrementAndGet(),
            firstName = "Pedro",
            lastName = "Bittencourt",
            address = "Juiz de Fora",
            gender = "male"
        )
    }

    fun create(person: Person): Person {
        return person
    }

    fun update(person: Person): Person {
        return person
    }

    fun delete(id: Long) = Unit

    private fun mockPerson(i: Int): Person {
        return Person(
            id = counter.incrementAndGet(),
            firstName = "First Name $i",
            lastName = "Last Name $i",
            address = "address $i",
            gender = "male"
        )
    }
}