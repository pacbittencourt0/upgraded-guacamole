package br.com.pacbittencourt.upgradedguacamole.integrationtests.vo.wrappers

import br.com.pacbittencourt.upgradedguacamole.integrationtests.vo.BookVO

data class ResultBookYml(
    var content: List<BookVO>? = null
)
