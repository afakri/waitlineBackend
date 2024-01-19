package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.QueueTestSamples.*;
import static com.mycompany.myapp.domain.WaitingTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class QueueTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Queue.class);
        Queue queue1 = getQueueSample1();
        Queue queue2 = new Queue();
        assertThat(queue1).isNotEqualTo(queue2);

        queue2.setId(queue1.getId());
        assertThat(queue1).isEqualTo(queue2);

        queue2 = getQueueSample2();
        assertThat(queue1).isNotEqualTo(queue2);
    }

    @Test
    void waitingTest() throws Exception {
        Queue queue = getQueueRandomSampleGenerator();
        Waiting waitingBack = getWaitingRandomSampleGenerator();

        queue.addWaiting(waitingBack);
        assertThat(queue.getWaitings()).containsOnly(waitingBack);
        assertThat(waitingBack.getQueue()).isEqualTo(queue);

        queue.removeWaiting(waitingBack);
        assertThat(queue.getWaitings()).doesNotContain(waitingBack);
        assertThat(waitingBack.getQueue()).isNull();

        queue.waitings(new HashSet<>(Set.of(waitingBack)));
        assertThat(queue.getWaitings()).containsOnly(waitingBack);
        assertThat(waitingBack.getQueue()).isEqualTo(queue);

        queue.setWaitings(new HashSet<>());
        assertThat(queue.getWaitings()).doesNotContain(waitingBack);
        assertThat(waitingBack.getQueue()).isNull();
    }
}
