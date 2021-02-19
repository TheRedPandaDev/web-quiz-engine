package engine;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Quiz {
    private int id;
    private String title;
    private String text;
    private String[] options;
    @JsonIgnore
    private List<Integer> answer;
}
