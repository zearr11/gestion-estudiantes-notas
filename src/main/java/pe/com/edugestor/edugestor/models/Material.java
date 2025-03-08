package pe.com.edugestor.edugestor.models;

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
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "material")
public class Material {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idMaterial;

    @Column(name = "title_material")
    private String titleMaterial;

    @Column(name = "description_material", length = 2083)
    private String descriptionMaterial;

    @ManyToOne
    @JoinColumn(name = "id_section")
    private Section section;

    @OneToMany(mappedBy = "material")
    private List<Resource> resources;
}
