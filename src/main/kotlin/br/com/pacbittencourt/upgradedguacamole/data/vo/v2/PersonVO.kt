package br.com.pacbittencourt.upgradedguacamole.data.vo.v2

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonPropertyOrder
import java.util.Date

@JsonPropertyOrder("id", "address", "firstName", "lastName", "gender")
data class PersonVO(
    var id: Long = 0,

    @field:JsonProperty("first_name")
    var firstName: String = "",

    @field:JsonProperty("last_name")
    var lastName: String = "",

    var address: String = "",

    @field:JsonIgnore
    var gender: String = "",

    var birthDay: Date? = null,
)