package pe.com.edugestor.edugestor.services;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pe.com.edugestor.edugestor.models.UploadExam;
import pe.com.edugestor.edugestor.repositories.UploadExamRepository;

@Service
public class UploadExamService {
    
    @Autowired
    private UploadExamRepository uploadExamRepository;

    public List<UploadExam> listAllUploadExam(){
        return this.uploadExamRepository.findAll();
    }

    public UploadExam createUploadExam(UploadExam entity){
        return this.uploadExamRepository.saveAndFlush(entity);
    }

    public UploadExam getUploadExamByID(Long id){
        return this.uploadExamRepository.findById(id).orElse(null);
    }

    public UploadExam updateUploadExam(UploadExam entity){

        UploadExam UploadExamToEdit = this.uploadExamRepository.findById(entity.getIdUploadExam()).orElse(null);

        if (UploadExamToEdit == null)
            return null;
        
        UploadExamToEdit.setExam(entity.getExam());
        UploadExamToEdit.setArchiveExams(entity.getArchiveExams());
        UploadExamToEdit.setGrades(entity.getGrades());
        UploadExamToEdit.setStudent(entity.getStudent());
        UploadExamToEdit.setTextOptional(entity.getTextOptional());
        UploadExamToEdit.setUploadDate(entity.getUploadDate());

        return this.uploadExamRepository.saveAndFlush(UploadExamToEdit);

    }
}
