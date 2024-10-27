package com.myapp.domain;

import static com.myapp.domain.SchoolTestSamples.*;
import static com.myapp.domain.StudentTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.myapp.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class SchoolTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(School.class);
        School school1 = getSchoolSample1();
        School school2 = new School();
        assertThat(school1).isNotEqualTo(school2);

        school2.setId(school1.getId());
        assertThat(school1).isEqualTo(school2);

        school2 = getSchoolSample2();
        assertThat(school1).isNotEqualTo(school2);
    }

    @Test
    void studentsTest() {
        School school = getSchoolRandomSampleGenerator();
        Student studentBack = getStudentRandomSampleGenerator();

        school.addStudents(studentBack);
        assertThat(school.getStudents()).containsOnly(studentBack);
        assertThat(studentBack.getSchool()).isEqualTo(school);

        school.removeStudents(studentBack);
        assertThat(school.getStudents()).doesNotContain(studentBack);
        assertThat(studentBack.getSchool()).isNull();

        school.students(new HashSet<>(Set.of(studentBack)));
        assertThat(school.getStudents()).containsOnly(studentBack);
        assertThat(studentBack.getSchool()).isEqualTo(school);

        school.setStudents(new HashSet<>());
        assertThat(school.getStudents()).doesNotContain(studentBack);
        assertThat(studentBack.getSchool()).isNull();
    }
}
