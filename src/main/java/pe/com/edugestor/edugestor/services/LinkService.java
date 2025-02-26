package pe.com.edugestor.edugestor.services;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pe.com.edugestor.edugestor.models.Link;
import pe.com.edugestor.edugestor.repositories.LinkRepository;

@Service
public class LinkService {
    
    @Autowired
    private LinkRepository linkRepository;

    public List<Link> listAllLinks(){
        return this.linkRepository.findAll();
    }

    public Link createLink(Link entity){
        return this.linkRepository.saveAndFlush(entity);
    }

    public Link getLinkByID(Long id){
        return this.linkRepository.findById(id).orElse(null);
    }

    public Link updateLink(Link entity){
        
        Link linkToUpdate = this.linkRepository.findById(entity.getIdLink()).orElse(null);

        if (linkToUpdate == null) 
            return null;

        linkToUpdate.setLinkWeb(entity.getLinkWeb());
        linkToUpdate.setResource(entity.getResource());

        return this.linkRepository.saveAndFlush(linkToUpdate);
    }

}
