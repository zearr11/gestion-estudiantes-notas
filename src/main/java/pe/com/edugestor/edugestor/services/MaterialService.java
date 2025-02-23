package pe.com.edugestor.edugestor.services;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pe.com.edugestor.edugestor.models.Material;
import pe.com.edugestor.edugestor.repositories.MaterialRepository;

@Service
public class MaterialService {
    
    @Autowired
    private MaterialRepository materialRepository;

    public List<Material> listAllMaterials(){
        return this.materialRepository.findAll();
    }

    public Material createMaterial(Material entity){
        return this.materialRepository.saveAndFlush(entity);
    }

    public Material getMaterialByID(Long id){
        return this.materialRepository.findById(id).orElse(null);
    }

    public Material updateMaterial(Material entity){
        
        Material materialToUpdate = this.materialRepository.findById(entity.getIdMaterial()).orElse(null);

        if (materialToUpdate == null) 
            return null;

        materialToUpdate.setDescriptionMaterial(entity.getDescriptionMaterial());
        materialToUpdate.setSection(entity.getSection());
        materialToUpdate.setTitleMaterial(entity.getTitleMaterial());

        return this.materialRepository.saveAndFlush(materialToUpdate);
    }
}
