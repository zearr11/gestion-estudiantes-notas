package pe.com.edugestor.edugestor.dto;

import java.time.LocalDate;
import java.time.LocalTime;

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
    private String nameSesion; // Sesion [1,2,3,etc] (General)
    private String titleResourceD; // Titulo del Material (General)
    private String descriptionResourceD; // Descripcion (General)
    private String typeResourceD; // Tipo de Recurso [Enlace Web o Archivo] (Recurso)
    private String urlD; // Enlace Web (Recurso)
    private MultipartFile fileArchiveD; // Archivo (General)

    private String typeMaterialOption; // Tipo de Material [Recurso o Evaluacion] (General)
    private LocalDate limitDate; // Fecha limite (Evaluacion)
    private LocalTime limiTime; // Hora limite (Evaluacion)
}
