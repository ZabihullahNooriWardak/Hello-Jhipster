package com.myapp.domain;

import static com.myapp.domain.SchoolTestSamples.*;
import static com.myapp.domain.StudClassTestSamples.*;
import static com.myapp.domain.StudentDetailsTestSamples.*;
import static com.myapp.domain.StudentTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.myapp.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class StudentTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Student.class);
        Student student1 = getStudentSample1();
        Student student2 = new Student();
        assertThat(student1).isNotEqualTo(student2);

        student2.setId(student1.getId());
        assertThat(student1).isEqualTo(student2);

        student2 = getStudentSample2();
        assertThat(student1).isNotEqualTo(student2);
    }

    @Test
    void studentDetailsTest() {
        Student student = getStudentRandomSampleGenerator();
        StudentDetails studentDetailsBack = getStudentDetailsRandomSampleGenerator();

        student.setStudentDetails(studentDetailsBack);
        assertThat(student.getStudentDetails()).isEqualTo(studentDetailsBack);

        student.studentDetails(null);
        assertThat(student.getStudentDetails()).isNull();
    }

    @Test
    void schoolTest() {
        Student student = getStudentRandomSampleGenerator();
        School schoolBack = getSchoolRandomSampleGenerator();

        student.setSchool(schoolBack);
        assertThat(student.getSchool()).isEqualTo(schoolBack);

        student.school(null);
        assertThat(student.getSchool()).isNull();
    }

    @Test
    void studClassesTest() {
        Student student = getStudentRandomSampleGenerator();
        StudClass studClassBack = getStudClassRandomSampleGenerator();

        student.addStudClasses(studClassBack);
        assertThat(student.getStudClasses()).containsOnly(studClassBack);
        assertThat(studClassBack.getStudents()).containsOnly(student);

        student.removeStudClasses(studClassBack);
        assertThat(student.getStudClasses()).doesNotContain(studClassBack);
        assertThat(studClassBack.getStudents()).doesNotContain(student);

        student.studClasses(new HashSet<>(Set.of(studClassBack)));
        assertThat(student.getStudClasses()).containsOnly(studClassBack);
        assertThat(studClassBack.getStudents()).containsOnly(student);

        student.setStudClasses(new HashSet<>());
        assertThat(student.getStudClasses()).doesNotContain(studClassBack);
        assertThat(studClassBack.getStudents()).doesNotContain(student);
    }
}
