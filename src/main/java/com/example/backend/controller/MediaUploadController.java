package com.example.backend.controller;

import com.example.backend.entity.MediaType;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/media")
public class MediaUploadController {

    private final Path uploadDirectory;

    public MediaUploadController(
            @Value("${media.upload-dir:./uploads}") String uploadDirectory
    ) {
        this.uploadDirectory = Path.of(uploadDirectory)
                .toAbsolutePath()
                .normalize();
    }

    @PostMapping("/upload")
    @PreAuthorize("hasAnyRole('USER', 'ENTREPRISE')")
    public List<MediaUploadResponse> upload(
            @RequestParam("files") List<MultipartFile> files
    ) {

        if (files == null || files.isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Sélectionnez au moins un fichier"
            );
        }

        try {
            Files.createDirectories(uploadDirectory);
        } catch (IOException exception) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Impossible de préparer le stockage des médias",
                    exception
            );
        }

        List<MediaUploadResponse> uploadedMedias = new ArrayList<>();

        for (MultipartFile file : files) {
            uploadedMedias.add(store(file));
        }

        return uploadedMedias;
    }

    private MediaUploadResponse store(MultipartFile file) {

        if (file.isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Un fichier sélectionné est vide"
            );
        }

        String contentType = file.getContentType();
        MediaType mediaType;

        if (contentType != null && contentType.startsWith("image/")) {
            mediaType = MediaType.IMAGE;
        } else if (contentType != null && contentType.startsWith("video/")) {
            mediaType = MediaType.VIDEO;
        } else {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Seules les images et les vidéos sont acceptées"
            );
        }

        String extension = getExtension(file.getOriginalFilename());
        String filename = UUID.randomUUID() + extension;
        Path target = uploadDirectory.resolve(filename).normalize();

        if (!target.startsWith(uploadDirectory)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Nom de fichier invalide");
        }

        try {
            Files.copy(file.getInputStream(), target, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException exception) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Impossible d'enregistrer le média",
                    exception
            );
        }

        String url = ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path("/uploads/")
                .path(filename)
                .toUriString();

        return new MediaUploadResponse(mediaType, url);
    }

    private String getExtension(String filename) {

        if (filename == null) {
            return "";
        }

        int extensionIndex = filename.lastIndexOf('.');

        return extensionIndex >= 0
                ? filename.substring(extensionIndex).replaceAll("[^a-zA-Z0-9.]", "")
                : "";
    }

    public record MediaUploadResponse(MediaType type, String url) {
    }
}
