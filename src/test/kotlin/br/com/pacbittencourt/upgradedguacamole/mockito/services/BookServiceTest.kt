package br.com.pacbittencourt.upgradedguacamole.mockito.services

import br.com.pacbittencourt.upgradedguacamole.mapper.mocks.MockBook
import br.com.pacbittencourt.upgradedguacamole.repository.BookRepository
import br.com.pacbittencourt.upgradedguacamole.services.BookService
import java.util.Optional
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
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
internal class BookServiceTest {

    private lateinit var inputObject: MockBook

    @InjectMocks
    private lateinit var service: BookService

    @Mock
    private lateinit var repository: BookRepository

    @BeforeEach
    fun setUp() {
        inputObject = MockBook()
        MockitoAnnotations.openMocks(this)
    }

    /*@Test
    fun findAll() {
        val list = inputObject.mockEntityList()
        `when`(repository.findAll()).thenReturn(list)

        val books = service.findAll()

        assertNotNull(books)
        assertEquals(11, books.size)

        val bookOne = books[1]
        assertNotNull(bookOne)
        assertNotNull(bookOne.key)
        assertNotNull(bookOne.links)
        assertTrue(bookOne.links.toString().contains("/api/book/v1/1"))
        assertEquals("Author 1", bookOne.author)
        assertEquals("Title 1", bookOne.title)
        assertEquals(1.0, bookOne.price)

        val bookFive = books[5]
        assertNotNull(bookFive)
        assertNotNull(bookFive.key)
        assertNotNull(bookFive.links)
        assertTrue(bookFive.links.toString().contains("/api/book/v1/5"))
        assertEquals("Author 5", bookFive.author)
        assertEquals("Title 5", bookFive.title)
        assertEquals(5.0, bookFive.price)

    }*/

    @Test
    fun findById() {
        val book = inputObject.mockEntity(1)
        `when`(repository.findById(1L)).thenReturn(Optional.of(book))

        val result = service.findById(1L)

        assertNotNull(result)
        assertNotNull(result.key)
        assertNotNull(result.links)
        assertTrue(result.links.toString().contains("/api/book/v1/1"))
        assertEquals("Author 1", result.author)
        assertEquals("Title 1", result.title)
        assertEquals(1.0, result.price)
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
        assertTrue(result.links.toString().contains("/api/book/v1/1"))
        assertEquals("Author 1", result.author)
        assertEquals("Title 1", result.title)
        assertEquals(1.0, result.price)
    }
//
//    @Test
//    fun createWithNullPerson() {
//        val exception: Exception = assertThrows(RequireObjectIsNullException::class.java) {
//            service.create(null)
//        }
//
//        val expectedMessage = "It is not allowed to persist a null object"
//        val actualMessage = exception.message
//
//        assertEquals(actualMessage, expectedMessage)
//    }
//
//    @Test
//    fun update() {
//        val entity = inputObject.mockEntity(1)
//        val persisted = entity.copy()
//
//        persisted.id = 1
//
//        val vo = inputObject.mockVO(1)
//        `when`(repository.findById(1)).thenReturn(Optional.of(entity))
//        `when`(repository.save(entity)).thenReturn(persisted)
//
//        val result = service.update(vo)
//
//        assertNotNull(result)
//        assertNotNull(result.key)
//        assertNotNull(result.links)
//        assertTrue(result.links.toString().contains("/api/v1/person/1"))
//        assertEquals("Address Test1", result.address)
//        assertEquals("First Name Test1", result.firstName)
//        assertEquals("Last Name Test1", result.lastName)
//        assertEquals("Female", result.gender)
//    }
//
//    @Test
//    fun updateWithNullPerson() {
//        val exception: Exception = assertThrows(RequireObjectIsNullException::class.java) {
//            service.update(null)
//        }
//
//        val expectedMessage = "It is not allowed to persist a null object"
//        val actualMessage = exception.message
//
//        assertEquals(actualMessage, expectedMessage)
//    }
//
//    @Test
//    fun delete() {
//        val entity = inputObject.mockEntity(1)
//        `when`(repository.findById(1)).thenReturn(Optional.of(entity))
//        service.delete(1L)
//    }

}