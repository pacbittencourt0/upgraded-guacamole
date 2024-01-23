package br.com.pacbittencourt.upgradedguacamole.repository

import br.com.pacbittencourt.upgradedguacamole.model.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface UserRepository : JpaRepository<User?, Long?> {

    @Query("SELECT u FROM User WHERE u.userName =:userName")
    fun findByUsername(@Param("userName") userName: String?): User?
}