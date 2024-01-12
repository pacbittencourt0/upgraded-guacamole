package br.com.pacbittencourt.upgradedguacamole.mapper.custom

import br.com.pacbittencourt.upgradedguacamole.data.vo.v1.BookVO
import br.com.pacbittencourt.upgradedguacamole.model.Book
import org.springframework.stereotype.Service
import java.util.Date

@Service
class BookMapper {

    fun mapEntityToVO(book: Book) = BookVO(
        key = book.id,
        title = book.title,
        author = book.author,
        launchDate = Date(),
        price = book.price
    )

    fun mapVOToEntity(book: BookVO) = Book(
        id = book.key,
        title = book.title,
        author = book.author,
        launchDate = book.launchDate,
        price = book.price
    )
}