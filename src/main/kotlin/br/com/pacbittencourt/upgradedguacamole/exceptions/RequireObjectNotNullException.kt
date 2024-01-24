package br.com.pacbittencourt.upgradedguacamole.exceptions

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.BAD_REQUEST)
class RequireObjectIsNullException : RuntimeException {
    constructor() : super("It is not allowed to persist a null object")
    constructor(exception: String?) : super(exception)
}