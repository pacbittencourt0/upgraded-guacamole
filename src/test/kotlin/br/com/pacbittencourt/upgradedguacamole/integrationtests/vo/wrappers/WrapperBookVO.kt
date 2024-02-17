package br.com.pacbittencourt.upgradedguacamole.integrationtests.vo.wrappers

import com.fasterxml.jackson.annotation.JsonProperty

class WrapperBookVO {

    @JsonProperty(value = "_embedded")
    var embedded: BookEmbeddedVO? = null
}