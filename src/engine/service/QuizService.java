package engine.service;

import engine.model.entity.*;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;

import java.security.Principal;
import java.util.List;

public interface QuizService {
    /**
     * Get all quizzes.
     * @param page Page
     * @return Page of quizzes
     */
    Page<Quiz> getAllQuizzes(int page);

    /**
     * Get quiz by id.
     * @param id Id
     * @return Quiz
     */
    Quiz getQuizById(Long id);

    /**
     * Save quiz.
     * @param quiz Quiz
     * @return Quiz
     */
    Quiz saveQuiz(IncompleteQuiz quiz, Principal principal);

    /**
     * Solve quiz by id.
     * @param id Id
     * @param answer Answer
     * @return Feedback
     */
    Feedback solveQuizById(Long id, Answer answer, Principal principal);

    /**
     * Delete quiz by id.
     * @param id Id
     * @param principal Principal
     * @return ResponseEntity<String>
     */
    ResponseEntity<String> deleteQuizById(Long id, Principal principal);

    /**
     * Get all completions of quizzes for a specified user
     * @param principal Principal
     * @return Page of completions
     */
    Page<CompletionDTO> getCompletionsByUser(int page, Principal principal);
}
