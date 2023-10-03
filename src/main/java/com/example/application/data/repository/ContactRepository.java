package com.example.application.data.repository;

import com.example.application.data.entity.Contact;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ContactRepository extends JpaRepository<Contact, Long> {
  // Essa query checa se a string dada é confere com o primeiro ou o último
  // nome, ignorando maiúsculas e minúsculas
  @Query("select c from Contact c "
         + "where lower(c.firstName) like lower(concat('%', :searchTerm, '%')) "
         + "or lower(c.lastName) like lower(concat('%', :searchTerm, '%'))")
  List<Contact>
  search(@Param("searchTerm") String searchTerm);
}
