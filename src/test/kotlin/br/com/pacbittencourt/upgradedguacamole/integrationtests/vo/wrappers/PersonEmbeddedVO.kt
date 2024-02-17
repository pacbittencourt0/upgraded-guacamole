package br.com.pacbittencourt.upgradedguacamole.integrationtests.vo.wrappers

import br.com.pacbittencourt.upgradedguacamole.integrationtests.vo.PersonVO
import com.fasterxml.jackson.annotation.JsonProperty

class PersonEmbeddedVO {

    @JsonProperty(value = "personVOList")
    var persons: List<PersonVO>? = null
}
