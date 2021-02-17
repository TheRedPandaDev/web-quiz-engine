package engine;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Quiz {
    private static int idSeq = 1;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private int id;
    private String title;
    private String text;
    private String[] options;
    @JsonIgnore
    private int answer;

    public Quiz(IncompleteQuiz incompleteQuiz) {
        this.id = idSeq++;
        this.title = incompleteQuiz.getTitle();
        this.text = incompleteQuiz.getText();
        this.options = incompleteQuiz.getOptions();
        this.answer = incompleteQuiz.getAnswer();
    }
}
