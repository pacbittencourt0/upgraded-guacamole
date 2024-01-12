package br.com.pacbittencourt.upgradedguacamole.mapper.mocks

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
}