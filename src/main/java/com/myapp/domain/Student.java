package com.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Student.
 */
@Entity
@Table(name = "student")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Student implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "last_name")
    private String lastName;

    @JsonIgnoreProperties(value = { "student" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(unique = true)
    private StudentDetails studentDetails;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "students" }, allowSetters = true)
    private School school;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "students")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "students" }, allowSetters = true)
    private Set<StudClass> studClasses = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Student id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Student name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return this.lastName;
    }

    public Student lastName(String lastName) {
        this.setLastName(lastName);
        return this;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public StudentDetails getStudentDetails() {
        return this.studentDetails;
    }

    public void setStudentDetails(StudentDetails studentDetails) {
        this.studentDetails = studentDetails;
    }

    public Student studentDetails(StudentDetails studentDetails) {
        this.setStudentDetails(studentDetails);
        return this;
    }

    public School getSchool() {
        return this.school;
    }

    public void setSchool(School school) {
        this.school = school;
    }

    public Student school(School school) {
        this.setSchool(school);
        return this;
    }

    public Set<StudClass> getStudClasses() {
        return this.studClasses;
    }

    public void setStudClasses(Set<StudClass> studClasses) {
        if (this.studClasses != null) {
            this.studClasses.forEach(i -> i.removeStudents(this));
        }
        if (studClasses != null) {
            studClasses.forEach(i -> i.addStudents(this));
        }
        this.studClasses = studClasses;
    }

    public Student studClasses(Set<StudClass> studClasses) {
        this.setStudClasses(studClasses);
        return this;
    }

    public Student addStudClasses(StudClass studClass) {
        this.studClasses.add(studClass);
        studClass.getStudents().add(this);
        return this;
    }

    public Student removeStudClasses(StudClass studClass) {
        this.studClasses.remove(studClass);
        studClass.getStudents().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Student)) {
            return false;
        }
        return getId() != null && getId().equals(((Student) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Student{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", lastName='" + getLastName() + "'" +
            "}";
    }
}
