package com.saferent1.service;

import com.saferent1.domain.Car;
import com.saferent1.domain.ImageFile;
import com.saferent1.dto.CarDTO;
import com.saferent1.exception.ConflictException;
import com.saferent1.exception.message.ErrorMessage;
import com.saferent1.mapper.CarMapper;
import com.saferent1.repository.CarRepository;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class CarService {

    private final CarRepository carRepository;

    private final ImageFileService imageFileService;
    private final CarMapper carMapper;

    public CarService(CarRepository carRepository, ImageFileService imageFileService, CarMapper carMapper) {
        this.carRepository = carRepository;
        this.imageFileService = imageFileService;
        this.carMapper = carMapper;
    }


    public void saveCar(String imageId, CarDTO carDTO) {

        // !!! ImageId, finns i repo?
        ImageFile imageFile = imageFileService.findImageById(imageId);

        // !!! ImageId, har använt en annan bil?
        Integer usedCarCount = carRepository.findCarCountByImageId(imageFile.getId());
        if (usedCarCount > 0) {
            throw new ConflictException(ErrorMessage.IMAGE_USED_MESSAGE);
        }

        // Mapper->
        Car car = carMapper.carDTOToCar(carDTO);

        //imageinfo lägger till car
        Set<ImageFile> imFiles = new HashSet<>();
        imFiles.add(imageFile);
        car.setImage(imFiles);
        carRepository.save(car);


    }

    public List<CarDTO> getAllCars() {

        List<Car> cars = carRepository.findAll();
        //mapper
        return carMapper.map(cars);
    }
}
