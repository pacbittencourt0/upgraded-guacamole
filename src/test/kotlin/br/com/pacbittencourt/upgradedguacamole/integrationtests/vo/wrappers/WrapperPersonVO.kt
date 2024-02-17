package br.com.pacbittencourt.upgradedguacamole.integrationtests.vo.wrappers

import com.fasterxml.jackson.annotation.JsonProperty

class WrapperPersonVO {

    @JsonProperty(value = "_embedded")
    var embedded: PersonEmbeddedVO? = null
}