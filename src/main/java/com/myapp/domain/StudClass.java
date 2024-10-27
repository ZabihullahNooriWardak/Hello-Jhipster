package com.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A StudClass.
 */
@Entity
@Table(name = "stud_class")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class StudClass implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "class_name")
    private String className;

    @Column(name = "subject")
    private String subject;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "rel_stud_class__students",
        joinColumns = @JoinColumn(name = "stud_class_id"),
        inverseJoinColumns = @JoinColumn(name = "students_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "studentDetails", "school", "studClasses" }, allowSetters = true)
    private Set<Student> students = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public StudClass id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getClassName() {
        return this.className;
    }

    public StudClass className(String className) {
        this.setClassName(className);
        return this;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getSubject() {
        return this.subject;
    }

    public StudClass subject(String subject) {
        this.setSubject(subject);
        return this;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public Set<Student> getStudents() {
        return this.students;
    }

    public void setStudents(Set<Student> students) {
        this.students = students;
    }

    public StudClass students(Set<Student> students) {
        this.setStudents(students);
        return this;
    }

    public StudClass addStudents(Student student) {
        this.students.add(student);
        return this;
    }

    public StudClass removeStudents(Student student) {
        this.students.remove(student);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof StudClass)) {
            return false;
        }
        return getId() != null && getId().equals(((StudClass) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "StudClass{" +
            "id=" + getId() +
            ", className='" + getClassName() + "'" +
            ", subject='" + getSubject() + "'" +
            "}";
    }
}
