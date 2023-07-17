package com.saferent1.service;

import com.saferent1.domain.Car;
import com.saferent1.domain.ImageFile;
import com.saferent1.dto.CarDTO;
import com.saferent1.exception.BadRequestException;
import com.saferent1.exception.ConflictException;
import com.saferent1.exception.ResourceNotFoundException;
import com.saferent1.exception.message.ErrorMessage;
import com.saferent1.mapper.CarMapper;
import com.saferent1.repository.CarRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class CarService {

    private final CarRepository carRepository;

    private final ImageFileService imageFileService;
    private final ReservationService reservationService;
    private final CarMapper carMapper;

    public CarService(CarRepository carRepository, ImageFileService imageFileService, ReservationService reservationService, CarMapper carMapper) {
        this.carRepository = carRepository;
        this.imageFileService = imageFileService;
        this.reservationService = reservationService;
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

    public Page<CarDTO> findAllWithPage(Pageable pageable) {

        Page<Car> cars = carRepository.findAll(pageable);
        return cars.map(car -> carMapper.carToCarDTO(car));

    }

    public CarDTO findById(Long id) {

        Car car = getCar(id);
        return carMapper.carToCarDTO(car);
    }


    // fiins car i repo? ******************************************************
    private Car getCar(Long id) {

        Car car = carRepository.findCarById(id).orElseThrow(() ->
                new ResourceNotFoundException(
                        String.format(ErrorMessage.RESOURCE_NOT_FOUND_EXCEPTION, id)));

        return car;

    }//********************************************


    public void updateCar(Long id, String imageId, CarDTO carDTO) {

        Car car = getCar(id);

        //builtIn?
        if (car.getBuiltIn()) {
            throw new BadRequestException(ErrorMessage.NOT_PERMITTED_METHOD_MESSAGE);
        }

        // !!! har använd denna imageId en annan bil?
        ImageFile imageFile = imageFileService.findImageById(imageId);
        List<Car> carList = carRepository.findCarsByImageId(imageFile.getId());

        // !!! conflict?
        for (Car c : carList) {
            // Long --> long
            if (car.getId().longValue() != c.getId().longValue()) {
                throw new ConflictException(ErrorMessage.IMAGE_USED_MESSAGE);
            }

        }

        car.setAge(carDTO.getAge());
        car.setAirCondition(carDTO.getAirCondition());
        car.setBuiltIn(carDTO.getBuiltIn());
        car.setDoors(carDTO.getDoors());
        car.setFuel(car.getFuel());
        car.setLuggage(carDTO.getLuggage());
        car.setModel(carDTO.getModel());
        car.setPricePerHour(car.getPricePerHour());
        car.setSeats(carDTO.getSeats());
        car.setTransmission(car.getTransmission());

        car.getImage().add(imageFile);

        carRepository.save(car);
    }


    public void removeById(Long id) {

        Car car = getCar(id);

        //builtIn?
        if (car.getBuiltIn()) {
            throw new BadRequestException(ErrorMessage.NOT_PERMITTED_METHOD_MESSAGE);
        }

        //!!! reservation kontrol
        carRepository.delete(car);
        boolean exist = reservationService.existByCar(car);
        if (exist) {
            throw new BadRequestException(ErrorMessage.CAR_USED_BY_RESERVATION_MESSAGE);
        }

    }

    public Car getCarById(Long carId) {

        return carRepository.findById(carId).orElseThrow(() -> new ResourceNotFoundException(
                String.format(ErrorMessage.RESOURCE_NOT_FOUND_EXCEPTION, carId)));

    }
}
