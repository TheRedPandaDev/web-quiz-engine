package engine.controller;

import engine.model.entity.*;
import engine.service.QuizServiceImpl;
import engine.service.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.security.Principal;
import java.util.*;

@RestController
@RequestMapping("/api")
@Validated
public class Controller {
    @Autowired
    QuizServiceImpl quizService;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/quiz")
    @ResponseBody
    public ResponseEntity<Quiz> getQuiz() {
        return new ResponseEntity<>(new Quiz(1L,
                "The Java Logo",
                "What is depicted on the Java logo?",
                new String[] {"Robot", "Tea leaf", "Cup of coffee", "Bug"},
                new Integer[] {2},
                new User()),
                HttpStatus.OK);
    }

    @PostMapping("/quiz")
    public ResponseEntity<Feedback> postQuiz(@RequestParam("answer") int answer) {
        if (answer == 2) {
            return new ResponseEntity<>(new Feedback(true,
                    "Congratulations, you're right!"),
                    HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new Feedback(false,
                    "Wrong answer! Please, try again."),
                    HttpStatus.OK);
        }
    }

    @PostMapping(value = "/quizzes", consumes = "application/json")
    public ResponseEntity<Quiz> postQuizzes(@Valid @RequestBody IncompleteQuiz incompleteQuiz, Principal principal) {
        return new ResponseEntity<>(quizService.saveQuiz(incompleteQuiz, principal), HttpStatus.OK);
    }

    @GetMapping("/quizzes")
    @ResponseBody
    public ResponseEntity<List<Quiz>> getQuizzes() {
        return new ResponseEntity<>(quizService.getAllQuizzes(), HttpStatus.OK);
    }

    @GetMapping("/quizzes/{id}")
    @ResponseBody
    public ResponseEntity<Quiz> getQuizzesId(@PathVariable("id") Long id) {
        return new ResponseEntity<>(quizService.getQuizById(id), HttpStatus.OK);
    }

    @DeleteMapping("/quizzes/{id}")
    public ResponseEntity<String> deleteQuizzesId(@PathVariable("id") Long id, Principal principal) {
        return quizService.deleteQuizById(id, principal);
    }

    @PostMapping("/quizzes/{id}/solve")
    public ResponseEntity<Feedback> postQuizzesIdSolve(
            @PathVariable("id") @Min(1) Long id, @Valid @RequestBody Answer answer) {
        return new ResponseEntity<>(quizService.solveQuizById(id, answer), HttpStatus.OK);
    }

    @PostMapping("/register")
    public ResponseEntity<String> createUser(@Valid @RequestBody User user) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        User savedUser = null;
        System.out.println(user);
        try {
            savedUser = userRepository.save(user);
        } catch (Exception e) {
            return new ResponseEntity<>("The email is already taken by another user", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>("The registration has been completed successfully", HttpStatus.OK);
    }
}