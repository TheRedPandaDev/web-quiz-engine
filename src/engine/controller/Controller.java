package engine.controller;

import engine.model.entity.*;
import engine.service.CompletionDTO;
import engine.service.QuizServiceImpl;
import engine.service.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.security.Principal;
import java.util.List;

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
                new User(),
                List.of()),
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
    public ResponseEntity<Page<Quiz>> getQuizzes(@RequestParam(value = "page", defaultValue = "0") @Min(0) int page) {
        return new ResponseEntity<>(quizService.getAllQuizzes(page), HttpStatus.OK);
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
            @PathVariable("id") @Min(1) Long id, @Valid @RequestBody Answer answer, Principal principal) {
        return new ResponseEntity<>(quizService.solveQuizById(id, answer, principal), HttpStatus.OK);
    }

    @GetMapping("/quizzes/completed")
    public ResponseEntity<Page<CompletionDTO>> getCompleted(
            @RequestParam(value = "page", defaultValue = "0") @Min(0) int page,
            Principal principal) {
        return new ResponseEntity<>(quizService.getCompletionsByUser(page, principal), HttpStatus.OK);
    }

    @PostMapping("/register")
    public ResponseEntity<String> createUser(@Valid @RequestBody User user) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        try {
            userRepository.save(user);
        } catch (Exception e) {
            return new ResponseEntity<>("The email is already taken by another user", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>("The registration has been completed successfully", HttpStatus.OK);
    }
}
