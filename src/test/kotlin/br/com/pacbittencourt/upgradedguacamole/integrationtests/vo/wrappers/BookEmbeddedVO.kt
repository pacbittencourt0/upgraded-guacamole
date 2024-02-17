package br.com.pacbittencourt.upgradedguacamole.integrationtests.vo.wrappers

import br.com.pacbittencourt.upgradedguacamole.integrationtests.vo.BookVO
import com.fasterxml.jackson.annotation.JsonProperty

class BookEmbeddedVO {
    @JsonProperty(value = "bookVOList")
    var books: List<BookVO>? = null
}