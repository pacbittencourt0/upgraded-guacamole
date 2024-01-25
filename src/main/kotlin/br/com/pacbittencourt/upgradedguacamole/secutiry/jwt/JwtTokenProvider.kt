package br.com.pacbittencourt.upgradedguacamole.secutiry.jwt

import br.com.pacbittencourt.upgradedguacamole.data.vo.v1.TokenVO
import br.com.pacbittencourt.upgradedguacamole.exceptions.InvalidJwtAuthenticationException
import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.interfaces.DecodedJWT
import jakarta.annotation.PostConstruct
import jakarta.servlet.http.HttpServletRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service
import org.springframework.web.servlet.support.ServletUriComponentsBuilder
import java.util.*

@Service
class JwtTokenProvider {

    @Value("\${security.jwt.token.secret-key:secret}")
    private var secretKey = "secret"

    @Value("\${security.jwt.token.expire-length:3600000}")
    private var validInMilliseconds: Long = 3_600_000

    @Autowired
    private lateinit var userDetailsService: UserDetailsService

    private lateinit var algorithm: Algorithm

    @PostConstruct
    protected fun init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.toByteArray())
        algorithm = Algorithm.HMAC256(secretKey.toByteArray())
    }

    fun createAccessToken(userName: String, roles: List<String?>): TokenVO {
        val now = Date()
        val validity = Date(now.time + validInMilliseconds)
        val accessToken = getAccessToken(userName, roles, now, validity)
        val refreshToken = getRefreshToken(userName, roles, now)
        return TokenVO(
            userName = userName,
            authenticated = true,
            accessToken = accessToken,
            refreshToken = refreshToken,
            created = now,
            expiration = validity

        )
    }

    fun refreshToken(refreshToken: String): TokenVO {
        var token = ""
        if (refreshToken.contains("Bearer ")) token = refreshToken.substring("Bearer ".length)
        val verifier = JWT.require(algorithm).build()
        val decodedJWT = verifier.verify(token)
        val username = decodedJWT.subject
        val roles = decodedJWT.getClaim("roles").asList(String::class.java)
        return createAccessToken(username, roles)
    }

    private fun getAccessToken(userName: String, roles: List<String?>, now: Date, validity: Date): String {
        val issueURL = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString()
        return JWT.create()
            .withClaim("roles", roles)
            .withIssuedAt(now)
            .withExpiresAt(validity)
            .withSubject(userName)
            .withIssuer(issueURL)
            .sign(algorithm)
            .trim()
    }

    private fun getRefreshToken(userName: String, roles: List<String?>, now: Date): String {
        val validityRefreshToken = Date(now.time + (validInMilliseconds * 3))
        return JWT.create()
            .withClaim("roles", roles)
            .withExpiresAt(validityRefreshToken)
            .withSubject(userName)
            .sign(algorithm)
            .trim()
    }

    fun getAuthentication(token: String): Authentication {
        val decodedJWT: DecodedJWT = decodedJWT(token)
        val userDetails: UserDetails = userDetailsService.loadUserByUsername(decodedJWT.subject)
        return UsernamePasswordAuthenticationToken(userDetails, "", userDetails.authorities)
    }

    private fun decodedJWT(token: String): DecodedJWT {
        val algorithm = Algorithm.HMAC256(secretKey.toByteArray())
        val verifier: JWTVerifier = JWT.require(algorithm).build()
        return verifier.verify(token)
    }

    fun resolveToken(req: HttpServletRequest): String? {
        val bearerToken = req.getHeader("Authorization")
        return if (!bearerToken.isNullOrBlank() && bearerToken.startsWith("Bearer ")) {
            bearerToken.substring("Bearer ".length)
        } else null
    }

    fun validateToken(token: String): Boolean {
        val decodedJWT = decodedJWT(token)
        return try {
            !decodedJWT.expiresAt.before(Date())
        } catch (e: Exception) {
            throw InvalidJwtAuthenticationException("Expired or invalid token")
        }
    }
}