package br.com.pacbittencourt.upgradedguacamole.mockito.services

import br.com.pacbittencourt.upgradedguacamole.exceptions.RequireObjectIsNullException
import br.com.pacbittencourt.upgradedguacamole.mapper.mocks.MockPerson
import br.com.pacbittencourt.upgradedguacamole.repository.PersonRepository
import br.com.pacbittencourt.upgradedguacamole.services.PersonService
import java.util.Optional
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import org.mockito.junit.jupiter.MockitoExtension

@ExtendWith(MockitoExtension::class)
internal class PersonServiceTest {

    private lateinit var inputObject: MockPerson

    @InjectMocks
    private lateinit var service: PersonService

    @Mock
    private lateinit var repository: PersonRepository

    @BeforeEach
    fun setUp() {
        inputObject = MockPerson()
        MockitoAnnotations.openMocks(this)
    }

    @Test
    fun findById() {
        val person = inputObject.mockEntity(1)
        `when`(repository.findById(1L)).thenReturn(Optional.of(person))

        val result = service.findById(1L)

        assertNotNull(result)
        assertNotNull(result.key)
        assertNotNull(result.links)
        assertTrue(result.links.toString().contains("/api/person/v1/1"))
        assertEquals("Address Test1", result.address)
        assertEquals("First Name Test1", result.firstName)
        assertEquals("Last Name Test1", result.lastName)
        assertEquals("Female", result.gender)
    }

    @Test
    fun create() {
        val entity = inputObject.mockEntity(1)
        val persisted = entity.copy()

        persisted.id = 1

        val vo = inputObject.mockVO(1)
        `when`(repository.save(entity)).thenReturn(persisted)

        val result = service.create(vo)

        assertNotNull(result)
        assertNotNull(result.key)
        assertNotNull(result.links)
        assertTrue(result.links.toString().contains("/api/person/v1/1"))
        assertEquals("Address Test1", result.address)
        assertEquals("First Name Test1", result.firstName)
        assertEquals("Last Name Test1", result.lastName)
        assertEquals("Female", result.gender)
    }

    @Test
    fun createWithNullPerson() {
        val exception: Exception = assertThrows(RequireObjectIsNullException::class.java) {
            service.create(null)
        }

        val expectedMessage = "It is not allowed to persist a null object"
        val actualMessage = exception.message

        assertEquals(actualMessage, expectedMessage)
    }

    @Test
    fun update() {
        val entity = inputObject.mockEntity(1)
        val persisted = entity.copy()

        persisted.id = 1

        val vo = inputObject.mockVO(1)
        `when`(repository.findById(1)).thenReturn(Optional.of(entity))
        `when`(repository.save(entity)).thenReturn(persisted)

        val result = service.update(vo)

        assertNotNull(result)
        assertNotNull(result.key)
        assertNotNull(result.links)
        assertTrue(result.links.toString().contains("/api/person/v1/1"))
        assertEquals("Address Test1", result.address)
        assertEquals("First Name Test1", result.firstName)
        assertEquals("Last Name Test1", result.lastName)
        assertEquals("Female", result.gender)
    }

    @Test
    fun updateWithNullPerson() {
        val exception: Exception = assertThrows(RequireObjectIsNullException::class.java) {
            service.update(null)
        }

        val expectedMessage = "It is not allowed to persist a null object"
        val actualMessage = exception.message

        assertEquals(actualMessage, expectedMessage)
    }

    @Test
    fun delete() {
        val entity = inputObject.mockEntity(1)
        `when`(repository.findById(1)).thenReturn(Optional.of(entity))
        service.delete(1L)
    }

    @Test
    fun createV2() {
    }
}