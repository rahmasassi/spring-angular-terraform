package com.mycompany.rentCar.Controllers;

import com.mycompany.rentCar.ApiResponse;
import com.mycompany.rentCar.CarDTO.CarDTO;
import com.mycompany.rentCar.Entities.Cars;
import com.mycompany.rentCar.Entities.Image;
import com.mycompany.rentCar.Services.CarsService;
import com.mycompany.rentCar.Services.UserService;
import com.mycompany.rentCar.Services.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.persistence.NoResultException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/cars")
public class CarsController {
    private final CarsService carsService;
    private final ImageService imageService;
    @Autowired
    public CarsController(CarsService carsService, ImageService imageService,UserService userService) {
        this.carsService = carsService;
        this.imageService = imageService;
    }
    @PostMapping("/addCar")
    public ResponseEntity<ApiResponse> addCarWithUserId(
            @RequestParam("file") MultipartFile file,
            @ModelAttribute Cars car,
            @RequestParam("userId") Long userId) {
        try {
            car.setAgencyId(userId);
            Cars savedCar = carsService.addCar(car, userId);
            Image addedImage = imageService.addImage(file, savedCar.getId());
            ApiResponse response = new ApiResponse("add car successfully : " + savedCar.getId(), savedCar.getId());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("Error add car : " + e.getMessage(), null));
        }
    }

    @PutMapping("/updateCar/{carId}")
    public ResponseEntity<String> updateCar(
            @PathVariable("carId") long carId,
            @RequestParam(value = "file", required = false) MultipartFile file,
            @ModelAttribute Cars updatedCar
    ) {
        try {
            Cars existingCar = carsService.getCarById(carId);
            if (existingCar == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("La voiture avec l'ID " + carId + " n'a pas été trouvée");
            }

            existingCar.setName(updatedCar.getName());
            existingCar.setModel(updatedCar.getModel());
            existingCar.setNb_doors(updatedCar.getNb_doors());
            existingCar.setNb_places(updatedCar.getNb_places());
            existingCar.setAddress(updatedCar.getAddress());
            existingCar.setDescription(updatedCar.getDescription());
            existingCar.setPrice_per_day(updatedCar.getPrice_per_day());
            existingCar.setRegistration_num(updatedCar.getRegistration_num());
            existingCar.setGearbox(updatedCar.getGearbox());

            if (file != null && !file.isEmpty()) {
                Image updatedImage = imageService.updateImage(file, carId);
                existingCar.setImage(updatedImage);
            }

            carsService.updateCar(existingCar);

            return ResponseEntity.ok("Voiture mise à jour avec succès avec l'ID : " + existingCar.getId());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erreur lors de la mise à jour de la voiture : " + e.getMessage());
        }
    }
    @GetMapping("/getAllCars")
    public ResponseEntity<List<CarDTO>> getAllCars() {
        try {
            List<CarDTO> carsWithImages = carsService.getAllCars();
            return ResponseEntity.ok(carsWithImages);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.emptyList());
        }
    }

    @GetMapping("/getCarById/{carId}")
    public ResponseEntity<CarDTO> getCarById(@PathVariable Long carId) {
        try {
            Cars car = carsService.getCarById(carId);
            if (car != null) {
                CarDTO carDTO = new CarDTO(car);
                return ResponseEntity.ok(carDTO);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(null);
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }

    @GetMapping("/searchCarsByName")
    public ResponseEntity<List<CarDTO>> searchCarsByName(@RequestParam String name) {
    try {
        List<CarDTO> matchingCars = carsService.getCarsByName(name);

        if (matchingCars.isEmpty()) {
            return ResponseEntity.ok(new ArrayList<>());
        } else {
            return ResponseEntity.ok(matchingCars);
        }
    } catch (NoResultException e) {
        return ResponseEntity.ok(new ArrayList<>());
    } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }
}
    @GetMapping("/searchCarsByModel")
    public ResponseEntity<List<CarDTO>> searchCarsByModel(@RequestParam String model) {
        try {
            List<CarDTO> matchingCars = carsService.getCarsByModel(model);

            if (matchingCars.isEmpty()) {
                return ResponseEntity.ok(new ArrayList<>());
            } else {
                return ResponseEntity.ok(matchingCars);
            }
        } catch (NoResultException e) {
            return ResponseEntity.ok(new ArrayList<>());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
    @GetMapping("/searchCarsByAddress")
    public ResponseEntity<List<CarDTO>> searchCarsByAddress(@RequestParam String address) {
        try {
            List<CarDTO> matchingCars = carsService.getCarsByAddress(address);

            if (matchingCars.isEmpty()) {
                return ResponseEntity.ok(new ArrayList<>());
            } else {
                return ResponseEntity.ok(matchingCars);
            }
        } catch (NoResultException e) {
            return ResponseEntity.ok(new ArrayList<>());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
    @GetMapping("/searchCarsByModelAndAddress")
    public ResponseEntity<List<CarDTO>> searchCarsByModelAndAddress(
            @RequestParam String model,
            @RequestParam String address) {
        try {
            List<CarDTO> matchingCars = carsService.getCarsByModelAndAddress(model, address);
            return ResponseEntity.ok(matchingCars);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
    @GetMapping("/searchCarsName")
    public ResponseEntity<List<CarDTO>> searchCars(@RequestParam String searchTerm) {
        try {
            List<CarDTO> matchingCars = carsService.searchCarsByName(searchTerm);
            return ResponseEntity.ok(matchingCars);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
    @GetMapping("/searchCarsModel")
    public ResponseEntity<List<CarDTO>> searchCarsModel(@RequestParam String searchTerm) {
        try {
            List<CarDTO> matchingCars = carsService.searchCarsByModel(searchTerm);
            return ResponseEntity.ok(matchingCars);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
    @GetMapping("/searchCarsModelAndAddress")
    public ResponseEntity<List<CarDTO>> searchCarsModelAndAddress(@RequestParam String searchTermModel ,@RequestParam String searchTermAddress  ) {
        try {
            List<CarDTO> matchingCars = carsService.searchCarsByModelAndAddress(searchTermModel, searchTermAddress );
            return ResponseEntity.ok(matchingCars);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
    @GetMapping("/searchCarsByModelAndAddressONEParametrs")
    public ResponseEntity<List<CarDTO>> searchCarsByModelAndAddressONEParametrs(@RequestParam String searchTerm) {
        try {
            String model = extractModelFromSearchTerm(searchTerm);
            String address = extractAddressFromSearchTerm(searchTerm);
            List<CarDTO> matchingCars = carsService.searchCarsByModelAndAddress(model, address);
            return ResponseEntity.ok(matchingCars);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
    private String extractModelFromSearchTerm(String searchTerm) {
        String[] parts = searchTerm.split(" ");

        if (parts.length > 0) {
            return parts[0];
        } else if (parts.length > 1){
            return parts[1];
        }
        else {
            return "Modèle non spécifié";
        }
    }
    private String extractAddressFromSearchTerm(String searchTerm) {
        String[] parts = searchTerm.split(" ");

        if (parts.length > 1) {
            return parts[1];
        } else if (parts.length > 0){
            return parts[0];
        }
        else {
            return "Adresse non spécifiée";
        }
    }
    @GetMapping("/search")
    public ResponseEntity<List<CarDTO>> searchCarsBySingleParameter(@RequestParam String searchTerm) {
        try {
            List<CarDTO> matchingCars = carsService.searchCarsBySingleParameter(searchTerm);
            return ResponseEntity.ok(matchingCars);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
    @DeleteMapping("/delete/{carId}")
    public ResponseEntity<String> deleteCar(@PathVariable Long carId) {
        try {
            carsService.deleteCar(carId);
            return ResponseEntity.ok("Voiture supprimée avec succès avec l'ID : " + carId);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erreur lors de la suppression de la voiture : " + e.getMessage());
        }
    }

    @GetMapping("/getCarsByAgencyId/{agencyId}")
    public ResponseEntity<List<CarDTO>> getCarsByAgencyId(@PathVariable Long agencyId) {
        try {
            List<CarDTO> carsByAgency = carsService.getCarsByAgencyId(agencyId);
            return ResponseEntity.ok(carsByAgency);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.emptyList());
        }
    }

}
