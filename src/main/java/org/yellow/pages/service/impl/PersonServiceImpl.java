package org.yellow.pages.service.impl;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yellow.pages.exception.PersonNotFoundException;
import org.yellow.pages.persistance.dto.PersonDTO;
import org.yellow.pages.persistance.entity.Person;
import org.yellow.pages.persistance.entity.PhoneNumber;
import org.yellow.pages.persistance.repository.PersonRepository;
import org.yellow.pages.service.PersonService;
import org.yellow.pages.utils.MappingUtils;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class PersonServiceImpl implements PersonService {

    @NonNull
    private final PersonRepository personRepository;

    @NonNull
    private final MappingUtils mappingUtils;

    public PersonServiceImpl(@NonNull PersonRepository personRepository, @NonNull MappingUtils mappingUtils) {
        this.personRepository = personRepository;
        this.mappingUtils = mappingUtils;
    }

    @Override
    public PersonDTO createPerson(PersonDTO newPersonDTO) {
        return mappingUtils.mapToPersonDTO(personRepository.save(mappingUtils.mapToPerson(newPersonDTO)));
    }

    @Override
    @Transactional
    public PersonDTO updatePerson(PersonDTO person) {
        Person updated =
                personRepository.findById(UUID.fromString(person.getId()))
                        .orElseThrow(() -> new PersonNotFoundException(String.format("Person with id %s not found!",
                                person.getId())));
        updated.setFirstName(person.getFirstName());
        updated.setMiddleName(person.getMiddleName());
        updated.setLastName(person.getLastName());
        updated.setAddress(person.getAddress());

        List<PhoneNumber> phoneNumbers = updated.getPhones();
        person.getPhones()
                .stream()
                .filter(phoneNumberDTO -> phoneNumberDTO.getId() == 0)
                .map(phoneNumberDTO -> mappingUtils.mapToPhoneNumber(phoneNumberDTO, updated))
                .forEach(updated::addPhone);

        List<PhoneNumber> numbersToRemove = phoneNumbers.stream()
                .filter(phoneNumber -> person.getPhones()
                        .stream()
                        .noneMatch(phoneNumberDTO -> phoneNumber.getId() == phoneNumberDTO.getId()))
                .collect(Collectors.toList());
        numbersToRemove.forEach(updated::removePhone);
        updated.setPhones(phoneNumbers);

        personRepository.save(updated);
        return mappingUtils.mapToPersonDTO(updated);
    }

    @Override
    public void deletePerson(String personId) {
        try {
            personRepository.deleteById(UUID.fromString(personId));
        } catch (EntityNotFoundException e) {
            throw new PersonNotFoundException(String.format("Person with id %s not found!", personId));
        }
    }

    @Override
    public List<PersonDTO> getAllPersons() {
        return personRepository.findAll().stream().map(mappingUtils::mapToPersonDTO).collect(
                Collectors.toList());
    }
}
