package pe.com.edugestor.edugestor.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pe.com.edugestor.edugestor.models.ArchiveExam;
import pe.com.edugestor.edugestor.repositories.ArchiveExamRepository;

@Service
public class ArchiveExamService {
    
    @Autowired
    private ArchiveExamRepository archiveExamRepository;

    public List<ArchiveExam> listAllArchiveExam(){
        return this.archiveExamRepository.findAll();
    }

    public ArchiveExam createArchiveExam(ArchiveExam entity){
        return this.archiveExamRepository.saveAndFlush(entity);
    }

    public ArchiveExam getArchiveExamByID(Long id){
        return this.archiveExamRepository.findById(id).orElse(null);
    }

    public ArchiveExam updateArchiveExam(ArchiveExam entity){

        ArchiveExam archiveExamToSave = this.archiveExamRepository.findById(entity.getIdArchiveExam()).orElse(null);

        if (archiveExamToSave == null)
            return null;
        
        archiveExamToSave.setFileType(entity.getFileType());
        archiveExamToSave.setNameArchiveOriginal(entity.getNameArchiveOriginal());
        archiveExamToSave.setResourceArchive(entity.getResourceArchive());
        archiveExamToSave.setUploadExamData(entity.getUploadExamData());

        return this.archiveExamRepository.saveAndFlush(archiveExamToSave);

    }
}
