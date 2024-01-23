package br.com.pacbittencourt.upgradedguacamole.data.vo.v1

import java.util.Date

data class TokenVO(
    val username: String? = null,
    val authenticated: Boolean? = null,
    val created: Date? = null,
    val expiration: Date? = null,
    val accessToken: String? = null,
    val refreshToke: String? = null,
)
