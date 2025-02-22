package pe.com.edugestor.edugestor.models;

import java.time.LocalTime;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "section")
public class Section {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idSection;

    @Column(name = "cod_section")
    private String codSection;

    @Column(name = "hour_start")
    private LocalTime hourStart;

    @Column(name = "hour_end")
    private LocalTime hourEnd;

    @Column(name = "state")
    private String state;

    @ManyToMany
    @JoinTable(
        name = "section_day",
        joinColumns = @JoinColumn(name = "id_section"),
        inverseJoinColumns = @JoinColumn(name = "id_day")
    )
    private List<Day> day;

    @ManyToOne
    @JoinColumn(name = "id_course")
    private Course course;

    @ManyToMany
    @JoinTable(
        name = "section_student",
        joinColumns = @JoinColumn(name = "id_section"),
        inverseJoinColumns = @JoinColumn(name = "id_student")
    )
    private List<Student> students;
}
