package com.mycompany.myapp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class WaitingTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Waiting getWaitingSample1() {
        return new Waiting().id(1L).name("name1").phone("phone1");
    }

    public static Waiting getWaitingSample2() {
        return new Waiting().id(2L).name("name2").phone("phone2");
    }

    public static Waiting getWaitingRandomSampleGenerator() {
        return new Waiting().id(longCount.incrementAndGet()).name(UUID.randomUUID().toString()).phone(UUID.randomUUID().toString());
    }
}
