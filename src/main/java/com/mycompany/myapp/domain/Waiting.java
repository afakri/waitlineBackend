package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Waiting.
 */
@Entity
@Table(name = "waiting")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Waiting implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @Column(name = "phone", nullable = false)
    private String phone;

    @Column(name = "time_arrived")
    private Instant timeArrived;

    @Column(name = "time_summoned")
    private Instant timeSummoned;

    @Column(name = "time_done")
    private Instant timeDone;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "waitings", "user" }, allowSetters = true)
    private Queue queue;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Waiting id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Waiting name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return this.phone;
    }

    public Waiting phone(String phone) {
        this.setPhone(phone);
        return this;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Instant getTimeArrived() {
        return this.timeArrived;
    }

    public Waiting timeArrived(Instant timeArrived) {
        this.setTimeArrived(timeArrived);
        return this;
    }

    public void setTimeArrived(Instant timeArrived) {
        this.timeArrived = timeArrived;
    }

    public Instant getTimeSummoned() {
        return this.timeSummoned;
    }

    public Waiting timeSummoned(Instant timeSummoned) {
        this.setTimeSummoned(timeSummoned);
        return this;
    }

    public void setTimeSummoned(Instant timeSummoned) {
        this.timeSummoned = timeSummoned;
    }

    public Instant getTimeDone() {
        return this.timeDone;
    }

    public Waiting timeDone(Instant timeDone) {
        this.setTimeDone(timeDone);
        return this;
    }

    public void setTimeDone(Instant timeDone) {
        this.timeDone = timeDone;
    }

    public Queue getQueue() {
        return this.queue;
    }

    public void setQueue(Queue queue) {
        this.queue = queue;
    }

    public Waiting queue(Queue queue) {
        this.setQueue(queue);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Waiting)) {
            return false;
        }
        return getId() != null && getId().equals(((Waiting) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Waiting{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", phone='" + getPhone() + "'" +
            ", timeArrived='" + getTimeArrived() + "'" +
            ", timeSummoned='" + getTimeSummoned() + "'" +
            ", timeDone='" + getTimeDone() + "'" +
            "}";
    }
}
