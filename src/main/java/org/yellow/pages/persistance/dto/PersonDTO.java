package org.yellow.pages.persistance.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Objects;

public class PersonDTO {
    private final String id;
    private final String firstName;
    private final String middleName;
    private final String lastName;
    private final String address;
    private final List<PhoneNumberDTO> phones;

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public PersonDTO(
            @JsonProperty("id") String id,
            @JsonProperty("firstName") String firstName,
            @JsonProperty("middleName") String middleName,
            @JsonProperty("lastName") String lastName,
            @JsonProperty("address") String address,
            @JsonProperty("phones") List<PhoneNumberDTO> phones) {
        this.id = id;
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
        this.address = address;
        this.phones = phones;
    }

    public String getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getAddress() {
        return address;
    }

    public List<PhoneNumberDTO> getPhones() {
        return phones;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PersonDTO personDTO = (PersonDTO) o;
        return Objects.equals(id, personDTO.id) &&
                Objects.equals(firstName, personDTO.firstName) &&
                Objects.equals(middleName, personDTO.middleName) &&
                Objects.equals(lastName, personDTO.lastName) &&
                Objects.equals(address, personDTO.address) &&
                Objects.equals(phones, personDTO.phones);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, firstName, middleName, lastName, address, phones);
    }

    @Override
    public String toString() {
        return "PersonDTO{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", middleName='" + middleName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", address='" + address + '\'' +
                ", phones=" + phones +
                '}';
    }
}
