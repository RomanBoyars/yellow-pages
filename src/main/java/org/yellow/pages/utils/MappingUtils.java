package org.yellow.pages.utils;

import org.springframework.stereotype.Service;
import org.yellow.pages.persistance.dto.PersonDTO;
import org.yellow.pages.persistance.dto.PhoneNumberDTO;
import org.yellow.pages.persistance.entity.Person;
import org.yellow.pages.persistance.entity.PhoneNumber;

import java.util.stream.Collectors;

@Service
public class MappingUtils {

    public PersonDTO mapToPersonDTO(Person person) {
        return new PersonDTO(person.getId().toString(),
                person.getFirstName(),
                person.getMiddleName(),
                person.getLastName(),
                person.getAddress(),
                person.getPhones()
                        .stream()
                        .map(phoneNumber -> new PhoneNumberDTO(phoneNumber.getId(), phoneNumber.getNumber()))
                        .collect(Collectors.toList()));
    }

    public Person mapToPerson(PersonDTO personDTO) {
        Person person = new Person();
        person.setFirstName(personDTO.getFirstName());
        person.setMiddleName(personDTO.getMiddleName());
        person.setLastName(personDTO.getLastName());
        person.setAddress(personDTO.getAddress());
        person.setPhones(personDTO.getPhones().stream().map(
                p -> mapToPhoneNumber(p, person)
        ).collect(Collectors.toList()));
        return person;
    }

    public PhoneNumber mapToPhoneNumber(PhoneNumberDTO phoneNumberDTO, Person person) {
        return new PhoneNumber(phoneNumberDTO.getNumber(), person);
    }
}
