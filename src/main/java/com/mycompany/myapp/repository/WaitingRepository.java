package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Waiting;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Waiting entity.
 */
@SuppressWarnings("unused")
@Repository
public interface WaitingRepository extends JpaRepository<Waiting, Long> {}
