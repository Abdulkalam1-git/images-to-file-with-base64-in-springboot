package com.telusko.files.controller;

import com.telusko.files.Model.ImageFile;
import com.telusko.files.service.ImageFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/images")
public class ImageController {

    private final ImageFileService imageService;

    @Autowired
    public ImageController(ImageFileService imageService) {
        this.imageService = imageService;
    }

    @PostMapping("/upload")
    public ResponseEntity<String> uploadImage(@RequestParam("file") MultipartFile file) {
        try {
            if (file.isEmpty()) {
                return new ResponseEntity<>("Please select a valid image file to upload",HttpStatus.BAD_REQUEST);
            }

            // Process and save the image file
            ImageFile savedImage = imageService.processAndSaveImage(file);
            return new ResponseEntity<>("Image uploaded and saved successfully: " + savedImage.getFilePath(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Image upload failed: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }
}
