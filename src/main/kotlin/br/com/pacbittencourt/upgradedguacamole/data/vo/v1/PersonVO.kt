package br.com.pacbittencourt.upgradedguacamole.data.vo.v1

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonPropertyOrder
import com.github.dozermapper.core.Mapping
import org.springframework.hateoas.RepresentationModel

@JsonPropertyOrder("id", "firstName", "lastName", "address", "gender", "enabled")
data class PersonVO(
    @Mapping(value = "id")
    @field:JsonProperty("id")

    var key: Long = 0,
    var firstName: String = "",
    var lastName: String = "",
    var address: String = "",
    var gender: String = "",
    var enabled: Boolean = true,
) : RepresentationModel<PersonVO>()