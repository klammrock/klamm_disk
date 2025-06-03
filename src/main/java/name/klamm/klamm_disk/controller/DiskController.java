package name.klamm.klamm_disk.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class DiskController {
    @GetMapping("files")
    public List<String> getFiles() {
        return List.of("a.txt", "b.txt");
    }
}
