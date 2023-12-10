package com.mycompany.rentCar.Services;

import com.mycompany.rentCar.CarDTO.CarDTO;
import com.mycompany.rentCar.Entities.Cars;
import com.mycompany.rentCar.Entities.Reservation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;


public interface CarsService {
    Cars addCar(Cars car,Long userId);

//    Cars addCarWithUserId(Cars car, Long userId);

    Cars updateCar(Cars car);
    List<CarDTO> getAllCars();
    Cars getCarById(Long carId);
    List<CarDTO> getCarsByName(String name);
    List<CarDTO> getCarsByModel(String model);
    List<CarDTO> getCarsByAddress(String address);
    List<CarDTO> getCarsByModelAndAddress(String model, String address);
    List<CarDTO> searchCarsByName(String searchTerm);
    List<CarDTO> searchCarsByModel(String searchTerm);
    List<CarDTO> searchCarsByModelAndAddress(String searchTermModel, String searchTermAddress);
    List<CarDTO> searchCarsBySingleParameter(String searchTerm);
    void deleteCar(Long carId);
    @Transactional
    List<CarDTO> getCarsByAgencyId(Long agencyId);
}
