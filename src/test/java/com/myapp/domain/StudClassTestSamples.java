package com.myapp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class StudClassTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static StudClass getStudClassSample1() {
        return new StudClass().id(1L).className("className1").subject("subject1");
    }

    public static StudClass getStudClassSample2() {
        return new StudClass().id(2L).className("className2").subject("subject2");
    }

    public static StudClass getStudClassRandomSampleGenerator() {
        return new StudClass()
            .id(longCount.incrementAndGet())
            .className(UUID.randomUUID().toString())
            .subject(UUID.randomUUID().toString());
    }
}
