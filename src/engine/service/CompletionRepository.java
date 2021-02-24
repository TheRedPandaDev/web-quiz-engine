package engine.service;

import engine.model.entity.Completion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

public interface CompletionRepository extends PagingAndSortingRepository<Completion, Long> {
    @Query("SELECT c FROM Completion c where c.user.email = :email order by c.completedAt desc")
    Page<Completion> findAllByUserOrderByCompletedAtDesc(@Param("email") String email, Pageable pageable);
}
