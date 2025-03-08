package pe.com.edugestor.edugestor.controllers;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pe.com.edugestor.edugestor.dto.ResourceDTO;
import pe.com.edugestor.edugestor.models.Archive;
import pe.com.edugestor.edugestor.models.Course;
import pe.com.edugestor.edugestor.models.Link;
import pe.com.edugestor.edugestor.models.Material;
import pe.com.edugestor.edugestor.models.Resource;
import pe.com.edugestor.edugestor.models.Section;
import pe.com.edugestor.edugestor.services.ArchiveService;
import pe.com.edugestor.edugestor.services.CourseService;
import pe.com.edugestor.edugestor.services.LinkService;
import pe.com.edugestor.edugestor.services.MaterialService;
import pe.com.edugestor.edugestor.services.ResourceService;
import pe.com.edugestor.edugestor.services.SectionService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

@Controller
@RequestMapping("/material")
public class MaterialController {

    @Autowired
    private CourseService courseService;
    @Autowired
    private SectionService sectionService;
    @Autowired
    private MaterialService materialService;
    @Autowired
    private LinkService linkService;
    @Autowired
    private ResourceService resourceService;
    @Autowired
    private ArchiveService archiveService;

    private Material materialDefault = new Material(null, null, null, null, null);
    private String urlSection = null;
    private ResourceDTO resourceDTO = new ResourceDTO(null, null, null, null, null, null);
    List<Material> materialSection;
    List<Resource> resources;

    @GetMapping("/{url}")
    public String goViewMaterial(@PathVariable String url, Model model) {

        String[] parts = url.split("-");
        String idSection = parts[parts.length - 1];
        String idCourse = parts[parts.length - 2];

        urlSection = url;
        Course courseSelected = this.courseService.getCourseByID(Long.parseLong(idCourse));
        Section section = this.sectionService.getSectiontByID(Long.parseLong(idSection));
        materialSection = new ArrayList<>();
        resources = new ArrayList<>();
        Map<Long, String> linksForResorces = new HashMap<>();
        Map<Long, String> lstArchivesNames = new HashMap<>();
        Map<Long, Long> lstArchivesID = new HashMap<>();

        for (Material material : this.materialService.listAllMaterials()) {
            if (material.getSection().getIdSection().equals(section.getIdSection())) {
                materialSection.add(material);
                for (Resource resourceTemp : material.getResources()) {
                    resources.add(resourceTemp);
                    for (Link linkTemp : resourceTemp.getLinks()) {
                        linksForResorces.put(resourceTemp.getIdResource(), linkTemp.getLinkWeb());
                    }
                    for (Archive archiveTemp : resourceTemp.getLstArchives()) {
                        lstArchivesNames.put(resourceTemp.getIdResource(), archiveTemp.getNameArchiveOriginal());
                        lstArchivesID.put(resourceTemp.getIdResource(), archiveTemp.getIdArchive());
                    }
                }
            }
        }

        materialDefault.setSection(section);
        setMaterialTitle(materialDefault, section.getIdSection());

        model.addAttribute("course", courseSelected);
        model.addAttribute("section", section);
        model.addAttribute("materials", materialSection);
        model.addAttribute("resourceData", resourceDTO);

        model.addAttribute("resources", resources);
        model.addAttribute("linksForResorces", linksForResorces);
        model.addAttribute("lstArchivesNames", lstArchivesNames);
        model.addAttribute("lstArchivesID", lstArchivesID);

        return "professor/material";
    }

    private void setMaterialTitle(Material material, Long idSection) {
        int count = 0;
        for (Material materiall : this.materialService.listAllMaterials()) {
            if (materiall.getSection().getIdSection().equals(idSection)) {
                count += 1;
            }
        }
        material.setTitleMaterial("Sesión " + count);
    }

    @GetMapping("/descargar/{id}")
    public ResponseEntity<byte[]> downloadArchive(@PathVariable Long id) {
        
        Archive file = this.archiveService.getArchiveByID(id);

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(file.getFileType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getNameArchiveOriginal() + "\"")
                .body(file.getResourceArchive());
    }


    @PostMapping("/agregar-sesion")
    public String addSesion(@RequestParam("description-sesion") String description) {

        materialDefault.setDescriptionMaterial(description);
        this.materialService.createMaterial(materialDefault);

        // Correccion de ruta, problema con el acento en las palabras
        try {
            String encodedUrl = URLEncoder.encode(urlSection, StandardCharsets.UTF_8);
            encodedUrl = encodedUrl.replace("+", "%20");
            return "redirect:/material/" + encodedUrl;
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/";
        }

    }

    @PostMapping("/agregar-recurso")
    public String addResource(@ModelAttribute ResourceDTO dataResource) {
        
        LocalDateTime dateTime = LocalDateTime.now();
        Resource resourceToSave = new Resource(null, dateTime, dataResource.getTitleResourceD(), 
        dataResource.getTypeResourceD(), dataResource.getDescriptionResourceD(), null, null, null); 

        for (Material material : materialSection) {
            if (material.getTitleMaterial().equals(dataResource.getNameSesion())){
                resourceToSave.setMaterial(material);
                break;
            }
        }
        
        if (dataResource.getTypeResourceD().equals("Enlace Web")){
            
            Link linkToSave = new Link(null, dataResource.getUrlD(), resourceToSave);
            
            this.resourceService.createResource(resourceToSave);
            this.linkService.createLink(linkToSave);

        }
        else if (dataResource.getTypeResourceD().equals("Archivo")){

            try {
                Archive archiveToSave = new Archive(null, 
                dataResource.getFileArchiveD().getOriginalFilename(), 
                dataResource.getFileArchiveD().getContentType(), 
                dataResource.getFileArchiveD().getBytes(), resourceToSave);

                this.resourceService.createResource(resourceToSave);
                this.archiveService.saveToArchive(archiveToSave);
            }
            catch (IOException e) {}
            
        }

        try {
            String encodedUrl = URLEncoder.encode(urlSection, StandardCharsets.UTF_8);
            encodedUrl = encodedUrl.replace("+", "%20");
            return "redirect:/material/" + encodedUrl;
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/";
        }
    }

}
