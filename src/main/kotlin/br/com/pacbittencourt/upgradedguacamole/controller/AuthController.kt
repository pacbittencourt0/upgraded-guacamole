package br.com.pacbittencourt.upgradedguacamole.controller

import br.com.pacbittencourt.upgradedguacamole.data.vo.v1.AccountCredentialsVO
import br.com.pacbittencourt.upgradedguacamole.services.AuthService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@Tag(name = "Authentication Endpoint")
@RestController
@RequestMapping("/auth")
class AuthController {

    @Autowired
    lateinit var authService: AuthService

    @Operation(summary = "Authenticates an user and return a token")
    @PostMapping(value = ["/signin"])
    fun signIn(@RequestBody data: AccountCredentialsVO?): ResponseEntity<*> {
        return if (data!!.username.isNullOrBlank() || data.password.isNullOrBlank()) {
            ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid client request")
        } else {
            authService.signin(data)
        }
    }

    @Operation(summary = "Refresh token for an user and return a token")
    @PutMapping(value = ["/refresh/{username}"])
    fun refreshToken(
        @PathVariable("username") username: String?,
        @RequestHeader("Authorization") refreshToken: String?
    ): ResponseEntity<*> {
        return if (username.isNullOrBlank() || refreshToken.isNullOrBlank()) {
            ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid client request")
        } else {
            authService.refreshToken(username, refreshToken)
        }
    }
}