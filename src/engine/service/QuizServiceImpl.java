package engine.service;

import engine.model.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public final class QuizServiceImpl implements QuizService {
    @SuppressWarnings("unused")
    @Autowired
    private QuizRepository quizRepository;

    @Autowired
    private UserRepository userRepository;

    public Quiz saveQuiz(IncompleteQuiz incompleteQuiz, Principal principal) {
        User user = userRepository.findByEmail(principal.getName());
        Quiz newQuiz = new Quiz();

        newQuiz.setTitle(incompleteQuiz.getTitle());
        newQuiz.setText(incompleteQuiz.getText());
        newQuiz.setOptions(incompleteQuiz.getOptions());
        newQuiz.setAnswer(incompleteQuiz.getAnswer());
        newQuiz.setUser(user);

        return quizRepository.save(newQuiz);
    }

    public List<Quiz> getAllQuizzes() {
        return (List<Quiz>) quizRepository.findAll();
    }

    public Quiz getQuizById(Long id) {
        return quizRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    public Feedback solveQuizById(Long id, Answer answer) {
        Quiz reqQuiz = quizRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        if (reqQuiz == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        } else {
            boolean flag = true;
            if (reqQuiz.getAnswer() != null) {
                if (answer.getAnswer() != null) {
                    List<Integer> answersList = Arrays.asList(reqQuiz.getAnswer());
                    List<Integer> answersInputList = Arrays.asList(answer.getAnswer());
                    if (answersList.size() > 0) {
                        if (answersList.size() != answersInputList.size()) {
                            flag = false;
                        } else {
                            for (Integer answerItem : answersList) {
                                if(!answersInputList.contains(answerItem)) {
                                    flag = false;
                                    break;
                                }
                            }
                        }
                    } else if (answersInputList.size() != 0) {
                        flag = false;
                    }
                } else {
                    flag = false;
                }
            } else if (answer.getAnswer() != null && answer.getAnswer().length > 0) {
                flag = false;
            }
            if (flag) {
                return new Feedback(true, "Congratulations, you're right!");
            } else {
                return new Feedback(false, "Wrong answer! Please, try again.");
            }
        }
    }

    @Override
    public ResponseEntity<String> deleteQuizById(Long id, Principal principal) {
        User user = userRepository.findByEmail(principal.getName());
        Optional<Quiz> optionalQuiz = quizRepository.findById(id);
        if (optionalQuiz.isEmpty()) {
            return new ResponseEntity<>("Not found", HttpStatus.NOT_FOUND);
        }
        Quiz quiz = optionalQuiz.get();
        if (!quiz.getUser().getId().equals(user.getId())) {
            return new ResponseEntity<>("Forbidden", HttpStatus.FORBIDDEN);
        }
        quizRepository.delete(quiz);
        return new ResponseEntity<>("Successful", HttpStatus.NO_CONTENT);
    }
}
