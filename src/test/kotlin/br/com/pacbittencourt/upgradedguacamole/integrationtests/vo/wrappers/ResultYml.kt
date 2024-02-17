package br.com.pacbittencourt.upgradedguacamole.integrationtests.vo.wrappers

import br.com.pacbittencourt.upgradedguacamole.integrationtests.vo.PersonVO

data class ResultYml(
    var content: List<PersonVO>? = null,
)
