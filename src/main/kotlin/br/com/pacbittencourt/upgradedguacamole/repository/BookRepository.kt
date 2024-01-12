package br.com.pacbittencourt.upgradedguacamole.repository

import br.com.pacbittencourt.upgradedguacamole.model.Book
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface BookRepository : JpaRepository<Book, Long?>