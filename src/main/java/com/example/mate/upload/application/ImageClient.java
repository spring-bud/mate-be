package com.example.mate.upload.application;

import org.springframework.web.multipart.MultipartFile;

public interface ImageClient {

    String upload(String objectKey, MultipartFile file);
}
