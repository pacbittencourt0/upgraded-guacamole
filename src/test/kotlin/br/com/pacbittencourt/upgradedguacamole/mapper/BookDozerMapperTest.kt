package br.com.pacbittencourt.upgradedguacamole.mapper

import br.com.pacbittencourt.upgradedguacamole.data.vo.v1.BookVO
import br.com.pacbittencourt.upgradedguacamole.mapper.mocks.MockBook
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class BookDozerMapperTest {

    private var inputObject: MockBook? = null

    @BeforeEach
    fun setUp() {
        inputObject = MockBook()
    }

    @Test
    fun parseEntityToVOTest() {
        val output: BookVO = DozerMapper.parseObject(inputObject!!.mockEntity(1), BookVO::class.java)
        Assertions.assertEquals(1, output.key)
        Assertions.assertEquals("Author 1", output.author)
        Assertions.assertEquals("Title 1", output.title)
        Assertions.assertEquals(1.0, output.price)
    }

}