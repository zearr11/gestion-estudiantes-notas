package pe.com.edugestor.edugestor.controllers;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pe.com.edugestor.edugestor.models.Course;
import pe.com.edugestor.edugestor.models.Material;
import pe.com.edugestor.edugestor.models.Section;
import pe.com.edugestor.edugestor.services.CourseService;
import pe.com.edugestor.edugestor.services.MaterialService;
import pe.com.edugestor.edugestor.services.SectionService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;



@Controller
@RequestMapping("/material")
public class MaterialController {

    @Autowired
    private CourseService courseService;

    @Autowired
    private SectionService sectionService;

    @Autowired
    private MaterialService materialService;

    private Material materialDefault = new Material(null, null, null, null);
    private String urlSection = null;
    
    @GetMapping("/{url}")
    public String goViewMaterial(@PathVariable String url, Model model) {

        String[] parts = url.split("-"); String idSection = parts[parts.length - 1]; String idCourse = parts[parts.length - 2];
        
        urlSection = url;
        Course courseSelected = this.courseService.getCourseByID(Long.parseLong(idCourse));
        Section section = this.sectionService.getSectiontByID(Long.parseLong(idSection));
        List<Material> materialSection = new ArrayList<>();

        for (Material material : this.materialService.listAllMaterials()) {
            if(material.getSection().getIdSection().equals(section.getIdSection())){
                materialSection.add(material);
            }
        }

        materialDefault.setSection(section);
        setMaterialTitle(materialDefault, section.getIdSection());

        model.addAttribute("course", courseSelected);
        model.addAttribute("section", section);
        model.addAttribute("materials", materialSection);

        return "professor/material";
    }

    private void setMaterialTitle(Material material, Long idSection){
        int count = 0;
        for (Material materiall : this.materialService.listAllMaterials()) {
            if (materiall.getSection().getIdSection().equals(idSection)){
                count += 1;
            }
        }
        material.setTitleMaterial("Sesión " + count);
    }

    @PostMapping("/agregar-sesion")
    public String addSesion(@RequestParam("description-sesion") String description) {
        
        materialDefault.setDescriptionMaterial(description);
        this.materialService.createMaterial(materialDefault);
        
        // Correccion de ruta, problema con el acento en las palabras
        try {
            String encodedUrl = URLEncoder.encode(urlSection, StandardCharsets.UTF_8);
            encodedUrl = encodedUrl.replace("+", "%20");
            
            System.out.println("URL después de encodear: " + encodedUrl);
            return "redirect:/material/" + encodedUrl;
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/";
        }

    }
    
    
}
