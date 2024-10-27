package com.myapp.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.myapp.domain.MyEntity} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class MyEntityDTO implements Serializable {

    private Long id;

    @NotNull
    private String name;

    private String description;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MyEntityDTO)) {
            return false;
        }

        MyEntityDTO myEntityDTO = (MyEntityDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, myEntityDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MyEntityDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
