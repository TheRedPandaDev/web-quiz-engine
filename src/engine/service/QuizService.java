package engine.service;

import engine.model.entity.Answer;
import engine.model.entity.Feedback;
import engine.model.entity.IncompleteQuiz;
import engine.model.entity.Quiz;
import org.springframework.http.ResponseEntity;

import java.security.Principal;
import java.util.List;

public interface QuizService {
    /**
     * Get all quizzes.
     * @return List of all quizzes.
     */
    List<Quiz> getAllQuizzes();

    /**
     * Get quiz by id.
     * @param id Id
     * @return Qiz
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
    Feedback solveQuizById(Long id, Answer answer);

    /**
     * Delete quiz by id.
     * @param id Id
     * @param principal Principal
     * @return ResponseEntity<String>
     */
    ResponseEntity<String> deleteQuizById(Long id, Principal principal);
}
