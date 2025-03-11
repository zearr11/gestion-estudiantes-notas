package pe.com.edugestor.edugestor.models;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "exam")
public class Exam {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idExam;

    @Column(name = "limit_date")
    private LocalDate limitDate;

    @Column(name = "limit_time")
    private LocalTime limitTime;

    @ManyToOne
    @JoinColumn(name = "id_resource")
    private Resource resourceEX;

    @OneToMany(mappedBy = "exam")
    private List<UploadExam> uploadExams;
}
