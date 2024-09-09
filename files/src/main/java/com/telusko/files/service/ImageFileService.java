package com.telusko.files.service;

import com.telusko.files.Model.ImageFile;
import com.telusko.files.repository.ImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Base64;

@Service
public class ImageFileService {

    private final ImageRepository imageRepository;
    private final String uploadDir = "C:\\Users\\abdul\\OneDrive\\ecommerce"; //path directory to save

    @Autowired
    public ImageFileService(ImageRepository imageRepository) {
        this.imageRepository = imageRepository;
    }

    // Process image file- convert to Base64, decode, save to file system, and store info in DB
    public ImageFile processAndSaveImage(MultipartFile file) throws IOException {
        // Convert file to Base64
        String base64Image = convertToBase64(file);

        // Decode Base64 and save file to file system
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        String filePath = Paths.get(uploadDir, fileName).toString();

        //throw error if name is duplicate
        if (fileExists(filePath)) {
            throw new IOException("File with name " + fileName + " already exists.");
        }
        saveFileFromBase64(base64Image, filePath);

        // Create and save ImageFile entity
        return saveImageFile(fileName, filePath, "image/jpeg", file.getSize());
    }

    // Convert MultipartFile to Base64 string
    private String convertToBase64(MultipartFile file) throws IOException {
        byte[] fileContent = file.getBytes();
        return Base64.getEncoder().encodeToString(fileContent);
    }

    // Decode Base64 string and save file to the file system
    private void saveFileFromBase64(String base64Image, String filePath) throws IOException {
        byte[] decodedBytes = Base64.getDecoder().decode(base64Image);
        try (FileOutputStream fos = new FileOutputStream(new File(filePath))) {
            fos.write(decodedBytes);
        }
    }
    // Create and save ImageFile entity
    private ImageFile saveImageFile(String fileName, String filePath, String fileType, long size) {
        ImageFile imageFile = new ImageFile(fileName, filePath, fileType, size);
        return imageRepository.save(imageFile);
    }
    //to check the file name already exist
    private boolean fileExists(String filePath) {
        File file = new File(filePath);
        return file.exists();
    }

}
