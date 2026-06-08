package com.nexuszen.auth.services;

import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;

public interface StorageService {
    String store(MultipartFile file, String path) throws IOException;
    void delete(String filename) throws IOException;
    byte[] load(String filename) throws IOException;
}
