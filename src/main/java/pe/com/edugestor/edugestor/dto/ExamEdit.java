package pe.com.edugestor.edugestor.dto;

import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ExamEdit {
    
    private Long idArchiveExamUp;
    private Long idUploadExam;
    private String editTextOpcional;
    private MultipartFile editFileOpcional;
    private String selectArchive;

}
