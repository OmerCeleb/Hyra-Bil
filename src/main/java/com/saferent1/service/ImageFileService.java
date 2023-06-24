package com.saferent1.service;

import com.saferent1.domain.ImageData;
import com.saferent1.domain.ImageFile;
import com.saferent1.exception.ResourceNotFoundException;
import com.saferent1.exception.message.ErrorMessage;
import com.saferent1.repository.ImageFileRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Objects;

@Service
public class ImageFileService {

    private final ImageFileRepository imageFileRepository;


    public ImageFileService(ImageFileRepository imageFileRepository) {
        this.imageFileRepository = imageFileRepository;
    }


    public String saveImage(MultipartFile file) {

        ImageFile imageFile = null;
        // !!! name;
        String fileName =
                StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));

        // !!! Data
        try {
            ImageData imageData = new ImageData(file.getBytes());
            imageFile = new ImageFile(fileName, file.getContentType(), imageData);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }

        imageFileRepository.save(imageFile);

        return imageFile.getId();
    }

    public ImageFile getImageById(String id) {

        ImageFile imageFile = imageFileRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException(String.format(ErrorMessage.IMAGE_NOT_FOUND_MESSAGE, id)));

        return imageFile;

    }
}
