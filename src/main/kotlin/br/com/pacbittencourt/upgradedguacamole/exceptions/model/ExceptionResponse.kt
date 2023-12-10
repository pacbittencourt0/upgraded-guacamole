package br.com.pacbittencourt.upgradedguacamole.exceptions.model

import java.util.Date

class ExceptionResponse (
    val timeStamp: Date,
    val message: String?,
    val details: String
)