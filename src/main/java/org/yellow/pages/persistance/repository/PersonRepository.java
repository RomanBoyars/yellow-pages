package org.yellow.pages.persistance.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.yellow.pages.persistance.entity.Person;

import java.util.UUID;

public interface PersonRepository extends JpaRepository<Person, UUID> {

}
