package com.nexuszen.auth.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import jakarta.annotation.PostConstruct;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class LocalStorageService implements StorageService {

    @Value("${app.storage.avatar-dir:/usr/app/data/uploads}")
    private String storagePath;

    @PostConstruct
    public void init() {
        try {
            Files.createDirectories(Paths.get(storagePath));
        } catch (IOException e) {
            throw new RuntimeException("Could not initialize storage location", e);
        }
    }

    @Override
    public String store(MultipartFile file, String pathPrefix) throws IOException {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("Failed to store empty file.");
        }
        
        String originalFilename = file.getOriginalFilename();
        String extension = "";
        if (originalFilename != null && originalFilename.contains(".")) {
            extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        }
        
        String newFilename = (pathPrefix != null ? pathPrefix + "_" : "") + UUID.randomUUID().toString() + extension;
        Path destinationFile = Paths.get(storagePath).resolve(Paths.get(newFilename)).normalize().toAbsolutePath();
        
        if (!destinationFile.getParent().equals(Paths.get(storagePath).toAbsolutePath())) {
            throw new SecurityException("Cannot store file outside current directory.");
        }
        
        file.transferTo(destinationFile);
        
        // Return relative URL for frontend or identifier
        return "/api/auth/storage/" + newFilename; 
    }

    @Override
    public void delete(String filename) throws IOException {
        // Extract filename from URL if necessary
        if (filename.startsWith("/api/auth/storage/")) {
            filename = filename.replace("/api/auth/storage/", "");
        }
        Path file = Paths.get(storagePath).resolve(filename).normalize().toAbsolutePath();
        if (Files.exists(file)) {
            Files.delete(file);
        }
    }

    @Override
    public byte[] load(String filename) throws IOException {
        Path file = Paths.get(storagePath).resolve(filename).normalize().toAbsolutePath();
        return Files.readAllBytes(file);
    }
}
