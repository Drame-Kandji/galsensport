package com.example.backend.controller;

import com.example.backend.entity.MediaType;
import com.example.backend.entity.User;
import com.example.backend.service.media.MediaStorageService;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1/media")
public class MediaUploadController {

    private final MediaStorageService mediaStorageService;

    public MediaUploadController(
            MediaStorageService mediaStorageService
    ) {
        this.mediaStorageService = mediaStorageService;
    }

    @PostMapping("/upload")
    @PreAuthorize("hasAnyRole('USER', 'ENTREPRISE')")
    public List<MediaUploadResponse> upload(
            @RequestParam("files") List<MultipartFile> files,
            Authentication authentication
    ) {

        if (files == null || files.isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Sélectionnez au moins un fichier"
            );
        }

        User user = (User) authentication.getPrincipal();

        List<MediaUploadResponse> uploadedMedias = new ArrayList<>();

        for (MultipartFile file : files) {
            MediaStorageService.StoredMedia storedMedia =
                    mediaStorageService.storePostMedia(file, user.getId());

            uploadedMedias.add(new MediaUploadResponse(
                    storedMedia.type(),
                    storedMedia.url()
            ));
        }

        return uploadedMedias;
    }

    @PostMapping("/profile-image")
    @PreAuthorize("hasAnyRole('USER', 'ENTREPRISE')")
    public MediaUploadResponse uploadProfileImage(
            @RequestParam("file") MultipartFile file,
            @RequestParam("purpose") String purpose,
            Authentication authentication
    ) {
        User user = (User) authentication.getPrincipal();
        MediaStorageService.StoredMedia media = mediaStorageService.storeProfileImage(file, user.getId(), purpose);
        return new MediaUploadResponse(media.type(), media.url());
    }

    public record MediaUploadResponse(MediaType type, String url) {
    }
}
