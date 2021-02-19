package engine;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public final class QuizService {
    private int idSeq = 1;
    private final Map<Integer, Quiz> quizMap = new HashMap<>();

    public Quiz createQuiz(IncompleteQuiz incompleteQuiz) {
        Quiz newQuiz = new Quiz();

        newQuiz.setId(idSeq++);
        newQuiz.setTitle(incompleteQuiz.getTitle());
        newQuiz.setText(incompleteQuiz.getText());
        newQuiz.setOptions(incompleteQuiz.getOptions());
        newQuiz.setAnswer(incompleteQuiz.getAnswer());

        quizMap.put(newQuiz.getId(), newQuiz);

        return newQuiz;
    }

    public Collection<Quiz> getAllQuizzes() {
        return quizMap.values();
    }

    public Quiz getQuizById(int id) {
        Quiz reqQuiz = quizMap.get(id);
        if (reqQuiz == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        } else {
            return reqQuiz;
        }
    }

    public Response solveQuizById(int id, Answer answer) {
        Quiz reqQuiz = quizMap.get(id);
        if (reqQuiz == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        } else {
            boolean flag = true;
            List<Integer> answersList = reqQuiz.getAnswer();
            List<Integer> answersInputList = answer.getAnswer();
            if (answersList != null && answersList.size() > 0) {
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
            } else if (answersInputList != null && answersInputList.size() > 0) {
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
