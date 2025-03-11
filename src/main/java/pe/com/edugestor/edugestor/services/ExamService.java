package pe.com.edugestor.edugestor.services;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pe.com.edugestor.edugestor.models.Exam;
import pe.com.edugestor.edugestor.repositories.ExamRepository;

@Service
public class ExamService {
    
    @Autowired
    private ExamRepository examRepository;

    public List<Exam> listAllExams(){
        return this.examRepository.findAll();
    }

    public Exam createExam(Exam entity){
        return this.examRepository.saveAndFlush(entity);
    }

    public Exam getExamByID(Long id){
        return this.examRepository.findById(id).orElse(null);
    }
}
