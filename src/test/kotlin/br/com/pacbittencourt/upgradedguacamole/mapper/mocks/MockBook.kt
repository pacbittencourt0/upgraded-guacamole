package br.com.pacbittencourt.upgradedguacamole.mapper.mocks

import br.com.pacbittencourt.upgradedguacamole.data.vo.v1.BookVO
import br.com.pacbittencourt.upgradedguacamole.model.Book
import java.util.Date

class MockBook {

    fun mockEntity(number: Int): Book {
        return Book(
            id = number.toLong(),
            author = "Author $number",
            launchDate = Date(),
            price = number.toDouble(),
            title = "Title $number"
        )
    }
    fun mockVO(number: Int): BookVO {
        return BookVO(
            key = number.toLong(),
            author = "Author $number",
            launchDate = Date(),
            price = number.toDouble(),
            title = "Title $number"
        )
    }

    fun mockEntityList(): List<Book> {
        val books = ArrayList<Book>()
        for (i in 0..10) {
            books.add(mockEntity(i))
        }
        return books
    }

    fun mockVOList(): List<BookVO> {
        val books = ArrayList<BookVO>()
        for (i in 0..10) {
            books.add(mockVO(i))
        }
        return books
    }
}