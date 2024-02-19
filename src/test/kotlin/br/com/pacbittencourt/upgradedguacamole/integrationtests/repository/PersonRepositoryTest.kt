package br.com.pacbittencourt.upgradedguacamole.integrationtests.repository

import br.com.pacbittencourt.upgradedguacamole.integrationtests.testcontainers.AbstractIntegrationTest
import br.com.pacbittencourt.upgradedguacamole.model.Person
import br.com.pacbittencourt.upgradedguacamole.repository.PersonRepository
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.MethodOrderer
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestMethodOrder
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.test.context.junit.jupiter.SpringExtension

@ExtendWith(SpringExtension::class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class PersonRepositoryTest : AbstractIntegrationTest() {

    @Autowired
    private lateinit var repository: PersonRepository

    private lateinit var person: Person

    @BeforeAll
    fun setup() {
        person = Person()
    }

    @Test
    @Order(1)
    fun testFindByName() {
        val pageable: Pageable = PageRequest.of(0, 12, Sort.by(Sort.Direction.ASC, "firstName"))
        person = repository.findPersonByName("ped", pageable).content[0]

        assert(person.firstName == "Pedro")
        assert(person.lastName == "Bittencourt")
        assert(person.address == "Juiz de Fora, MG")
        assert(person.gender == "male")
        assert(person.enabled)

    }

    @Test
    @Order(2)
    fun testDisablePerson() {
        repository.disablePerson(person.id)
        val result = repository.findById(person.id).get()
        person = result

        assert(person.firstName == "Pedro")
        assert(person.lastName == "Bittencourt")
        assert(person.address == "Juiz de Fora, MG")
        assert(person.gender == "male")
        assert(!person.enabled)

    }
}