package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Queue;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Queue entity.
 */
@Repository
public interface QueueRepository extends JpaRepository<Queue, Long> {
    @Query("select queue from Queue queue where queue.user.login = ?#{authentication.name}")
    List<Queue> findByUserIsCurrentUser();

    default Optional<Queue> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Queue> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Queue> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(value = "select queue from Queue queue left join fetch queue.user", countQuery = "select count(queue) from Queue queue")
    Page<Queue> findAllWithToOneRelationships(Pageable pageable);

    @Query("select queue from Queue queue left join fetch queue.user")
    List<Queue> findAllWithToOneRelationships();

    @Query("select queue from Queue queue left join fetch queue.user where queue.id =:id")
    Optional<Queue> findOneWithToOneRelationships(@Param("id") Long id);
}
