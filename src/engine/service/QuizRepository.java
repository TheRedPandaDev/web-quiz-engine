package engine.service;

import engine.model.entity.Quiz;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface QuizRepository extends PagingAndSortingRepository<Quiz, Long> {
}
