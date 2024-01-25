package br.com.pacbittencourt.upgradedguacamole

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.security.crypto.password.DelegatingPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder

@SpringBootApplication
class StartUp

fun main(args: Array<String>) {
	runApplication<StartUp>(*args)

//	val encoders: MutableMap<String, PasswordEncoder> = hashMapOf()
//	val pbkdf2PasswordEncoder = Pbkdf2PasswordEncoder("", 8, 185000, Pbkdf2PasswordEncoder.SecretKeyFactoryAlgorithm.PBKDF2WithHmacSHA256)
//	encoders["pbkdf2"] = pbkdf2PasswordEncoder
//	val passwordEncoder = DelegatingPasswordEncoder("pbkdf2", encoders)
//	passwordEncoder.setDefaultPasswordEncoderForMatches(pbkdf2PasswordEncoder)
//
//	val result = passwordEncoder.encode("pedro123")
//	println("My hash $result")
}
