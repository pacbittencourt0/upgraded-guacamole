package br.com.pacbittencourt.upgradedguacamole.data.vo.v1

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonPropertyOrder
import com.github.dozermapper.core.Mapping
import org.springframework.hateoas.RepresentationModel
import java.util.Date

@JsonPropertyOrder("id", "title", "author", "launchDate", "price")
data class BookVO(
    @Mapping(value = "id")
    @field:JsonProperty("id")
    val key: Long = 0,
    var title: String = "",
    var author: String = "",
    var launchDate: Date? = null,
    var price: Double = 0.0,
) : RepresentationModel<BookVO>()