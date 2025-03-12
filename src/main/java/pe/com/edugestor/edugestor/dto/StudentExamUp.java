package pe.com.edugestor.edugestor.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class StudentExamUp {
    
    private Long idStudent;
    private String namesAndLastname;
    private LocalDateTime timeUp;
    private String textExam;
    private Long idArchiveExam;
    private String nameArchiveExam;
    private String grade;

}
