package com.myapp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class StudentTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Student getStudentSample1() {
        return new Student().id(1L).name("name1").lastName("lastName1");
    }

    public static Student getStudentSample2() {
        return new Student().id(2L).name("name2").lastName("lastName2");
    }

    public static Student getStudentRandomSampleGenerator() {
        return new Student().id(longCount.incrementAndGet()).name(UUID.randomUUID().toString()).lastName(UUID.randomUUID().toString());
    }
}
