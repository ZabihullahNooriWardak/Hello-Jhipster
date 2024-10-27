package com.myapp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class StudentDetailsTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static StudentDetails getStudentDetailsSample1() {
        return new StudentDetails().id(1L).phone("phone1").email("email1").address("address1");
    }

    public static StudentDetails getStudentDetailsSample2() {
        return new StudentDetails().id(2L).phone("phone2").email("email2").address("address2");
    }

    public static StudentDetails getStudentDetailsRandomSampleGenerator() {
        return new StudentDetails()
            .id(longCount.incrementAndGet())
            .phone(UUID.randomUUID().toString())
            .email(UUID.randomUUID().toString())
            .address(UUID.randomUUID().toString());
    }
}
