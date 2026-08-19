package com.example.backend.service.media;

import com.example.backend.entity.MediaType;

import org.springframework.web.multipart.MultipartFile;

public interface MediaStorageService {

    StoredMedia storePostMedia(MultipartFile file, Long ownerId);

    StoredMedia storeProfileImage(MultipartFile file, Long ownerId, String purpose);

    record StoredMedia(MediaType type, String url) {
    }
}
