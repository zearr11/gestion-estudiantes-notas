package pe.com.edugestor.edugestor.services;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pe.com.edugestor.edugestor.models.Resource;
import pe.com.edugestor.edugestor.repositories.ResourceRepository;

@Service
public class ResourceService {
    
    @Autowired
    private ResourceRepository resourceRepository;

    public List<Resource> listAllResources(){
        return this.resourceRepository.findAll();
    }

    public Resource createResource(Resource entity){
        return this.resourceRepository.saveAndFlush(entity);
    }

    public Resource getResourceByID(Long id){
        return this.resourceRepository.findById(id).orElse(null);
    }

    public Resource updateResource(Resource entity){
        
        Resource resourceToUpdate = this.resourceRepository.findById(entity.getIdResource()).orElse(null);

        if (resourceToUpdate == null) 
            return null;

        resourceToUpdate.setDescriptionResorce(entity.getDescriptionResorce());
        resourceToUpdate.setLinks(entity.getLinks());
        resourceToUpdate.setMaterial(entity.getMaterial());
        resourceToUpdate.setNameResource(entity.getNameResource());
        resourceToUpdate.setTypeResource(entity.getTypeResource());
        resourceToUpdate.setUploadDate(entity.getUploadDate());

        return this.resourceRepository.saveAndFlush(resourceToUpdate);
    }

}
