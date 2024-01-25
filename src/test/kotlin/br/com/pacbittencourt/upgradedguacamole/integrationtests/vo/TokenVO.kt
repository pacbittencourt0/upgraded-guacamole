package br.com.pacbittencourt.upgradedguacamole.integrationtests.vo

import java.util.*

data class TokenVO(
    val userName: String? = null,
    val authenticated: Boolean? = null,
    val created: Date? = null,
    val expiration: Date? = null,
    val accessToken: String? = null,
    val refreshToken: String? = null,
)
