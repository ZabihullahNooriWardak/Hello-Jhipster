package com.myapp.domain;

import static com.myapp.domain.StudentDetailsTestSamples.*;
import static com.myapp.domain.StudentTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class StudentDetailsTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(StudentDetails.class);
        StudentDetails studentDetails1 = getStudentDetailsSample1();
        StudentDetails studentDetails2 = new StudentDetails();
        assertThat(studentDetails1).isNotEqualTo(studentDetails2);

        studentDetails2.setId(studentDetails1.getId());
        assertThat(studentDetails1).isEqualTo(studentDetails2);

        studentDetails2 = getStudentDetailsSample2();
        assertThat(studentDetails1).isNotEqualTo(studentDetails2);
    }

    @Test
    void studentTest() {
        StudentDetails studentDetails = getStudentDetailsRandomSampleGenerator();
        Student studentBack = getStudentRandomSampleGenerator();

        studentDetails.setStudent(studentBack);
        assertThat(studentDetails.getStudent()).isEqualTo(studentBack);
        assertThat(studentBack.getStudentDetails()).isEqualTo(studentDetails);

        studentDetails.student(null);
        assertThat(studentDetails.getStudent()).isNull();
        assertThat(studentBack.getStudentDetails()).isNull();
    }
}
