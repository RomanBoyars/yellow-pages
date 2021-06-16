package org.yellow.pages.persistance.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

public class PhoneNumberDTO {
    private final long id;
    private final String number;

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public PhoneNumberDTO(
            @JsonProperty("id") long id,
            @JsonProperty("number") String number) {
        this.id = id;
        this.number = number;
    }

    public long getId() {
        return id;
    }

    public String getNumber() {
        return number;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PhoneNumberDTO that = (PhoneNumberDTO) o;
        return id == that.id &&
                Objects.equals(number, that.number);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, number);
    }

    @Override
    public String toString() {
        return "PhoneNumberDTO{" +
                "id=" + id +
                ", number='" + number + '\'' +
                '}';
    }
}
