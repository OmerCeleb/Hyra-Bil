package com.saferent1.service;

import com.saferent1.domain.ImageData;
import com.saferent1.domain.ImageFile;
import com.saferent1.dto.ImageFileDTO;
import com.saferent1.exception.ResourceNotFoundException;
import com.saferent1.exception.message.ErrorMessage;
import com.saferent1.repository.ImageFileRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

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

    public List<ImageFileDTO> getAllImages() {

        List<ImageFile> imageFileDTOS = imageFileRepository.findAll();
        // image1 : localhost8080/files/download/id

        List<ImageFileDTO> imageFileDTOS1 = imageFileDTOS.stream().map(inFile -> {
            // URL skappas
            String imageUrl = ServletUriComponentsBuilder.
                    fromCurrentContextPath().//localhost:8080
                            path("files/download").//localhost:8080/files/download
                            path(inFile.getId()).toUriString();//localhost:8080/files/download/id

            return new ImageFileDTO(inFile.getName(),
                    imageUrl,
                    inFile.getType(),
                    inFile.getLength());
        }).collect(Collectors.toList());

        return imageFileDTOS1;
    }

    public void removeById(String id) {

        ImageFile imageFile = getImageById(id);
        imageFileRepository.delete(imageFile);

    }
}
