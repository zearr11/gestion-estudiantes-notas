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
public class ResourceDTO {
    private String nameSesion;
    private String titleResourceD;
    private String descriptionResourceD;
    private String typeResourceD;
    private String urlD;
    private MultipartFile fileArchiveD;
}
