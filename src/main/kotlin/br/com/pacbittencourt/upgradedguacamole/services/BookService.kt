package br.com.pacbittencourt.upgradedguacamole.services

import br.com.pacbittencourt.upgradedguacamole.controller.BookController
import br.com.pacbittencourt.upgradedguacamole.data.vo.v1.BookVO
import br.com.pacbittencourt.upgradedguacamole.mapper.DozerMapper
import br.com.pacbittencourt.upgradedguacamole.mapper.custom.BookMapper
import br.com.pacbittencourt.upgradedguacamole.repository.BookRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo
import org.springframework.stereotype.Service
import java.util.logging.Logger

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
}