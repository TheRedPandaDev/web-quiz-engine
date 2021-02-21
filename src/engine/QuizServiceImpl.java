package engine;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.List;

@Service
public final class QuizServiceImpl implements QuizService {
    @SuppressWarnings("unused")
    @Autowired
    private QuizRepository quizRepository;

    public Quiz saveQuiz(IncompleteQuiz incompleteQuiz) {
        Quiz newQuiz = new Quiz();

        newQuiz.setTitle(incompleteQuiz.getTitle());
        newQuiz.setText(incompleteQuiz.getText());
        newQuiz.setOptions(incompleteQuiz.getOptions());
        newQuiz.setAnswer(incompleteQuiz.getAnswer());

        quizRepository.save(newQuiz);

        return newQuiz;
    }

    public List<Quiz> getAllQuizzes() {
        return (List<Quiz>) quizRepository.findAll();
    }

    public Quiz getQuizById(Long id) {
        return quizRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    public Response solveQuizById(Long id, Answer answer) {
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
                return new Response(true, "Congratulations, you're right!");
            } else {
                return new Response(false, "Wrong answer! Please, try again.");
            }
        }
    }
}
