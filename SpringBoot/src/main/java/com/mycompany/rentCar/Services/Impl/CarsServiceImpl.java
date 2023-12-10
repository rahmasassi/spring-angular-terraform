package com.mycompany.rentCar.Services.Impl;

import com.mycompany.rentCar.CarDTO.CarDTO;
import com.mycompany.rentCar.Entities.Cars;
import com.mycompany.rentCar.Entities.Image;
import com.mycompany.rentCar.Repositories.CarsRepository;
import com.mycompany.rentCar.Services.CarsService;
import com.mycompany.rentCar.Services.ImageService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CarsServiceImpl implements CarsService {
    private final CarsRepository carsRepository;

    @Override
    public Cars addCar(Cars car, Long userId) {
        car.setAgencyId(userId);
        Cars savedCar = carsRepository.save(car);
        return savedCar;
    }


    public Cars updateCar(Cars car) {
        return carsRepository.save(car);
    }

     @Override
    public List<CarDTO> getAllCars() {
        List<Cars> allCars = (List<Cars>) carsRepository.findAll();
         List<CarDTO> carsWithImages = new ArrayList<>();

         for (Cars car : allCars) {
             CarDTO carDTO = new CarDTO(car);

             Image image = car.getImage();
             if (image != null) {
                 carDTO.setImageId(image.getId());
                 carDTO.setImageData(image.getData());
                 carDTO.setImageFileType(image.getFileType());
             }

             carsWithImages.add(carDTO);
         }

         return carsWithImages;
    }
    @Override
    public Cars getCarById(Long carId) {
        return carsRepository.findById(carId)
                .orElse(null);
    }
public List<CarDTO> getCarsByName(String name) {
    List<CarDTO> matchingCars = new ArrayList<>();

    try {

        List<CarDTO> allCars = getAllCars();


        for (CarDTO car : allCars) {
            if (car != null && car.getName() != null && car.getName().equalsIgnoreCase(name)) {
                matchingCars.add(car);
            }
        }
    } catch (Exception e) {
        e.printStackTrace();

    }

    return matchingCars;
}


   @Override
    public List<CarDTO> getCarsByModel(String model) {
        List<CarDTO> matchingCars = new ArrayList<>();
        try {
            List<CarDTO> allCars = getAllCars();
            for (CarDTO car : allCars) {
                if (car != null && car.getModel() != null && car.getModel().equalsIgnoreCase(model)) {
                    matchingCars.add(car);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return matchingCars;
    }
    @Override
    public List<CarDTO> getCarsByAddress(String address) {
        List<CarDTO> matchingCars = new ArrayList<>();
        try {
            List<CarDTO> allCars = getAllCars();
            for (CarDTO car : allCars) {
                if (car != null && car.getAddress() != null && car.getAddress().equalsIgnoreCase(address)) {
                    matchingCars.add(car);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return matchingCars;
    }
    @Override
    public List<CarDTO> getCarsByModelAndAddress(String model, String address) {
        List<CarDTO> matchingCars = new ArrayList<>();
        try {
            List<CarDTO> allCars = getAllCars();
            for (CarDTO car : allCars) {
                if (car != null && car.getModel() != null && car.getModel().equalsIgnoreCase(model) &&
                        car.getAddress() != null && car.getAddress().equalsIgnoreCase(address)) {
                    matchingCars.add(car);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return matchingCars;
    }
   @Override
   public List<CarDTO> searchCarsByName(String searchTerm) {
    List<CarDTO> matchingCars = new ArrayList<>();

    try {
        List<CarDTO> allCars = getAllCars();
        for (CarDTO car : allCars) {
            if (car != null && car.getName() != null && car.getName().toLowerCase().contains(searchTerm.toLowerCase())) {
                matchingCars.add(car);
            }
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
    return matchingCars;
}
    @Override
    public List<CarDTO> searchCarsByModel(String searchTerm) {
        List<CarDTO> matchingCars = new ArrayList<>();

        try {
            List<CarDTO> allCars = getAllCars();
            for (CarDTO car : allCars) {
                if (car != null && car.getModel() != null && car.getModel().toLowerCase().contains(searchTerm.toLowerCase())) {
                    matchingCars.add(car);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return matchingCars;
    }

    @Override
    public List<CarDTO> searchCarsByModelAndAddress(String searchTermModel, String searchTermAddress) {
        List<CarDTO> matchingCars = new ArrayList<>();

        try {
            List<CarDTO> allCars = getAllCars();
            for (CarDTO car : allCars) {
                if (car != null &&
                        (car.getModel() != null && car.getModel().toLowerCase().contains(searchTermModel.toLowerCase()) ||
                                car.getAddress() != null && car.getAddress().toLowerCase().contains(searchTermAddress.toLowerCase()))
                ) {
                    matchingCars.add(car);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return matchingCars;
    }
    @Override
    public List<CarDTO> searchCarsBySingleParameter(String searchTerm) {
        List<CarDTO> matchingCars = new ArrayList<>();

        try {
            List<CarDTO> allCars = getAllCars();

            for (CarDTO car : allCars) {
                if (car != null) {
                    if (searchTerm != null && !searchTerm.isEmpty()) {
                        if (searchTerm.matches(".*\\d+.*")) {
                            if (isSearchTermPriceValid(searchTerm, car.getPrice_per_day())) {
                                matchingCars.add(car);
                            }
                        } else if (car.getModel() != null && car.getModel().toLowerCase().contains(searchTerm.toLowerCase())) {
                            matchingCars.add(car);
                        } else if (car.getAddress() != null && car.getAddress().toLowerCase().contains(searchTerm.toLowerCase())) {
                            matchingCars.add(car);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return matchingCars;
    }
    private boolean isSearchTermPriceValid(String searchTermPrice, Float carPrice) {
        try {
            Float price = new Float(searchTermPrice);
            return carPrice.compareTo(price) == 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    @Override
    public void deleteCar(Long carId) {
        Cars carToDelete = carsRepository.findById(carId)
                .orElseThrow(() -> new EntityNotFoundException("La voiture avec l'ID " + carId + " n'a pas été trouvée"));

        carsRepository.delete(carToDelete);
    }

    @Override
    public List<CarDTO> getCarsByAgencyId(Long agencyId) {
        List<Cars> cars = carsRepository.findByAgencyId(agencyId);

        List<CarDTO> carsWithImages = new ArrayList<>();
        for (Cars car : cars) {
            CarDTO carDTO = new CarDTO(car);

            Image image = car.getImage();
            if (image != null) {
                carDTO.setImageId(image.getId());
                carDTO.setImageData(image.getData());
                carDTO.setImageFileType(image.getFileType());
            }

            carsWithImages.add(carDTO);
        }

        return carsWithImages;
    }


}
