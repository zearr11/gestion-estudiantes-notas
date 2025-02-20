package pe.com.edugestor.edugestor.services;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pe.com.edugestor.edugestor.models.Course;
import pe.com.edugestor.edugestor.repositories.CourseRepository;

@Service
public class CourseService {
    
    @Autowired
    private CourseRepository courseRepository;

    public List<Course> listAllCourses(){
        return this.courseRepository.findAll();
    }

    public Course createCourse(Course courseToSave){
        return this.courseRepository.saveAndFlush(courseToSave);
    }

    public Course getCourseByID(Long id){
        return this.courseRepository.findById(id).orElse(null);
    }

    public Course updateCourse(Course entity){
        
        Course courseToUpdate = this.courseRepository.findById(entity.getIdCourse()).orElse(null);

        if (courseToUpdate == null) 
            return null;

        courseToUpdate.setDescription(entity.getDescription());
        courseToUpdate.setNameCourse(entity.getNameCourse());
        courseToUpdate.setProfessor(entity.getProfessor());

        return this.courseRepository.saveAndFlush(courseToUpdate);

    }
}
