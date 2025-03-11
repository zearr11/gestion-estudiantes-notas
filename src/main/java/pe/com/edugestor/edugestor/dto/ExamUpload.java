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
public class ExamUpload {
    
    private Long idResourceState;
    private Long idExamOptionalForm;
    private String addTextOpcional;
    private MultipartFile addFileOpcional;

}
