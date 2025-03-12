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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import pe.com.edugestor.edugestor.dto.ExamEdit;
import pe.com.edugestor.edugestor.dto.ExamUpload;
import pe.com.edugestor.edugestor.dto.ResourceDTO;
import pe.com.edugestor.edugestor.dto.StudentExamUp;
import pe.com.edugestor.edugestor.models.Archive;
import pe.com.edugestor.edugestor.models.ArchiveExam;
import pe.com.edugestor.edugestor.models.Course;
import pe.com.edugestor.edugestor.models.Exam;
import pe.com.edugestor.edugestor.models.Grade;
import pe.com.edugestor.edugestor.models.Link;
import pe.com.edugestor.edugestor.models.Material;
import pe.com.edugestor.edugestor.models.Person;
import pe.com.edugestor.edugestor.models.Resource;
import pe.com.edugestor.edugestor.models.Section;
import pe.com.edugestor.edugestor.models.Student;
import pe.com.edugestor.edugestor.models.UploadExam;
import pe.com.edugestor.edugestor.models.User;
import pe.com.edugestor.edugestor.services.ArchiveExamService;
import pe.com.edugestor.edugestor.services.ArchiveService;
import pe.com.edugestor.edugestor.services.CourseService;
import pe.com.edugestor.edugestor.services.ExamService;
import pe.com.edugestor.edugestor.services.GradeService;
import pe.com.edugestor.edugestor.services.LinkService;
import pe.com.edugestor.edugestor.services.MaterialService;
import pe.com.edugestor.edugestor.services.PersonService;
import pe.com.edugestor.edugestor.services.ResourceService;
import pe.com.edugestor.edugestor.services.SectionService;
import pe.com.edugestor.edugestor.services.StudentService;
import pe.com.edugestor.edugestor.services.UploadExamService;
import pe.com.edugestor.edugestor.services.UserService;
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
    @Autowired
    private ExamService examService;

    @Autowired
    private UploadExamService uploadExamService;
    @Autowired
    private ArchiveExamService archiveExamService;
    @Autowired
    private GradeService gradeService;
    @Autowired
    private StudentService studentService;
    @Autowired
    private PersonService personService;
    @Autowired
    private UserService userService;

    private Material materialDefault = new Material(null, null, null, null, null);
    private String urlSection = null;
    private ResourceDTO resourceDTO = new ResourceDTO(null, 
    null, null, null, null, null, null, null, null);
    private ExamUpload examUpload = new ExamUpload(null, null, null, null);
    private ExamEdit examEdit = new ExamEdit(null, null, null, null, null);
    List<Material> materialSection;
    List<Resource> resources;

    @GetMapping("/{url}")
    public String goViewMaterial(@PathVariable String url, Model model) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String rol = userDetails.getAuthorities().iterator().next().getAuthority();

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
        Map<Long, String> lstExamsDates = new HashMap<>(); 
        Map<Long, String> lstExamsTimes = new HashMap<>();
        List<Exam> examsOfResource = new ArrayList<>();
        Map<String, Long> idExams = new HashMap<>();

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
                    for (Exam examTemp : resourceTemp.getExams()) {
                        lstExamsDates.put(resourceTemp.getIdResource(), examTemp.getLimitDate()+"");
                        lstExamsTimes.put(resourceTemp.getIdResource(), examTemp.getLimitTime()+"");
                        examsOfResource.add(examTemp);
                        idExams.put("Evaluacion", examTemp.getIdExam());
                    }
                }
            }
        }


        if (!rol.equals("ROLE_Profesor")) {
            
            Map<Long, Long> idUploadExams = new HashMap<>();
            Map<Long, String> textUploadExams = new HashMap<>();
            Map<Long, String> dateUploadExams = new HashMap<>();
            Map<Long, Long> IDArchiveExamsOptionals = new HashMap<>();
            Map<Long, String> ArchiveExamsNameOriginal = new HashMap<>();
            Map<Long, String> NoteGrades = new HashMap<>();
            Map<Long, Long> lstIdExamResources = new HashMap<>();
            
            for (int i = 0; i < examsOfResource.size(); i++) {
                
                lstIdExamResources.put(examsOfResource.get(i).getResourceEX().getIdResource(), 
                    examsOfResource.get(i).getIdExam());

                if (examsOfResource.get(i).getUploadExams().size()>0) {
                    
                    Long idResource = examsOfResource.get(i).getResourceEX().getIdResource();
                    UploadExam uploadExam = examsOfResource.get(i).getUploadExams().get(0);

                    String texto = uploadExam.getTextOptional();
                    String dateUpload = uploadExam.getUploadDate()+"";
                    
                    
                    idUploadExams.put(idResource, uploadExam.getIdUploadExam());
                    textUploadExams.put(idResource, texto);
                    dateUploadExams.put(idResource, dateUpload);

                    if (uploadExam.getArchiveExams().size()>0) {
                        ArchiveExam archiveExam = uploadExam.getArchiveExams().get(0);
                        IDArchiveExamsOptionals.put(idResource, archiveExam.getIdArchiveExam());
                        ArchiveExamsNameOriginal.put(idResource, archiveExam.getNameArchiveOriginal());
                    }
                    if (uploadExam.getGrades().size()>0) {
                        Grade grade = uploadExam.getGrades().get(0);
                        NoteGrades.put(idResource, grade.getGradeExam());
                    }
                    
                }
            }

            model.addAttribute("idUploadExams", idUploadExams);
            model.addAttribute("textUploadExams", textUploadExams);
            model.addAttribute("dateUploadExams", dateUploadExams);
            model.addAttribute("IDArchiveExamsOptionals", IDArchiveExamsOptionals);
            model.addAttribute("ArchiveExamsNameOriginal", ArchiveExamsNameOriginal);
            model.addAttribute("NoteGrades", NoteGrades);
            model.addAttribute("lstIdExamResources", lstIdExamResources);
            model.addAttribute("examUpload", examUpload);
            model.addAttribute("examEdit", examEdit);
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

        model.addAttribute("lstExamsDates", lstExamsDates);
        model.addAttribute("lstExamsTimes", lstExamsTimes);

        model.addAttribute("idExams", idExams);

        String ruta = (rol.equals("ROLE_Profesor")) ? "professor/material" : "student/material";
        
        return ruta;
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
                                  dataResource.getTypeResourceD(), dataResource.getDescriptionResourceD(), null, null, null, null); 
        
        for (Material material : materialSection) {
            if (material.getTitleMaterial().equals(dataResource.getNameSesion())){
                resourceToSave.setMaterial(material);
                break;
            }
        }


        if (dataResource.getTypeMaterialOption().equals("Evaluacion")){

            resourceToSave.setTypeResource(dataResource.getTypeMaterialOption());
            
            Exam examToSave = new Exam(null, dataResource.getLimitDate(), dataResource.getLimiTime(),
            resourceToSave, null);

            this.resourceService.createResource(resourceToSave);
            this.examService.createExam(examToSave);

            if (dataResource.getFileArchiveD().getSize() > 0) {
                try {
                    Archive archiveToSave = new Archive(null, 
                    dataResource.getFileArchiveD().getOriginalFilename(), 
                    dataResource.getFileArchiveD().getContentType(), 
                    dataResource.getFileArchiveD().getBytes(), resourceToSave);
                    this.archiveService.saveToArchive(archiveToSave);
                }
                catch (IOException e) {}
            }
        }
        else {

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

    @PostMapping("/entrega-examen")
    public String uploadExamStudent(@ModelAttribute ExamUpload dataExamUpload) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User userLoged = this.userService.getUserByCod(userDetails.getUsername());
        Person personLoged = this.personService.getPersonByUser(userLoged);
        Student studentLoged = this.studentService.getStudentByPerson(personLoged);
        Exam examGet = this.examService.getExamByID(dataExamUpload.getIdExamOptionalForm());
        
        // Crear Examen -- El examen aun no existe (INSERT)

        UploadExam uploadExamToSave = new UploadExam(null, LocalDateTime.now(), dataExamUpload.getAddTextOpcional(),
        studentLoged, examGet, null, null );

        uploadExamToSave = this.uploadExamService.createUploadExam(uploadExamToSave);

        if (dataExamUpload.getAddFileOpcional().getSize() > 0) {
            try {
                ArchiveExam archiveExamToSave = new ArchiveExam(null, 
                dataExamUpload.getAddFileOpcional().getOriginalFilename(), 
                dataExamUpload.getAddFileOpcional().getContentType(), 
                dataExamUpload.getAddFileOpcional().getBytes(), uploadExamToSave);
                this.archiveExamService.createArchiveExam(archiveExamToSave);
            }
            catch (IOException e) {}
        }

        // if (dataExamUpload.getIdUploadExam() == 0 || dataExamUpload.getIdArchiveExamUp() == 0) {}

        try {
            String encodedUrl = URLEncoder.encode(urlSection, StandardCharsets.UTF_8);
            encodedUrl = encodedUrl.replace("+", "%20");
            return "redirect:/material/" + encodedUrl;
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/";
        }
    }

    @PostMapping("/editar-examen")
    public String updateExamStudent(@ModelAttribute ExamEdit dataExamEdit) {

        UploadExam uploadExam = this.uploadExamService.getUploadExamByID(dataExamEdit.getIdUploadExam());
        uploadExam.setTextOptional(dataExamEdit.getEditTextOpcional());
        uploadExam.setUploadDate(LocalDateTime.now());
        uploadExam = this.uploadExamService.updateUploadExam(uploadExam);

        if (dataExamEdit.getIdArchiveExamUp() != 0) { // El archivo existe
            
            if (!dataExamEdit.getSelectArchive().equals("0")) { // (1) Quiere modificar su archivo
                
                if (dataExamEdit.getEditFileOpcional().getSize() > 0) {

                    ArchiveExam archiveExamToUpdate = this.archiveExamService.getArchiveExamByID(dataExamEdit.getIdArchiveExamUp());
                    try {
                        archiveExamToUpdate.setFileType(dataExamEdit.getEditFileOpcional().getContentType());
                        archiveExamToUpdate.setNameArchiveOriginal(dataExamEdit.getEditFileOpcional().getOriginalFilename());
                        archiveExamToUpdate.setResourceArchive(dataExamEdit.getEditFileOpcional().getBytes());
                        this.archiveExamService.updateArchiveExam(archiveExamToUpdate);
                    }
                    catch (IOException e) {}

                }

            }

        }
        else { // El archivo no existe

            if (dataExamEdit.getEditFileOpcional().getSize() > 0) { // Quiere agregar un archivo

                try {
                    ArchiveExam archiveExamToSave = new ArchiveExam(null, 
                    dataExamEdit.getEditFileOpcional().getOriginalFilename(), 
                    dataExamEdit.getEditFileOpcional().getContentType(), 
                    dataExamEdit.getEditFileOpcional().getBytes(), uploadExam);
                    this.archiveExamService.createArchiveExam(archiveExamToSave);
                }
                catch (IOException e) {}

            }

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

    @GetMapping("/descargar/entrega/{id}")
    public ResponseEntity<byte[]> downloadArchiveSend(@PathVariable Long id) {
        
        ArchiveExam file = this.archiveExamService.getArchiveExamByID(id);

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(file.getFileType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getNameArchiveOriginal() + "\"")
                .body(file.getResourceArchive());
    }


    @GetMapping("/examen/datos")
    @ResponseBody
    public List<StudentExamUp> obtenerDatosExamen(@RequestParam("idExam") Long idExam) {
        return returnLstDataExams(idExam);
    }

    public List<StudentExamUp> returnLstDataExams(Long idExam) {

        Exam examToList = this.examService.getExamByID(idExam);
        Resource resourceToList = examToList.getResourceEX();
        Material materialToList = resourceToList.getMaterial();
        Section section = materialToList.getSection();
        List<StudentExamUp> studentExamUps = new ArrayList<>();
        List<ArchiveExam> archiveExams = this.archiveExamService.listAllArchiveExam();
        List<Grade> grades = this.gradeService.listAllGrade();

        for (Student student : section.getStudents()) {

            UploadExam uploadExam = examToList.getUploadExams()
            .stream()
            .filter(u -> u.getStudent().equals(student))
            .findFirst()
            .orElse(null);
        
            if (uploadExam != null) { // El estudiante entregó examen
                Long idStudent = student.getIdStudent();
                String namesAndLastname = student.getPerson().getName() + " " + student.getPerson().getLastname();
                LocalDateTime timeUp = uploadExam.getUploadDate();
                String textExam = (!uploadExam.getTextOptional().equals("")) ? uploadExam.getTextOptional() : "Sin Descripción";
                
                // Si usas archiveExams de manera similar, deberías buscar el archivo relacionado al uploadExam
                ArchiveExam archiveExam = archiveExams.stream()
                    .filter(ae -> ae.getUploadExamData().equals(uploadExam))
                    .findFirst()
                    .orElse(null);

                Grade gradeExam = grades.stream()
                .filter( ge -> ge.getUploadExam().equals(uploadExam))
                .findFirst()
                .orElse(null);

                String grade = gradeExam != null ? gradeExam.getGradeExam() : null;

                Long idArchiveExam = archiveExam != null ? archiveExam.getIdArchiveExam() : null;
                String nameArchiveExam = archiveExam != null ? archiveExam.getNameArchiveOriginal() : "No adjunto archivo";
                
                studentExamUps.add(new StudentExamUp(idStudent, namesAndLastname, 
                    timeUp, textExam, idArchiveExam, nameArchiveExam, grade));
            } 
            else { // El estudiante no entregó examen
                Long idStudent = student.getIdStudent();
                String namesAndLastname = student.getPerson().getName() + " " + student.getPerson().getLastname();
                studentExamUps.add(new StudentExamUp(idStudent, namesAndLastname, 
                null, null, null, null, null));
            }

        }

        return studentExamUps;
        
    }


    @PostMapping("/examen/guardar-calificaciones")
    public ResponseEntity<String> guardarCalificaciones(
            @RequestParam("idExam") Long idExam,
            @RequestParam("students[]") List<Long> students,
            @RequestParam("grades[]") List<String> grades) {
        
        Exam examRe = this.examService.getExamByID(idExam);
        List<UploadExam> uploadExams = new ArrayList<>();
        for (UploadExam upExam : this.uploadExamService.listAllUploadExam()) {
            if (upExam.getExam().equals(examRe)) {
                uploadExams.add(upExam);
            }
        }
        List<Grade> gradesAll = this.gradeService.listAllGrade();
        Boolean swt;
        for (int i = 0; i < students.size(); i++) {
            Student student = this.studentService.getStudentByID(students.get(i));
            for (int o = 0; o < uploadExams.size(); o++) {
                swt = false;
                if (student.equals(uploadExams.get(o).getStudent())){
                    for (int x = 0; x < gradesAll.size(); x++) {
                        if (gradesAll.get(x).getUploadExam().equals(uploadExams.get(o))){
                            Grade gradeToUpdate = gradesAll.get(x);
                            gradeToUpdate.setGradeExam(grades.get(i));
                            this.gradeService.updateGrade(gradeToUpdate);
                            swt = true;
                            //System.out.println(gradeToUpdate.getGradeExam() + "-- UPDATE --");
                        }
                    }
                    if (!swt) {
                        Grade gradeNew = new Grade(null, grades.get(i), uploadExams.get(o));
                        this.gradeService.createGrade(gradeNew);
                        //System.out.println(gradeNew.getGradeExam() + "-- CREADO --");
                    }
                }
            }
        }
        return ResponseEntity.ok("Calificaciones guardadas correctamente");
    }

}
