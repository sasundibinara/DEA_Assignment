package com.alpms.al_paper_management.papers.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;

@Service
public class FileStorageService {
    private final Path root = Paths.get("uploads/papers");

    public String store(MultipartFile file) throws IOException {
        if (file.isEmpty()) throw new IOException("Empty file");
        if (!"application/pdf".equalsIgnoreCase(file.getContentType()))
            throw new IOException("Only PDF allowed");

        Files.createDirectories(root);
        String safeName = Path.of(file.getOriginalFilename()).getFileName().toString();
        String filename = System.currentTimeMillis() + "_" + safeName;
        Path dest = root.resolve(filename);
        Files.copy(file.getInputStream(), dest, StandardCopyOption.REPLACE_EXISTING);
        return dest.toString().replace("\\", "/");
    }
}
