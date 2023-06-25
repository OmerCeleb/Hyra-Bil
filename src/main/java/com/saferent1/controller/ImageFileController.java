package com.saferent1.controller;

import com.saferent1.domain.ImageFile;
import com.saferent1.dto.ImageFileDTO;
import com.saferent1.dto.response.ImageSavedResponse;
import com.saferent1.dto.response.ResponseMessage;
import com.saferent1.service.ImageFileService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/files")
public class ImageFileController {

    private final ImageFileService imageFileService;

    public ImageFileController(ImageFileService imageFileService) {
        this.imageFileService = imageFileService;
    }


    //*******************************************************************************
    // !!! Upload

    @PostMapping("/upload")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ImageSavedResponse> uploadFile(
            @RequestParam("file") MultipartFile file) {

        String imageId = imageFileService.saveImage(file);

        ImageSavedResponse response =
                new ImageSavedResponse(imageId, ResponseMessage.IMAGE_SAVED_RESPONSE_MESSAGE, true);
        return ResponseEntity.ok(response);


    }

    //*******************************************************************************
    // Download

    @GetMapping("/download/{id}")
    public ResponseEntity<byte[]> downloadFile(@PathVariable String id) {

        ImageFile imageFile = imageFileService.getImageById(id);

        return ResponseEntity.ok().header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "attachment;filename=" + imageFile.getName()).
                body(imageFile.getImageData().getData());

    }

    //*******************************************************************************
    // !!! ImageDisplay

    @GetMapping("/display/{id}")
    public ResponseEntity<byte[]> displayFile(@PathVariable String id) {

        ImageFile imageFile = imageFileService.getImageById(id);

        HttpHeaders header = new HttpHeaders();
        header.setContentType(MediaType.IMAGE_PNG);

        return new ResponseEntity<>(imageFile.getImageData().getData(), header, HttpStatus.OK);

    }

    //*******************************************************************************
    //!!! GetAllImages
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<ImageFileDTO>> getAllImages() {
        List<ImageFileDTO> imageFileDTOS = imageFileService.getAllImages();

        return ResponseEntity.ok(imageFileDTOS);
    }


}
