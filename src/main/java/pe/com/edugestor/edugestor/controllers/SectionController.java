package pe.com.edugestor.edugestor.controllers;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pe.com.edugestor.edugestor.models.Course;
import pe.com.edugestor.edugestor.models.Day;
import pe.com.edugestor.edugestor.models.Material;
import pe.com.edugestor.edugestor.models.Professor;
import pe.com.edugestor.edugestor.models.Section;
import pe.com.edugestor.edugestor.models.Student;
import pe.com.edugestor.edugestor.services.CourseService;
import pe.com.edugestor.edugestor.services.DayService;
import pe.com.edugestor.edugestor.services.MaterialService;
import pe.com.edugestor.edugestor.services.ProfessorService;
import pe.com.edugestor.edugestor.services.SectionService;
import pe.com.edugestor.edugestor.services.StudentService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;


@Controller
@RequestMapping("/secciones")
public class SectionController {

    @Autowired
    private CourseService courseService;
    @Autowired
    private ProfessorService professorService;
    @Autowired
    private StudentService studentService;
    @Autowired
    private SectionService sectionService;
    @Autowired
    private DayService dayService;

    @Autowired
    private MaterialService materialService;

    private Course courseDefault = new Course(null, null, null, null, null);
    
    // --------------------------------------------------------------------------------------------- //
    // POR VER //
    @GetMapping("/{url}")
    public String goViewSections(@PathVariable String url, Model model) {
        
        String[] parts = url.split("-"); String idCourse = parts[parts.length - 1]; 
        
        Course courseSelected = this.courseService.getCourseByID(Long.parseLong(idCourse));

        model.addAttribute("course", courseSelected);
        model.addAttribute("sections", courseSelected.getSections());
        
        return "professor/section-course"; 
    }
    // --------------------------------------------------------------------------------------------- //

    @GetMapping("/nuevo")
    public String goViewNewSection(Model model) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        Professor professorLogIn = professorService.getProfessorLog(userDetails.getUsername());

        List<Course> courses = new ArrayList<>();
        List<Student> students = new ArrayList<>();

        for (Course course : courseService.listAllCourses()) {
            if (course.getProfessor().getIdProfessor().equals(professorLogIn.getIdProfessor())){
                courses.add(course);
            }
        }
        for (Student student : studentService.listAllStudents()) {
            for (int i = 0; student.getProfessor().size() > i; i++){
                if (student.getProfessor().get(i).getIdProfessor().equals(professorLogIn.getIdProfessor())){
                    students.add(student);
                    break;
                }
            }
        }

        model.addAttribute("course", courseDefault);
        model.addAttribute("courses", courses);
        model.addAttribute("students", students);

        return "professor/section-add";
    }
    
    @PostMapping("/guardar")
    public String saveSection(@RequestParam("nameCourse") Long id,
                              @RequestParam("hourStart") LocalTime hourStart,
                              @RequestParam("hourEnd") LocalTime hourEnd,
                              @RequestParam("days") List<Integer> daysOfWeek,
                              @RequestParam("studentsAdded") String studentsAdded) {
        // Lista de ID de Estudiantes Agregados en el curso
        List<Integer> studentIds = Arrays.stream(studentsAdded.split(",")).map(Integer::parseInt).collect(Collectors.toList());
        // Codigo de Seccion
        String codSection = "SC"+getNumberSection(id)+"."+generateCodSection(courseService.getCourseByID(id).getNameCourse())+".M"+LocalDate.now().getMonthValue()+"."+LocalDate.now().getYear();
        // Creacion de Listas para almacenar los objetos
        List<Day> daysToSection = new ArrayList<>();
        List<Student> studentsToSection = new ArrayList<>();
        // Agregado de Objetos en lista de dias
        for (int i = 0; daysOfWeek.size() > i; i++){
            daysToSection.add(this.dayService.getDayById(Long.parseLong(daysOfWeek.get(i)+"")));
        }
        // Agregado de Objetos en lista de estudiantes
        for (int s = 0; studentIds.size() > s; s++){
            studentsToSection.add(this.studentService.getStudentByID(Long.parseLong(studentIds.get(s)+"")));
        } 

        List<Material> materials = new ArrayList<>();
        materials.add(new Material(null, "Introducción",
        this.courseService.getCourseByID(id).getDescription(), null, null));

        // Creacion de objeto seccion y agregado de todos los datos
        Section sectionToSave = new Section(null, codSection, hourStart, hourEnd, "En curso", daysToSection, 
        this.courseService.getCourseByID(id), studentsToSection, materials);

        // Asignacion de datos faltantes
        materials.get(0).setSection(sectionToSave);
        sectionToSave.setMaterials(materials);

        // Insercion en la bd
        this.sectionService.createSection(sectionToSave);
        this.materialService.createMaterial(materials.get(0));
        return "redirect:/cursos";
    }

    private String generateCodSection(String txt){
        StringBuilder result = new StringBuilder();
        for (String word : txt.split("\\s+")) {
            if (!word.isEmpty()) {
                result.append(Character.toUpperCase(word.charAt(0)));
            }
        }
        return result.toString();
    }

    private int getNumberSection(Long idCourse){
        int numberSection = 0;
        if (this.sectionService.listAllSection().size()>0){
            for (Section section : this.sectionService.listAllSection()) {
                if (section.getCourse().getIdCourse().equals(idCourse)){
                    numberSection += 1;
                }
            }
        }
        if (numberSection==0) numberSection = 1;
        else numberSection += 1;
        return numberSection;
    }
    
    
}
