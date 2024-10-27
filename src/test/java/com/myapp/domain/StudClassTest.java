package com.myapp.domain;

import static com.myapp.domain.StudClassTestSamples.*;
import static com.myapp.domain.StudentTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.myapp.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class StudClassTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(StudClass.class);
        StudClass studClass1 = getStudClassSample1();
        StudClass studClass2 = new StudClass();
        assertThat(studClass1).isNotEqualTo(studClass2);

        studClass2.setId(studClass1.getId());
        assertThat(studClass1).isEqualTo(studClass2);

        studClass2 = getStudClassSample2();
        assertThat(studClass1).isNotEqualTo(studClass2);
    }

    @Test
    void studentsTest() {
        StudClass studClass = getStudClassRandomSampleGenerator();
        Student studentBack = getStudentRandomSampleGenerator();

        studClass.addStudents(studentBack);
        assertThat(studClass.getStudents()).containsOnly(studentBack);

        studClass.removeStudents(studentBack);
        assertThat(studClass.getStudents()).doesNotContain(studentBack);

        studClass.students(new HashSet<>(Set.of(studentBack)));
        assertThat(studClass.getStudents()).containsOnly(studentBack);

        studClass.setStudents(new HashSet<>());
        assertThat(studClass.getStudents()).doesNotContain(studentBack);
    }
}
