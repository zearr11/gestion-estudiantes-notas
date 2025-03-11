package pe.com.edugestor.edugestor.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
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
@Table(name = "archive_exam")
public class ArchiveExam {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idArchiveExam;

    @Column(name = "name_archive_original")
    private String nameArchiveOriginal;

    @Column(name = "file_type")
    private String fileType;

    @Lob
    @Column(columnDefinition = "MEDIUMBLOB", name = "resource_archive")
    private byte[] resourceArchive;

    @ManyToOne
    @JoinColumn(name = "id_upload_exam")
    private UploadExam uploadExamData;
    
}
