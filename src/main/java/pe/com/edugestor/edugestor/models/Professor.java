package pe.com.edugestor.edugestor.models;

import java.sql.Date;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public class Professor {
    private Long id;
    private String name;
    private String lastname;
    private Date dateBirth;
    private String gender;
    private String email;
    private int numberPhone;
    private String address;
    private String specialty;
}
