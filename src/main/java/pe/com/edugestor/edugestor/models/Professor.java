package pe.com.edugestor.edugestor.models;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity // Identifica como modelo a la clase en sb y jpa
@Getter // Genera los getter de la clase
@Setter // Genera los setter de la clase
@AllArgsConstructor // Genera el constructor con todos los atributos de la clase
@NoArgsConstructor // Genera el constructor vacio
@Table(name = "professor") // Personaliza la tabla en la bd
public class Professor {
    
    @Id // Identifica el ID de la tabla
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Hace al ID autoincremental
    private Long id;

    @Column(name = "name", length = 100, nullable = false) // Estructura de datos de la columna
    private String name;

    @Column(name = "lastname", length = 100, nullable = false)
    private String lastname;

    @Column(name = "dateBirth", nullable = false)
    private LocalDate dateBirth;

    @Column(name = "gender", length = 20, nullable = false)
    private String gender;

    @Column(name = "email", length = 100, nullable = false)
    private String email;

    @Column(name = "nid", unique = true, nullable = false)
    private int nid;

    @Column(name = "numberPhone", nullable = false)
    private int numberPhone;

    @Column(name = "address", length = 100, nullable = false)
    private String address;

    @Column(name = "specialty", length = 100, nullable = false)
    private String specialty;
    
}
