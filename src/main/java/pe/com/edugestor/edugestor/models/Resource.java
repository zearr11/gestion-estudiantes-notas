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
@Table(name = "resource")
public class Resource {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idResource;

    @Column(name = "uploadDate")
    private LocalDateTime uploadDate;

    @Column(name = "name_resource")
    private String nameResource;
    
    @Column(name = "type_resource")
    private String typeResource;

    @Column(name = "description_resource", length = 2083)
    private String descriptionResorce;

    @OneToMany(mappedBy = "resource")
    private List<Link> links;

    @ManyToOne
    @JoinColumn(name = "id_material")
    private Material material;

    @OneToMany(mappedBy = "resourceEntity")
    private List<Archive> lstArchives;
}
