package engine;

import java.util.List;

public interface QuizService {
    /**
     * Get all quizzes.
     * @return Collection of all quizzes.
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
    Quiz saveQuiz(IncompleteQuiz quiz);

    /**
     * Solve quiz by id.
     * @param id id
     * @param answer answer
     * @return Response
     */
    Response solveQuizById(Long id, Answer answer);
}
