package pe.com.edugestor.edugestor.models;

import java.time.LocalDateTime;
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
@Table(name = "upload_exam")
public class UploadExam {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idUploadExam;

    @Column(name = "upload_date")
    private LocalDateTime uploadDate;

    @Column(name = "text_optional")
    private String textOptional;

    @ManyToOne
    @JoinColumn(name = "id_student")
    private Student student;

    @ManyToOne
    @JoinColumn(name = "id_exam")
    private Exam exam;

    @OneToMany(mappedBy = "uploadExam")
    private List<Grade> grades;

    @OneToMany(mappedBy = "uploadExamData")
    private List<ArchiveExam> archiveExams;

}
