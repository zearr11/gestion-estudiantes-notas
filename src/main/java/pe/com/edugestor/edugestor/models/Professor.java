package pe.com.edugestor.edugestor.models;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
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
    private Long idProfessor;

    @Column(name = "specialty", length = 100, nullable = false)
    private String specialty;

    @ManyToOne
    @JoinColumn(name = "id_person")
    private Person person;

    @ManyToMany(mappedBy = "professor")
    private List<Student> student;

    @OneToMany(mappedBy = "professor")
    private List<Course> courses;
}
