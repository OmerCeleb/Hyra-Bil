package com.saferent1.mapper;

import com.saferent1.domain.Car;
import com.saferent1.domain.ImageFile;
import com.saferent1.dto.CarDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface CarMapper {

    //!!! CarDTO --> Car

    @Mapping(target = "image", ignore = true)
        //String!!!
    Car carDTOToCar(CarDTO carDTO);

    //1!!List.CarDTO ---> List.Car
    List<CarDTO> map(List<Car> cars);


    // Car --> CarDTO
    @Mapping(source = "image", target = "image", qualifiedByName = "getImageAsString")
    CarDTO carToCarDTO(Car car);

    @Named("getImageAsString")
    public static Set<String> getImageIds(Set<ImageFile> imageFiles) {

        Set<String> imgs;
        imgs = imageFiles.stream().
                map(imageFile -> imageFile.getId().toString())
                .collect(Collectors.toSet());

        return imgs;


    }
}
