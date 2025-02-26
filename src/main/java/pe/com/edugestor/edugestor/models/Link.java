package pe.com.edugestor.edugestor.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
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
@Table(name = "link")
public class Link {
    
    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY)
    private Long idLink;

    @Column(name = "link_web", length = 2083)
    private String linkWeb;

    @ManyToOne
    @JoinColumn(name = "id_resource")
    private Resource resource;
}
