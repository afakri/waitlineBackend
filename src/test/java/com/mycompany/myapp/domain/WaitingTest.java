package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.QueueTestSamples.*;
import static com.mycompany.myapp.domain.WaitingTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class WaitingTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Waiting.class);
        Waiting waiting1 = getWaitingSample1();
        Waiting waiting2 = new Waiting();
        assertThat(waiting1).isNotEqualTo(waiting2);

        waiting2.setId(waiting1.getId());
        assertThat(waiting1).isEqualTo(waiting2);

        waiting2 = getWaitingSample2();
        assertThat(waiting1).isNotEqualTo(waiting2);
    }

    @Test
    void queueTest() throws Exception {
        Waiting waiting = getWaitingRandomSampleGenerator();
        Queue queueBack = getQueueRandomSampleGenerator();

        waiting.setQueue(queueBack);
        assertThat(waiting.getQueue()).isEqualTo(queueBack);

        waiting.queue(null);
        assertThat(waiting.getQueue()).isNull();
    }
}
