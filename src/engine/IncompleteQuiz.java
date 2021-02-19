package engine;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class IncompleteQuiz {
    @NotEmpty(message = "The title is required")
    private String title;
    @NotEmpty(message = "The text is required")
    private String text;
    @NotNull
    @Size(min = 2, message = "At least 2 options are required")
    private String[] options;
    private ArrayList<Integer> answer;
}
