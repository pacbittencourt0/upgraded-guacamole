package br.com.pacbittencourt.upgradedguacamole.services

import br.com.pacbittencourt.upgradedguacamole.controller.BookController
import br.com.pacbittencourt.upgradedguacamole.data.vo.v1.BookVO
import br.com.pacbittencourt.upgradedguacamole.exceptions.RequireObjectIsNullException
import br.com.pacbittencourt.upgradedguacamole.exceptions.ResourceNotFoundException
import br.com.pacbittencourt.upgradedguacamole.mapper.DozerMapper
import br.com.pacbittencourt.upgradedguacamole.mapper.custom.BookMapper
import br.com.pacbittencourt.upgradedguacamole.model.Book
import br.com.pacbittencourt.upgradedguacamole.repository.BookRepository
import java.util.logging.Logger
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo
import org.springframework.stereotype.Service

@Service
class BookService {

    @Autowired
    private lateinit var repository: BookRepository

    @Autowired
    private lateinit var mapper: BookMapper

    private val logger = Logger.getLogger(BookService::class.java.name)

    fun findAll(): List<BookVO> {
        logger.info("Finding all books")
        val books = repository.findAll()
        val vos = DozerMapper.parseObjectList(books, BookVO::class.java)
        vos.forEach { book ->
            val withSelfRef = linkTo(BookController::class.java).slash(book.key).withSelfRel()
            book.add(withSelfRef)
        }
        return vos
    }

    fun findById(id: Long): BookVO {
        logger.info("Finding one person with ID $id")
        val bookVo = DozerMapper.parseObject(findOne(id), BookVO::class.java)

        val withSelfRel = linkTo(BookController::class.java).slash(bookVo.key).withSelfRel()
        bookVo.add(withSelfRel)

        return bookVo
    }

    private fun findOne(id: Long): Book {
        return repository.findById(id)
            .orElseThrow { ResourceNotFoundException("No records found!") }
    }

    fun create(book: BookVO?): BookVO {
        if (book == null) throw RequireObjectIsNullException()
        logger.info("Creating book with title: ${book.title}")
        val entity: Book = DozerMapper.parseObject(book, Book::class.java)
        val bookVO = DozerMapper.parseObject(repository.save(entity), BookVO::class.java)
        val withSelfRel = linkTo(BookController::class.java).slash(bookVO.key).withSelfRel()
        bookVO.add(withSelfRel)

        return bookVO
    }

    fun update(book: BookVO?): BookVO {
        if (book == null) throw RequireObjectIsNullException()
        logger.info("Updating person with id: ${book.key}")
        val entity = findOne(book.key).apply {
            author = book.author
            title = book.title
            launchDate = book.launchDate
            price = book.price
        }
        val bookVO = DozerMapper.parseObject(repository.save(entity), BookVO::class.java)
        val withSelfRel = linkTo(BookController::class.java).slash(bookVO.key).withSelfRel()
        bookVO.add(withSelfRel)
        return bookVO
    }

    fun delete(id: Long) {
        logger.info("Deleting book with id: $id")
        val entity = findOne(id)
        repository.delete(entity)
    }
}