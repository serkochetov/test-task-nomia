package ru.nomia.test.task.nomia.api;

import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import ru.nomia.test.task.nomia.domain.Section;
import ru.nomia.test.task.nomia.repository.SectionRepository;

import java.util.List;

@RestController
public class ApiController {
    private final SectionRepository sectionRepository;

    @Autowired
    public ApiController(SectionRepository sectionRepository) {
        this.sectionRepository = sectionRepository;
    }

//    @GetMapping("/")
//    ResponseEntity<?> hello() {
//
//        List<Section> allByPath = sectionRepository.findAllByPath("A");
//
//        return ResponseEntity.ok().body(allByPath);
//    }

//    @GetMapping("/{path}")
//    ResponseEntity<?> findAllByPath(@PathVariable String path) {
//
//        List<Section> allByPath = sectionRepository.findAllByPath(path);
//
//        return ResponseEntity.ok().body(allByPath);
//    }
//
//    @GetMapping("/{path}/count")
//    ResponseEntity<?> countByPath(@PathVariable String path) {
//
//        return ResponseEntity.ok().body(sectionRepository.countByPath(path));
//    }
//
//    @DeleteMapping("/{path}")
//    ResponseEntity<?> deleteAllByPath(@PathVariable String path) {
//
//        sectionRepository.deleteAllByPath(path);
//        return ResponseEntity.ok().body("deleted");
//    }
//
//    @GetMapping("/moveRootTreeDown")
//    ResponseEntity<?> moveRootTreeDown() {
//
//        sectionRepository.moveRootTreeDown("1.2.4", "3");
//        return ResponseEntity.ok().body("moveRootTreeDown");
//    }
//
//    @GetMapping("/moveNonRootTreeDown")
//    ResponseEntity<?> moveNonRootTreeDown() {
//
//        sectionRepository.moveNonRootTreeDown("1.2.4", "1.3");
//        return ResponseEntity.ok().body("moveNonRootTreeDown");
//    }







//    @GetMapping("/copyTree")
//    ResponseEntity<?> copyTree() {
//
//        sectionRepository.copyTree("1.2.4", "1.3");
//        return ResponseEntity.ok().body("copyTree");
//    }
//
//    @GetMapping("/nlevel")
//    ResponseEntity<?> nlevel() {
//
//        List<SectionRepository.TreeNLevel> nLevel = sectionRepository.nlevel();
//
//        return ResponseEntity.ok().body(nLevel);
//    }
}
