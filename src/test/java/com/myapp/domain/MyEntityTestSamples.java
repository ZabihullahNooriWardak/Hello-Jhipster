package com.myapp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class MyEntityTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static MyEntity getMyEntitySample1() {
        return new MyEntity().id(1L).name("name1").description("description1");
    }

    public static MyEntity getMyEntitySample2() {
        return new MyEntity().id(2L).name("name2").description("description2");
    }

    public static MyEntity getMyEntityRandomSampleGenerator() {
        return new MyEntity().id(longCount.incrementAndGet()).name(UUID.randomUUID().toString()).description(UUID.randomUUID().toString());
    }
}
