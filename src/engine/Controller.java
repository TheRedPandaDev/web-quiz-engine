package engine;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class Controller {
    // TO DO: СДЕЛАТЬ QUIZ SERVICE!!!

    private final Map<Integer, Quiz> quizMap = new HashMap<>();

    @GetMapping("/quiz")
    @ResponseBody
    public Quiz serveQuiz() {
        return new Quiz(1, "The Java Logo", "What is depicted on the Java logo?", new String[] {"Robot", "Tea leaf", "Cup of coffee", "Bug"}, 2);
    }

    @PostMapping("/quiz")
    public Response serveResponse(@RequestParam int answer) {
        if (answer == 2) {
            return new Response(true, "Congratulations, you're right!");
        } else {
            return new Response(false, "Wrong answer! Please, try again.");
        }
    }

    @PostMapping(value = "/quizzes", consumes = "application/json")
    public Quiz createQuiz(@RequestBody IncompleteQuiz incompleteQuiz) {
        Quiz newQuiz = new Quiz(incompleteQuiz);
        quizMap.put(newQuiz.getId(), newQuiz);
        return newQuiz;
    }

    @GetMapping("/quizzes")
    @ResponseBody
    public Collection<Quiz> getAllQuizzes() {
        return quizMap.values();
    }

    @GetMapping("/quizzes/{id}")
    @ResponseBody
    public Quiz getQuizById(@PathVariable int id) {
        Quiz reqQuiz = quizMap.get(id);
        if (reqQuiz == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        } else {
            return reqQuiz;
        }
    }

    @PostMapping("/quizzes/{id}/solve")
    public Response solveQuizById(@PathVariable int id, @RequestParam int answer) {
        Quiz reqQuiz = quizMap.get(id);
        if (reqQuiz == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        } else {
            if (reqQuiz.getAnswer() == answer) {
                return new Response(true, "Congratulations, you're right!");
            } else {
                return new Response(false, "Wrong answer! Please, try again.");
            }
        }
    }
}
