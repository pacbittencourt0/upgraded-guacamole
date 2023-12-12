package br.com.pacbittencourt.upgradedguacamole.repository

import br.com.pacbittencourt.upgradedguacamole.model.Person
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface PersonRepository : JpaRepository<Person, Long?>