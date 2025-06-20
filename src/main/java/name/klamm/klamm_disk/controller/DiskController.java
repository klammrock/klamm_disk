package name.klamm.klamm_disk.controller;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@RestController
public class DiskController {
    // curl -X GET localhost:8888/files

    @GetMapping("/files")
    public List<String> getFiles() {
        return List.of("a.txt", "b.txt");
    }

    // curl -X POST localhost:8888/upload -F "file=@text.txt"

    @PostMapping("/upload")
    public String handleFileUpload(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return "Please select a file to upload";
        }

        try {
            // Get file information
            String fileName = file.getOriginalFilename();
            long size = file.getSize();

            // Save file to disk
            byte[] bytes = file.getBytes();
            Path path = Paths.get("uploads/" + fileName);
            Files.write(path, bytes);

            return "File uploaded successfully: " + fileName + " (" + size + " bytes)";
        } catch (IOException e) {
            e.printStackTrace();
            return "File upload failed: " + e.getMessage();
        }
    }

    // curl -O http://localhost:8888/download/text.txt
    // curl -o text2.txt http://localhost:8888/download/text.txt

    @GetMapping("/download/{filename:.+}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String filename) {
        try {
            Path filePath = Paths.get("uploads/" + filename);
            Resource resource = new UrlResource(filePath.toUri());

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                    .body(resource);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}
