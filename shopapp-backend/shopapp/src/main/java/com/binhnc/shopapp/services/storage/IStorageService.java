package com.binhnc.shopapp.services.storage;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface IStorageService {
    String storeFile(MultipartFile file) throws IOException;
}
