package org.yellow.pages.service;

import org.yellow.pages.persistance.dto.PersonDTO;

import java.util.List;

public interface PersonService {

    PersonDTO createPerson(PersonDTO newPersonDTO);

    PersonDTO updatePerson(PersonDTO person);

    void deletePerson(String personId);

    List<PersonDTO> getAllPersons();

}
