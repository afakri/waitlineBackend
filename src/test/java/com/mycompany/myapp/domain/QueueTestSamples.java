package com.mycompany.myapp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class QueueTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Queue getQueueSample1() {
        return new Queue().id(1L).name("name1");
    }

    public static Queue getQueueSample2() {
        return new Queue().id(2L).name("name2");
    }

    public static Queue getQueueRandomSampleGenerator() {
        return new Queue().id(longCount.incrementAndGet()).name(UUID.randomUUID().toString());
    }
}
