package pe.com.edugestor.edugestor.services;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pe.com.edugestor.edugestor.models.Grade;
import pe.com.edugestor.edugestor.repositories.GradeRepository;

@Service
public class GradeService {
    
    @Autowired
    private GradeRepository gradeRepository;

    public List<Grade> listAllGrade(){
        return this.gradeRepository.findAll();
    }

    public Grade createGrade(Grade entity){
        return this.gradeRepository.saveAndFlush(entity);
    }

    public Grade getGradeByID(Long id){
        return this.gradeRepository.findById(id).orElse(null);
    }

    public Grade updateGrade(Grade entity){

        Grade GradeToEdit = this.gradeRepository.findById(entity.getIdGrade()).orElse(null);

        if (GradeToEdit == null)
            return null;
        
        GradeToEdit.setCommentary(entity.getCommentary());
        GradeToEdit.setGradeExam(entity.getGradeExam());
        GradeToEdit.setUploadExam(entity.getUploadExam());

        return this.gradeRepository.saveAndFlush(GradeToEdit);

    }
}
