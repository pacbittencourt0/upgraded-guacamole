package br.com.pacbittencourt.upgradedguacamole.controller

import br.com.pacbittencourt.upgradedguacamole.data.vo.v1.AccountCredentialsVO
import br.com.pacbittencourt.upgradedguacamole.data.vo.v1.TokenVO
import br.com.pacbittencourt.upgradedguacamole.services.AuthService
import br.com.pacbittencourt.upgradedguacamole.util.AppMediaType
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Tag(name = "Auth")
@RestController
@RequestMapping("/auth")
class AuthController {

    @Autowired
    lateinit var authService: AuthService

    @Operation(
        summary = "Authenticates an user and return a token",
        description = "Authentication an user with username and password",
        tags = ["Auth"],
        responses = [
            ApiResponse(
                description = "Success",
                responseCode = "200",
                content = [Content(schema = Schema(implementation = TokenVO::class))]
            ),
            ApiResponse(
                description = "Bad request",
                responseCode = "400",
                content = [Content(schema = Schema(implementation = Unit::class))]
            ),
            ApiResponse(
                description = "Server Error",
                responseCode = "500",
                content = [Content(schema = Schema(implementation = Unit::class))]
            ),
        ]
    )
    @PostMapping(
        value = ["/signin"],
        consumes = [AppMediaType.APPLICATION_JSON, AppMediaType.APPLICATION_XML, AppMediaType.APPLICATION_YAML],
        produces = [AppMediaType.APPLICATION_JSON, AppMediaType.APPLICATION_XML, AppMediaType.APPLICATION_YAML],
    )
    fun signIn(@RequestBody data: AccountCredentialsVO?): ResponseEntity<*> {
        return if (data!!.username.isNullOrBlank() || data.password.isNullOrBlank()) {
            ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid client request")
        } else {
            authService.signin(data)
        }
    }

    @Operation(
        summary = "Refresh the user's token",
        description = "Refresh the token expired for the user without needing pass the credentials again",
        tags = ["Auth"],
        responses = [
            ApiResponse(
                description = "Success",
                responseCode = "200",
                content = [Content(schema = Schema(implementation = TokenVO::class))]
            ),
            ApiResponse(
                description = "Bad request",
                responseCode = "400",
                content = [Content(schema = Schema(implementation = Unit::class))]
            ),
            ApiResponse(
                description = "Server Error",
                responseCode = "500",
                content = [Content(schema = Schema(implementation = Unit::class))]
            ),
        ]
    )
    @PutMapping(
        value = ["/refresh/{username}"],
        produces = [AppMediaType.APPLICATION_JSON, AppMediaType.APPLICATION_XML, AppMediaType.APPLICATION_YAML],
    )
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