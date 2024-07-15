package com.huuloc.bookstore.bbook.service;

import com.google.auth.Credentials;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import com.huuloc.bookstore.bbook.entity.Image;
import com.huuloc.bookstore.bbook.repository.ImageRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Objects;
import java.util.UUID;

@Service
@Slf4j
public class ImageService {
    @Value("${app.firebase.bucket:bbook-3da18.appspot.com}")
    private String BUCKET_NAME;
    @Value("${app.firebase.file:bbook-firebase-adminsdk.json}")
    private String FIREBASE_PRIVATE_KEY;

    @Autowired
    private ImageRepository imageRepository;

    @Retryable(retryFor = Exception.class, maxAttempts = 3)
    public Image upload(MultipartFile multipartFile) {
        String fileName = UUID.randomUUID().toString().concat(
                getExtension(Objects.requireNonNull(multipartFile.getOriginalFilename()
                )));
        try {
            File file = convertToFile(multipartFile, fileName);
            String url = uploadFile(file, fileName);
            file.delete();
            Image image = Image.builder()
                    .name(fileName)
                    .url(url)
                    .build();
            return imageRepository.save(image);
        } catch (Exception e) {
            log.error("Error occurred: {0}", e);
            return null;
        }
    }

    public Image upload(byte[] bytes, String fileName) {
        try {
            fileName = UUID.randomUUID().toString().concat(
                    getExtension(Objects.requireNonNull(fileName)));
            File file = new File(fileName);
            try (FileOutputStream fos = new FileOutputStream(file)) {
                fos.write(bytes);
            }
            String url = uploadFile(file, fileName);
            file.delete();
            Image image = Image.builder()
                    .name(fileName)
                    .url(url)
                    .build();
            return imageRepository.save(image);
        } catch (Exception e) {
            log.error("Error occurred: {0}", e);
            return null;
        }
    }

    public Image getImageById(Long id) {
        return imageRepository.findById(id).orElse(null);
    }

    private String uploadFile(File file, String fileName) throws IOException {
        if (FIREBASE_PRIVATE_KEY == null) {
            throw new RuntimeException("Firebase private key is not found. " +
                    "Please set 'app.firebase.file' in application.properties");
        }
        BlobId blobId = BlobId.of(BUCKET_NAME, fileName);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType("media").build();
        try (InputStream inputStream = ImageService.class.getClassLoader()
                .getResourceAsStream(FIREBASE_PRIVATE_KEY)) {
            if (inputStream == null) {
                throw new RuntimeException("Firebase private key is not found. " +
                        "Please check 'app.firebase.file' in application.properties");
            }
            Credentials credentials = GoogleCredentials.fromStream(inputStream);
            Storage storage = StorageOptions.newBuilder().setCredentials(credentials).build().getService();
            storage.create(blobInfo, Files.readAllBytes(file.toPath()));
        }

        String DOWNLOAD_URL = "https://firebasestorage.googleapis.com/v0/b/" + BUCKET_NAME + "/o/%s?alt=media";

        return String.format(DOWNLOAD_URL, URLEncoder.encode(fileName, StandardCharsets.UTF_8));
    }

    private File convertToFile(MultipartFile multipartFile, String fileName) throws IOException {
        File tempFile = new File(fileName);
        try (FileOutputStream fos = new FileOutputStream(tempFile)) {
            fos.write(multipartFile.getBytes());
        }
        return tempFile;
    }

    private String getExtension(String fileName) {
        return fileName.substring(fileName.lastIndexOf("."));
    }
}