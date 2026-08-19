package com.example.backend.service.media;

import com.example.backend.entity.MediaType;

import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.BucketExistsArgs;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.InputStream;
import java.time.LocalDate;
import java.util.Map;
import java.util.UUID;

@Service
public class MinioMediaStorageService implements MediaStorageService {

    private static final Map<String, MediaType> SUPPORTED_CONTENT_TYPES = Map.of(
            "image/jpeg", MediaType.IMAGE,
            "image/png", MediaType.IMAGE,
            "image/webp", MediaType.IMAGE,
            "image/gif", MediaType.IMAGE,
            "video/mp4", MediaType.VIDEO,
            "video/webm", MediaType.VIDEO,
            "video/quicktime", MediaType.VIDEO
    );

    private static final Map<String, String> EXTENSIONS = Map.of(
            "image/jpeg", ".jpg",
            "image/png", ".png",
            "image/webp", ".webp",
            "image/gif", ".gif",
            "video/mp4", ".mp4",
            "video/webm", ".webm",
            "video/quicktime", ".mov"
    );

    private final MinioClient minioClient;
    private final String bucket;
    private final String publicUrl;

    public MinioMediaStorageService(
            @Value("${media.minio.endpoint}") String endpoint,
            @Value("${media.minio.access-key}") String accessKey,
            @Value("${media.minio.secret-key}") String secretKey,
            @Value("${media.minio.bucket}") String bucket,
            @Value("${media.minio.public-url}") String publicUrl
    ) {
        this.minioClient = MinioClient.builder()
                .endpoint(endpoint)
                .credentials(accessKey, secretKey)
                .build();
        this.bucket = bucket;
        this.publicUrl = publicUrl.replaceAll("/$", "");
    }

    @Override
    public StoredMedia storePostMedia(MultipartFile file, Long ownerId) {

        return store(file, ownerId, "posts", false);
    }

    @Override
    public StoredMedia storeProfileImage(MultipartFile file, Long ownerId, String purpose) {

        if (!"avatar".equals(purpose) && !"cover".equals(purpose)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Type de média de profil invalide");
        }

        return store(file, ownerId, "profiles/" + purpose, true);
    }

    private StoredMedia store(MultipartFile file, Long ownerId, String directory, boolean imageOnly) {

        if (file.isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Un fichier sélectionné est vide"
            );
        }

        String contentType = file.getContentType();
        MediaType type = contentType == null
                ? null
                : SUPPORTED_CONTENT_TYPES.get(contentType);

        if (type == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Format non pris en charge. Utilisez JPG, PNG, WEBP, GIF, MP4, WEBM ou MOV."
            );
        }

        if (imageOnly && type != MediaType.IMAGE) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Le profil accepte uniquement des images");
        }

        String objectName = directory + "/" + ownerId + "/" + LocalDate.now()
                + "/" + UUID.randomUUID() + EXTENSIONS.get(contentType);

        try (InputStream inputStream = file.getInputStream()) {
            ensureBucketExists();
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucket)
                            .object(objectName)
                            .stream(inputStream, file.getSize(), -1)
                            .contentType(contentType)
                            .build()
            );
        } catch (Exception exception) {
            throw new ResponseStatusException(
                    HttpStatus.SERVICE_UNAVAILABLE,
                    "Impossible d'enregistrer le média pour le moment",
                    exception
            );
        }

        return new StoredMedia(
                type,
                publicUrl + "/" + bucket + "/" + objectName
        );
    }

    private void ensureBucketExists() throws Exception {

        if (!minioClient.bucketExists(
                BucketExistsArgs.builder().bucket(bucket).build()
        )) {
            minioClient.makeBucket(
                    MakeBucketArgs.builder().bucket(bucket).build()
            );
        }
    }
}
