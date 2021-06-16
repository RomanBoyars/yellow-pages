package org.yellow.pages.controller;

import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.yellow.pages.persistance.dto.PersonDTO;
import org.yellow.pages.service.PersonService;

import java.util.List;

@RestController
@RequestMapping("/api/persons")
public class PersonController {

    @NonNull
    private final PersonService personService;

    public PersonController(@NonNull PersonService personService) {
        this.personService = personService;
    }

    @PostMapping
    @ApiOperation(value = "Create a new person", response = PersonDTO.class)
    public ResponseEntity<PersonDTO> createPerson(@RequestBody PersonDTO personDTO) {
        return ResponseEntity.ok(personService.createPerson(personDTO));
    }

    @GetMapping
    @ApiOperation(value = "View a list of persons", response = Iterable.class)
    public ResponseEntity<List<PersonDTO>> getPersons() {
        return ResponseEntity.ok(personService.getAllPersons());
    }

    @DeleteMapping("/{personId}")
    @ApiOperation(value = "Delete person with an UUID")
    public ResponseEntity<?> deletePerson(@PathVariable String personId) {
        personService.deletePerson(personId);
        return ResponseEntity.ok().build();
    }

    @PutMapping
    @ApiOperation(value = "Update person", response = PersonDTO.class)
    public ResponseEntity<PersonDTO> updatePerson(@RequestBody PersonDTO personDTO) {
        return ResponseEntity.ok(personService.updatePerson(personDTO));
    }


}
